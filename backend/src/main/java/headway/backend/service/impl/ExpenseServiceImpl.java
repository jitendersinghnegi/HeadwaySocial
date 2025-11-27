package headway.backend.service.impl;

import headway.backend.dto.expense.ExpenseCategoryDTO;
import headway.backend.dto.expense.SupplierDTO;
import headway.backend.entity.expense.ExpenseCategory;
import headway.backend.entity.expense.Supplier;
import headway.backend.repo.ExpenseCategoryRepository;
import headway.backend.repo.SupplierRepository;
import headway.backend.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseCategoryRepository categoryRepo;
    private final SupplierRepository supplierRepo;
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

    private ExpenseCategoryDTO findCategoryDTO(ExpenseCategory category) {
        return ExpenseCategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .suppliers(category.getSuppliers().stream()
                        .map(s -> new SupplierDTO(s.getId(), s.getName()))
                        .toList())
                .build();
    }
}
