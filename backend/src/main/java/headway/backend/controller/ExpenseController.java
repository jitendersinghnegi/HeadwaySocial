package headway.backend.controller;

import org.springframework.core.io.Resource;
import headway.backend.dto.expense.ExpenseCategoryDTO;
import headway.backend.dto.expense.ExpenseDTO;
import headway.backend.dto.expense.SupplierDTO;
import headway.backend.entity.expense.Expense;
import headway.backend.repo.ExpenseRepository;
import headway.backend.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
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

    @PostMapping(
            path = "/create",
            consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }
    )
    public Expense createExpense(
            @RequestParam String date,
            @RequestParam String type,
            @RequestParam Long hotelId,
            @RequestParam Long categoryId,
            @RequestParam Long supplierId,
            @RequestParam String amount,
            @RequestParam String paymentMethod,
            @RequestParam String paymentStatus,
            @RequestParam(required = false) String billNo,
            @RequestPart(name = "bill", required = false) MultipartFile bill
    ) {
        return service.createExpense(
                date,
                type,
                hotelId,
                categoryId,
                supplierId,
                amount,
                paymentMethod,
                paymentStatus,
                billNo,
                bill
        );
    }

    @GetMapping("/records")
    public Page<ExpenseDTO> getAllExpenses(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "20") int size,
                                           @RequestParam(defaultValue = "date,desc") String sort,

                                           @RequestParam(required = false) String type,
                                           @RequestParam(required = false) Long hotelId,
                                           @RequestParam(required = false) Long categoryId,

                                           @RequestParam(required = false) String from,
                                           @RequestParam(required = false) String to) {
        return service.getAllExpenses(page, size, sort, type, hotelId, categoryId, from, to);
    }

    @GetMapping(
            value = "/download-bill",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<Resource> downloadBill(@RequestParam String path) {
        try {
            File file = new File(path);

            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(file.toURI());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(null);
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + file.getName() + "\"")
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
