package headway.backend.service;

import headway.backend.dto.dashboard.ExpenseSummary;
import headway.backend.dto.expense.ExpenseCategoryDTO;
import headway.backend.dto.expense.ExpenseDTO;
import headway.backend.dto.expense.SupplierDTO;
import headway.backend.entity.expense.Expense;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ExpenseService {
    public List<ExpenseCategoryDTO> findAllExpenseCategories();
    public ExpenseCategoryDTO createCategory(String name);
    public ExpenseCategoryDTO updateCategory(Long id, String name);
    public SupplierDTO addSupplier(Long categoryId, String name);
    public SupplierDTO updateSupplier(Long categoryId, Long supplierId, String name);
    public void deleteSupplier(Long supplierId);
    public void deleteCategory(Long id);
    public Expense createExpense(String date,
                                 String type,
                                 Long hotelId,
                                 Long categoryId,
                                 Long supplierId,
                                 String amount,
                                 String paymentMethod,
                                 String paymentStatus,
                                 String billNo,
                                 MultipartFile billFile);

    public Page<ExpenseDTO> getAllExpenses(int page,
                                           int size,
                                           String sortString,
                                           String type,
                                           Long hotelId,
                                           Long categoryId,
                                           String from,
                                           String to);

    public ExpenseSummary getSummary(int year, Long hotelId);
}
