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
        List<Category> categories = categoryRepository.findAll();
        Category category = categories.stream().filter(c -> c.getCategoryId().equals(categoryId)).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Resource Not Found"));

        categoryRepository.delete(category);
        return "Category with category Id:" + categoryId +"deleted";}

    /**
     * @param category
     * @param categoryId
     * @return
     */
    @Override
    public Category updateCategory(CategoryDTO category, Long categoryId) {
        List<Category> categories = categoryRepository.findAll();
        Optional<Category> optionalCategory = categories.stream().filter(c -> c.getCategoryId().equals(categoryId)).findFirst();
        if(optionalCategory.isPresent()){
            Category existingCategory = optionalCategory.get();
            existingCategory.setCategoryName(category.getCategoryName());
            return categoryRepository.save(existingCategory);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Category Not Found");
        }
    }


}
