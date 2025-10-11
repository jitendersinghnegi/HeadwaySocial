package headway.backend.controller;

import headway.backend.dto.CategoryDTO;
import headway.backend.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoryController {
    private CategoryService categoryService;
    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }
    @GetMapping("api/public/v1/categories")
    public List<CategoryDTO> getAllCategories(){
        return categoryService.getAllCategories();
    }
    @PostMapping("api/public/v1/categories")
    public String createCategory(@RequestBody CategoryDTO categoryDTO){
        categoryService.createCategory(categoryDTO);
        return "Category Added";
    }
    @DeleteMapping("api/admin/v1/categories/{categoryId}")
    public String deleteCategory(@PathVariable  Long categoryId){
        String status = categoryService.deleteCategory(categoryId);
        return status;
    }
}
