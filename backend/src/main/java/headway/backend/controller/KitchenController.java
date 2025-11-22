package headway.backend.controller;

import headway.backend.dto.kitchen.KitchenItemRequest;
import headway.backend.entity.kitchen.KitchenItem;
import headway.backend.service.KitchenService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@CrossOrigin(origins = "*")
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("api/v1/kitchen")
public class KitchenController {
    @Autowired
    private KitchenService kitchenService;

    @GetMapping("/items")
    public ResponseEntity<List<KitchenItem>> getAllItems(
            @RequestParam(name = "onlyActive", defaultValue = "false") boolean onlyActive) {
        return ResponseEntity.ok(kitchenService.getAllItems(onlyActive));
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<KitchenItem> getItem(@PathVariable Long id) {
        return ResponseEntity.ok(kitchenService.getItem(id));
    }

    @PostMapping("/items/create")
    public ResponseEntity<KitchenItem> createItem(@RequestBody KitchenItemRequest item) {
        return ResponseEntity.ok(kitchenService.createItem(item));
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<KitchenItem> updateItem(@PathVariable Long id,
                                                  @RequestBody KitchenItemRequest item) {
        return ResponseEntity.ok(kitchenService.updateItem(id, item));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        kitchenService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }


}
