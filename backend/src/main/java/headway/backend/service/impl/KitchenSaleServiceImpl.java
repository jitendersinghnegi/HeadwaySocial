package headway.backend.service.impl;

import headway.backend.dto.kitchen.KitchenSaleRequest;
import headway.backend.dto.kitchen.KitchenSaleResponse;
import headway.backend.entity.kitchen.KitchenSale;
import headway.backend.entity.kitchen.KitchenSaleItem;
import headway.backend.repo.HotelRepository;
import headway.backend.repo.KitchenSaleItemRepository;
import headway.backend.repo.KitchenSaleRepository;
import headway.backend.service.KitchenSaleService;
import headway.backend.utils.BillNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import headway.backend.service.EmailService;
import headway.backend.service.WhatsAppService;
import headway.backend.entity.stays.Hotel;


import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KitchenSaleServiceImpl implements KitchenSaleService {
    @Autowired
    private final KitchenSaleRepository saleRepo;
    @Autowired
    private final KitchenSaleItemRepository itemRepo;
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
    @Override
    public KitchenSaleResponse createSale(KitchenSaleRequest request) {
        // Fetch hotel name
        String hotelName = hotelRepository.findById(request.getHotelId())
                .map(Hotel::getName)
                .orElse("Unknown Hotel");

        KitchenSale sale = new KitchenSale();
        sale.setBillNumber(billNumberGenerator.generateBillNumber());
        sale.setCreatedAt(LocalDateTime.now());

        // Hotel info
        sale.setHotelId(request.getHotelId());
        sale.setHotelName(hotelName);

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

        return new KitchenSaleResponse(
                sale.getId(),
                sale.getBillNumber(),
                sale.getCreatedAt(),
                sale.getHotelName()
        );
    }

    /**
     * @param itemId
     * @return
     */
    @Override
    public String getItemNameFromDB(Long itemId) {
        // TODO fetch from kitchen items table
        return "Item #" + itemId;
    }
}
