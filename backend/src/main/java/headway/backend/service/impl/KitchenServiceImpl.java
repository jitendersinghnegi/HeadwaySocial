package headway.backend.service.impl;

import headway.backend.dto.kitchen.KitchenItemCategoryRequest;
import headway.backend.dto.kitchen.KitchenItemRequest;
import headway.backend.entity.kitchen.KitchenItem;
import headway.backend.entity.kitchen.KitchenItemCategory;
import headway.backend.repo.KitchenItemCategoryRepository;
import headway.backend.repo.KitchenItemRepository;
import headway.backend.service.KitchenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class KitchenServiceImpl implements KitchenService {
    @Autowired
    private KitchenItemRepository kitchenItemRepo;
    @Autowired
    private KitchenItemCategoryRepository kitchenItemCategoryRepo;
    /**
     * @param onlyActive
     * @return
     */
    @Override
    public List<KitchenItem> getAllItems(boolean onlyActive) {
        return onlyActive ? kitchenItemRepo.findByIsActiveTrue() : kitchenItemRepo.findAll();
    }

    /**
     * @param id
     * @return
     */
    @Override
    public KitchenItem getItem(Long id) {
        return kitchenItemRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Kitchen item not found: " + id));
    }

    /**
     * @param item
     * @return
     */
    @Override
    public KitchenItem createItem(KitchenItemRequest item) {
        KitchenItem newItem = new KitchenItem();
        newItem.setName(item.getName());
        newItem.setCategory(item.getCategory());
        newItem.setDescription(item.getDescription());
        newItem.setIsActive(item.getIsActive());
        newItem.setTaxRate(item.getTaxRate());
        newItem.setUnitPrice(item.getUnitPrice());
        return kitchenItemRepo.save(newItem);
    }

    /**
     * @param id
     * @param updated
     * @return
     */
    @Override
    public KitchenItem updateItem(Long id, KitchenItemRequest updated) {
        KitchenItem existing = getItem(id);
        existing.setName(updated.getName());
        existing.setCategory(updated.getCategory());
        existing.setDescription(updated.getDescription());
        existing.setUnitPrice(updated.getUnitPrice());
        existing.setTaxRate(updated.getTaxRate());
        existing.setIsActive(updated.getIsActive());
        return kitchenItemRepo.save(existing);
    }

    /**
     * @param id
     */
    @Override
    public void deleteItem(Long id) {
        kitchenItemRepo.deleteById(id);
    }

    /**
     * @param onlyActive
     * @return
     */
    @Override
    public List<KitchenItemCategory> getAllItemCategories(boolean onlyActive) {
        return onlyActive ? kitchenItemCategoryRepo.findByIsActiveTrue() : kitchenItemCategoryRepo.findAll();
    }

    /**
     * @param id
     * @return
     */
    @Override
    public KitchenItemCategory getItemCategory(Long id) {
        return kitchenItemCategoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Kitchen item category not found: " + id));
    }

    /**
     * @param category
     * @return
     */
    @Override
    public KitchenItemCategory createItemCategory(KitchenItemCategoryRequest category) {
        KitchenItemCategory newCategory = new KitchenItemCategory();
        newCategory.setName(category.getName());
        newCategory.setDescription(category.getDescription());
        newCategory.setIsActive(category.getIsActive());
        return kitchenItemCategoryRepo.save(newCategory);
    }

    /**
     * @param id
     * @param updated
     * @return
     */
    @Override
    public KitchenItemCategory updateItemCategory(Long id, KitchenItemCategoryRequest updated) {
        KitchenItemCategory existing = getItemCategory(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setIsActive(updated.getIsActive());
        return kitchenItemCategoryRepo.save(existing);
    }

    /**
     * @param id
     */
    @Override
    public void deleteItemCategory(Long id) {
        kitchenItemCategoryRepo.deleteById(id);
    }
}
