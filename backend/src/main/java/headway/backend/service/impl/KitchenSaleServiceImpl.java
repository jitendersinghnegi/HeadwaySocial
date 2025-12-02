package headway.backend.service.impl;

import headway.backend.dto.dashboard.DashboardKitchenSummary;
import headway.backend.dto.kitchen.KitchenSaleRequest;
import headway.backend.dto.kitchen.KitchenSaleResponse;
import headway.backend.entity.kitchen.KitchenItem;
import headway.backend.entity.kitchen.KitchenSale;
import headway.backend.entity.kitchen.KitchenSaleItem;
import headway.backend.repo.HotelRepository;
import headway.backend.repo.KitchenItemRepository;
import headway.backend.repo.KitchenSaleItemRepository;
import headway.backend.repo.KitchenSaleRepository;
import headway.backend.service.KitchenSaleService;
import headway.backend.utils.BillNumberGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import headway.backend.service.EmailService;
import headway.backend.service.WhatsAppService;
import headway.backend.entity.stays.Hotel;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KitchenSaleServiceImpl implements KitchenSaleService {
    @Autowired
    private final KitchenSaleRepository saleRepo;
    @Autowired
    private final KitchenSaleItemRepository itemRepo;
    @Autowired
    private final KitchenItemRepository kitchenItemRepository;
    @Autowired
    private final BillNumberGenerator billNumberGenerator;
    @Autowired
    private final EmailService emailService;
    @Autowired
    private final WhatsAppService whatsappService;
    @Autowired
    private final HotelRepository hotelRepository;


    /**
     * @param request
     * @return
     */
    @Transactional
    @Override
    public KitchenSaleResponse createSale(KitchenSaleRequest request) {
        // Fetch hotel name
        if (request.getPaymentMethod() == null || request.getPaymentMethod().isEmpty()) {
            throw new RuntimeException("Payment method is required");
        }
        String hotelName = hotelRepository.findById(request.getHotelId())
                .map(Hotel::getName)
                .orElse("Unknown Hotel");
        KitchenSale sale = new KitchenSale();
        sale.setBillNumber(billNumberGenerator.generateBillNumber());
        sale.setCreatedAt(LocalDateTime.now());

        // Hotel info
        sale.setHotelId(request.getHotelId());
        sale.setHotelName(hotelName);
        sale.setPaymentMethod(request.getPaymentMethod());

        // Financials
        sale.setSubtotal(request.getSubtotal());
        sale.setTotalTax(request.getTotalTax());
        sale.setGrandTotal(request.getGrandTotal());

        // Customer info
        KitchenSaleRequest.CustomerDTO c = request.getCustomer();
        sale.setCustomerName(c.getName());
        sale.setCustomerEmail(c.getEmail());
        sale.setCustomerPhone(c.getPhone());
        sale.setCustomerType(c.getType());
        sale.setRoomNumber(c.getRoomNumber());

        saleRepo.save(sale);

        // Line items
        List<KitchenSaleItem> saleItems = request.getItems().stream()
                .map(i -> KitchenSaleItem.builder()
                        .sale(sale)
                        .itemId(i.getItemId())
                        .itemName(getItemNameFromDB(i.getItemId()))
                        .quantity(i.getQuantity())
                        .unitPrice(i.getUnitPrice())
                        .taxRate(i.getTaxRate())
                        .build())
                .toList();

        itemRepo.saveAll(saleItems);

        // Send Email
        if (request.isSendEmail() && sale.getCustomerEmail() != null) {
            emailService.sendKitchenBillEmail(sale, saleItems);

        }

        // Send WhatsApp
        if (request.isSendWhatsApp() && sale.getCustomerPhone() != null) {
            whatsappService.sendKitchenBill(sale, saleItems);
        }

        List<KitchenSaleResponse.SaleItemResponse> itemResponses = saleItems.stream()
                .map(i -> new KitchenSaleResponse.SaleItemResponse(
                        i.getItemId(),
                        i.getItemName(),
                        i.getQuantity(),
                        i.getUnitPrice(),
                        i.getTaxRate(),
                        i.getUnitPrice() * i.getQuantity()
                ))
                .toList();
        return new KitchenSaleResponse(
                sale.getId(),
                sale.getBillNumber(),
                sale.getPaymentMethod(),
                sale.getCustomerName(),
                sale.getCreatedAt(),
                sale.getHotelName(),
                sale.getSubtotal(),
                sale.getTotalTax(),
                sale.getGrandTotal(),
                itemResponses
        );
    }

    /**
     * @param itemId
     * @return
     */
    @Override
    public String getItemNameFromDB(Long itemId) {
        Optional<KitchenItem> kitchenItem = kitchenItemRepository.findById(itemId);
        if(kitchenItem.isPresent()){
            return kitchenItem.get().getName();
        }else{
        return "Item #" + itemId;}
    }

    /**
     * @param hotelId
     * @param from
     * @param to
     * @return
     */
    @Override
    public List<KitchenSaleResponse> getSalesFiltered(Long hotelId, LocalDate from, LocalDate to) {
        LocalDateTime start = (from != null)
                ? from.atStartOfDay()
                : LocalDate.of(1970, 1, 1).atStartOfDay();

        LocalDateTime end = (to != null)
                ? to.atTime(23, 59, 59)
                : LocalDate.of(3000, 1, 1).atTime(23, 59, 59);
        List<KitchenSale> sales;

        if (hotelId != null) {
            sales = saleRepo.findByHotelIdAndCreatedAtBetween(hotelId, start, end);
        } else {
            sales = saleRepo.findByCreatedAtBetween(start, end);
        }

        return sales.stream().map(this::mapToResponse).toList();
    }

    /**
     * @param year
     * @param hotelId
     * @return
     */
    @Override
    public DashboardKitchenSummary getSummary(int year, Long hotelId) {
        List<KitchenSale> list = (hotelId == null)
                ? saleRepo.findByYear(year)
                : saleRepo.findByYearAndHotel(year, hotelId);

        double total = list.stream()
                .mapToDouble(s -> s.getGrandTotal() != null ? s.getGrandTotal() : 0D)
                .sum();

        return new DashboardKitchenSummary(total);
    }

    private KitchenSaleResponse mapToResponse(KitchenSale sale) {
        // If items are LAZY, make sure they are fetched in the query or with @EntityGraph
        List<KitchenSaleItem> saleItems = sale.getItems();

        List<KitchenSaleResponse.SaleItemResponse> itemResponses = saleItems.stream()
                .map(i -> new KitchenSaleResponse.SaleItemResponse(
                        i.getItemId(),
                        i.getItemName(),
                        i.getQuantity(),
                        i.getUnitPrice(),
                        i.getTaxRate(),
                        i.getUnitPrice() * i.getQuantity()  // lineTotal
                ))
                .toList();

        return new KitchenSaleResponse(
                sale.getId(),
                sale.getBillNumber(),
                sale.getPaymentMethod(),
                sale.getCustomerName(),
                sale.getCreatedAt(),
                sale.getHotelName(),
                sale.getSubtotal(),
                sale.getTotalTax(),
                sale.getGrandTotal(),
                itemResponses
        );
    }
}
