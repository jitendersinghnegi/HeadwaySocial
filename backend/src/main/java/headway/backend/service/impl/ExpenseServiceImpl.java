package headway.backend.service.impl;

import headway.backend.dto.expense.ExpenseCategoryDTO;
import headway.backend.dto.expense.ExpenseDTO;
import headway.backend.dto.expense.SupplierDTO;
import headway.backend.entity.expense.*;
import headway.backend.entity.stays.Hotel;
import headway.backend.repo.ExpenseCategoryRepository;
import headway.backend.repo.ExpenseRepository;
import headway.backend.repo.HotelRepository;
import headway.backend.repo.SupplierRepository;
import headway.backend.service.ExpenseService;
import headway.backend.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseCategoryRepository categoryRepo;
    private final SupplierRepository supplierRepo;
    @Autowired
    private final ExpenseRepository expenseRepository;
    @Autowired
    private final HotelRepository hotelRepository;
    @Autowired
    private final FileStorageService fileStorageService;


    /**
     * @return
     */
    @Override
    public List<ExpenseCategoryDTO> findAllExpenseCategories() {
        return categoryRepo.findAll().stream()
                .map(category -> ExpenseCategoryDTO.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .suppliers(category.getSuppliers().stream()
                                .map(s -> new SupplierDTO(s.getId(), s.getName()))
                                .toList()
                        )
                        .build())
                .toList();
    }

    /**
     * @param name
     * @return
     */
    @Override
    public ExpenseCategoryDTO createCategory(String name) {
        ExpenseCategory cat = categoryRepo.save(
                ExpenseCategory.builder().name(name).build()
        );
        return new ExpenseCategoryDTO(cat.getId(), cat.getName(), List.of());
    }

    /**
     * @param id
     * @param name
     * @return
     */
    @Override
    public ExpenseCategoryDTO updateCategory(Long id, String name) {
        ExpenseCategory cat = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        cat.setName(name);
        categoryRepo.save(cat);

        return findCategoryDTO(cat);
    }

    /**
     * @param categoryId
     * @param name
     * @return
     */
    @Override
    public SupplierDTO addSupplier(Long categoryId, String name) {
        ExpenseCategory category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Supplier supplier = supplierRepo.save(
                Supplier.builder().name(name).category(category).build()
        );

        return new SupplierDTO(supplier.getId(), supplier.getName());
    }

    /**
     * @param categoryId
     * @param supplierId
     * @param name
     * @return
     */
    @Override
    public SupplierDTO updateSupplier(Long categoryId, Long supplierId, String name) {
        Supplier supplier = supplierRepo.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        supplier.setName(name);
        supplierRepo.save(supplier);

        return new SupplierDTO(supplier.getId(), supplier.getName());
    }

    /**
     * @param supplierId
     */
    @Override
    public void deleteSupplier(Long supplierId) {
        supplierRepo.deleteById(supplierId);
    }

    /**
     * @param id
     */
    @Override
    public void deleteCategory(Long id) {
        categoryRepo.deleteById(id);
    }

    /**
     * @param date
     * @param type
     * @param hotelId
     * @param categoryId
     * @param supplierId
     * @param amount
     * @param paymentMethod
     * @param paymentStatus
     * @param billNo
     * @param billFile
     * @return
     */
    @Override
    public Expense createExpense(String date, String type, Long hotelId, Long categoryId, Long supplierId, String amount, String paymentMethod, String paymentStatus, String billNo, MultipartFile billFile) {
        LocalDate expenseDate = LocalDate.parse(date); // expects "yyyy-MM-dd"
        ExpenseType expenseType = ExpenseType.valueOf(type.replace(" ", "_").toUpperCase());
        PaymentMethod pm = PaymentMethod.valueOf(paymentMethod.replace(" ", "_").toUpperCase());
        PaymentStatus ps = PaymentStatus.valueOf(paymentStatus.replace(" ", "_").toUpperCase());

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        ExpenseCategory category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Supplier supplier = supplierRepo.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        BigDecimal amt = new BigDecimal(amount);

        String billPath = fileStorageService.storeBillFile(billFile);
        String billFileName = (billFile != null) ? billFile.getOriginalFilename() : null;

        Expense expense = Expense.builder()
                .date(expenseDate)
                .type(expenseType)
                .hotel(hotel)
                .category(category)
                .supplier(supplier)
                .amount(amt)
                .paymentMethod(pm)
                .paymentStatus(ps)
                .billNo(billNo)
                .billFileName(billFileName)
                .billFilePath(billPath)
                .createdAt(LocalDateTime.now())
                .build();

        return expenseRepository.save(expense);
    }

    /**
     * @return
     */
    @Override
    public Page<ExpenseDTO> getAllExpenses(int page,
                                           int size,
                                           String sortString,
                                           String type,
                                           Long hotelId,
                                           Long categoryId,
                                           String from,
                                           String to) {
        // Parse sorting
        String[] sortParts = sortString.split(",");
        String sortField = sortParts[0];
        Sort.Direction direction = sortParts.length > 1 && sortParts[1].equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        Page<Expense> rawPage = expenseRepository.findAll(pageable);

        // Apply filters manually
        Stream<Expense> stream = rawPage.stream();

        if (type != null && !type.isEmpty()) {
            stream = stream.filter(e -> e.getType().name().equals(type));
        }
        if (hotelId != null) {
            stream = stream.filter(e -> e.getHotel().getId().equals(hotelId));
        }
        if (categoryId != null) {
            stream = stream.filter(e -> e.getCategory().getId().equals(categoryId));
        }
        if (from != null && !from.isBlank()) {
            LocalDate fromDate = LocalDate.parse(from);
            stream = stream.filter(e -> !e.getDate().isBefore(fromDate));
        }

        if (to != null && !to.isBlank()) {
            LocalDate toDate = LocalDate.parse(to);
            stream = stream.filter(e -> !e.getDate().isAfter(toDate));
        }

        List<ExpenseDTO> filteredList = stream
                .map(this::toDTO)
                .toList();

        return new PageImpl<>(filteredList, pageable, filteredList.size());
    }

    private ExpenseCategoryDTO findCategoryDTO(ExpenseCategory category) {
        return ExpenseCategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .suppliers(category.getSuppliers().stream()
                        .map(s -> new SupplierDTO(s.getId(), s.getName()))
                        .toList())
                .build();
    }

    public ExpenseDTO toDTO(Expense e) {
        return ExpenseDTO.builder()
                .id(e.getId())
                .date(e.getDate().toString())
                .type(e.getType().name())

                .hotelId(e.getHotel().getId())
                .hotelName(e.getHotel().getName())

                .categoryId(e.getCategory().getId())
                .categoryName(e.getCategory().getName())

                .supplierId(e.getSupplier().getId())
                .supplierName(e.getSupplier().getName())

                .amount(e.getAmount())
                .paymentMethod(e.getPaymentMethod().name())
                .paymentStatus(e.getPaymentStatus().name())

                .billNo(e.getBillNo())
                .billFileName(e.getBillFileName())
                .billFilePath(e.getBillFilePath())
                .build();
    }
}
