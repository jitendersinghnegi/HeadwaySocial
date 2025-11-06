package headway.backend.service.impl;

import headway.backend.dto.CategoryDTO;
import headway.backend.entity.category.Category;
import headway.backend.exceptions.APIException;
import headway.backend.exceptions.ResourceNotFoundException;
import headway.backend.repo.CategoryRepository;
import headway.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public List<Category> getAllCategories() {
        List<Category> categories =  categoryRepository.findAll();
        if(categories.isEmpty()){
            throw new APIException("No categories found");
        }
        return categories;
    }

    /**
     * @param category
     * @return
     */
    @Override
    public Void createCategory(CategoryDTO category) {
        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if(savedCategory != null){
            throw new APIException("Category with name "+category.getCategoryName() + "alreadt exists !!!!!!");
        }
        Category categoryTemp = new Category();
        categoryTemp.setCategoryName(category.getCategoryName());
        categoryRepository.save(categoryTemp);
        return null;
    }

    /**
     * @param categoryId
     * @return
     */
    @Override
    public String deleteCategory(Long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if(optionalCategory.isPresent()){
            Category category = optionalCategory.get();
            categoryRepository.delete(category);
            return "Category with category Id:" + categoryId +"deleted";
        }else{
            throw new ResourceNotFoundException("Category","CategoryId",categoryId);
        }
    }

    /**
     * @param category
     * @param categoryId
     * @return
     */
    @Override
    public Category updateCategory(CategoryDTO category, Long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if(optionalCategory.isPresent()){
            Category existingCategory = optionalCategory.get();
            existingCategory.setCategoryName(category.getCategoryName());
            return categoryRepository.save(existingCategory);
        }else{
            throw new ResourceNotFoundException("Category","CategoryId",categoryId);
        }
    }


}
