package headway.backend.controller;

import headway.backend.dto.CategoryDTO;
import headway.backend.entity.category.Category;
import headway.backend.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoryController {
    private CategoryService categoryService;
    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }
    @GetMapping("api/public/v1/categories")
    public ResponseEntity<List<Category>> getAllCategories(){
        List<Category> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories,HttpStatus.OK);
    }
    @PostMapping("api/public/v1/categories")
    public ResponseEntity<String> createCategory(@RequestBody CategoryDTO categoryDTO){
        categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>("Category Added",HttpStatus.CREATED);
    }
    @DeleteMapping("api/admin/v1/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable  Long categoryId){
        try {
            String status = categoryService.deleteCategory(categoryId);
            return new ResponseEntity<>(status, HttpStatus.OK);
        }catch(ResponseStatusException e){
            return new ResponseEntity<>(e.getReason(),e.getStatusCode());
        }
    }
    @PutMapping("api/public/v1/categories/{categoryId}")
    public ResponseEntity<String> updateCategory(@RequestBody CategoryDTO category ,@PathVariable Long categoryId){
        try{
            Category savedCategory = categoryService.updateCategory(category,categoryId);
            return new ResponseEntity<>("Category with category ID "+ categoryId +"Updated",HttpStatus.OK);
        }catch(ResponseStatusException e){
            return new ResponseEntity<>(e.getReason(),e.getStatusCode());
        }

    }
}
