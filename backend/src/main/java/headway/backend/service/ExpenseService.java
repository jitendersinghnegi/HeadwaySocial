package headway.backend.service;

import headway.backend.dto.expense.ExpenseCategoryDTO;
import headway.backend.dto.expense.SupplierDTO;

import java.util.List;

public interface ExpenseService {
    public List<ExpenseCategoryDTO> findAllExpenseCategories();
    public ExpenseCategoryDTO createCategory(String name);
    public ExpenseCategoryDTO updateCategory(Long id, String name);
    public SupplierDTO addSupplier(Long categoryId, String name);
    public SupplierDTO updateSupplier(Long categoryId, Long supplierId, String name);
    public void deleteSupplier(Long supplierId);
    public void deleteCategory(Long id);

}
