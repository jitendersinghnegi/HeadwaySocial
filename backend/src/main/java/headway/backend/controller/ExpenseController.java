package headway.backend.controller;

import headway.backend.dto.expense.ExpenseCategoryDTO;
import headway.backend.dto.expense.SupplierDTO;
import headway.backend.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/expense")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ExpenseController {
    @Autowired
    private final ExpenseService service;

    @GetMapping("/categories")
    public List<ExpenseCategoryDTO> getAllExpenseCategories() {
        return service.findAllExpenseCategories();
    }
    @PostMapping(("/categories/create"))
    public ExpenseCategoryDTO create(@RequestBody Map<String, String> req) {
        return service.createCategory(req.get("name"));
    }
    @PutMapping("/categories/{id}")
    public ExpenseCategoryDTO update(
            @PathVariable Long id,
            @RequestBody Map<String, String> req
    ) {
        return service.updateCategory(id, req.get("name"));
    }
    @DeleteMapping("/categories/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteCategory(id);
    }
    @PostMapping("/categories/{categoryId}/suppliers")
    public SupplierDTO addSupplier(
            @PathVariable Long categoryId,
            @RequestBody Map<String, String> req
    ) {
        return service.addSupplier(categoryId, req.get("name"));
    }

    @PutMapping("/categories/{categoryId}/suppliers/{supplierId}")
    public SupplierDTO updateSupplier(
            @PathVariable Long categoryId,
            @PathVariable Long supplierId,
            @RequestBody Map<String, String> req
    ) {
        return service.updateSupplier(categoryId, supplierId, req.get("name"));
    }
    @DeleteMapping("/categories/{categoryId}/suppliers/{supplierId}")
    public void deleteSupplier(
            @PathVariable Long categoryId,
            @PathVariable Long supplierId
    ) {
        service.deleteSupplier(supplierId);
    }
}
