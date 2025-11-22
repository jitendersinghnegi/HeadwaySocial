package headway.backend.service.impl;

import headway.backend.dto.kitchen.KitchenItemRequest;
import headway.backend.entity.kitchen.KitchenItem;
import headway.backend.repo.KitchenItemRepository;
import headway.backend.service.KitchenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class KitchenServiceImpl implements KitchenService {
    @Autowired
    private KitchenItemRepository kitchenItemRepo;
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
}
