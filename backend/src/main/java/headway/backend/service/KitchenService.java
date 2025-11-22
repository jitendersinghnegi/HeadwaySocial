package headway.backend.service;

import headway.backend.dto.kitchen.KitchenItemRequest;
import headway.backend.entity.kitchen.KitchenItem;

import java.util.List;

public interface KitchenService {
    public List<KitchenItem> getAllItems(boolean onlyActive);
    public KitchenItem getItem(Long id);
    public KitchenItem createItem(KitchenItemRequest item);
    public KitchenItem updateItem(Long id, KitchenItemRequest updated);
    public void deleteItem(Long id);
}
