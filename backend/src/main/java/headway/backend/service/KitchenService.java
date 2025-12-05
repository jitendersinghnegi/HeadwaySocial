package headway.backend.service;

import headway.backend.dto.kitchen.KitchenItemCategoryRequest;
import headway.backend.dto.kitchen.KitchenItemRequest;
import headway.backend.entity.kitchen.KitchenItem;
import headway.backend.entity.kitchen.KitchenItemCategory;

import java.util.List;

public interface KitchenService {
    public List<KitchenItem> getAllItems(boolean onlyActive);
    public KitchenItem getItem(Long id);
    public KitchenItem createItem(KitchenItemRequest item);
    public KitchenItem updateItem(Long id, KitchenItemRequest updated);
    public void deleteItem(Long id);
    public List<KitchenItemCategory> getAllItemCategories(boolean onlyActive);
    public KitchenItemCategory getItemCategory(Long id);
    public KitchenItemCategory createItemCategory(KitchenItemCategoryRequest item);
    public KitchenItemCategory updateItemCategory(Long id, KitchenItemCategoryRequest updated);
    public void deleteItemCategory(Long id);
}
