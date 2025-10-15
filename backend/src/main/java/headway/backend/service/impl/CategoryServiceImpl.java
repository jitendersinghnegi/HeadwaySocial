package headway.backend.service.impl;

import headway.backend.dto.CategoryDTO;
import headway.backend.entity.category.Category;
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
        return categoryRepository.findAll();
    }

    /**
     * @param category
     * @return
     */
    @Override
    public Void createCategory(CategoryDTO category) {
        Category cat = new Category();
        cat.setCategoryName(category.getCategoryName());
        categoryRepository.save(cat);
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Resource Not Found");
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
        //List<Category> categories = categoryRepository.findAll();
        //Optional<Category> optionalCategory = categories.stream().filter(c -> c.getCategoryId().equals(categoryId)).findFirst();
        if(optionalCategory.isPresent()){
            Category existingCategory = optionalCategory.get();
            existingCategory.setCategoryName(category.getCategoryName());
            return categoryRepository.save(existingCategory);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Category Not Found");
        }
    }


}
