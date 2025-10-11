package headway.backend.service.impl;

import headway.backend.dto.CategoryDTO;
import headway.backend.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class CategoryServiceImpl implements CategoryService {
    private List<CategoryDTO> categories = new ArrayList<>();
    @Override
    public List<CategoryDTO> getAllCategories() {
        return categories;
    }

    /**
     * @param category
     * @return
     */
    @Override
    public Void createCategory(CategoryDTO category) {
        categories.add(category);
        return null;
    }

    /**
     * @param categoryId
     * @return
     */
    @Override
    public String deleteCategory(Long categoryId) {
        CategoryDTO category = categories.stream().filter(c -> c.getCategoryId().equals(categoryId)).findFirst().orElse(null);
        if(category == null){
            return "Category not found";
        }else{
        categories.remove(category);
        return "Category with category Id:" + categoryId +"deleted";}
    }

}
