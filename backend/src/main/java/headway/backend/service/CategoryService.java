package headway.backend.service;

import headway.backend.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAllCategories();
    Void createCategory(CategoryDTO category);
    String deleteCategory(Long categoryId);
}
