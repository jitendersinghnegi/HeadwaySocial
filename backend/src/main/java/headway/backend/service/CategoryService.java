package headway.backend.service;

import headway.backend.dto.CategoryDTO;
import headway.backend.entity.category.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Void createCategory(CategoryDTO category);
    String deleteCategory(Long categoryId);
    Category updateCategory(CategoryDTO category, Long categoryId);
}
