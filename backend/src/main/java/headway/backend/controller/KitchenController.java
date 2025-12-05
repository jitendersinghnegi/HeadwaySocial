package headway.backend.controller;

import headway.backend.dto.kitchen.KitchenItemCategoryRequest;
import headway.backend.dto.kitchen.KitchenItemRequest;
import headway.backend.dto.kitchen.KitchenSaleRequest;
import headway.backend.dto.kitchen.KitchenSaleResponse;
import headway.backend.entity.kitchen.KitchenItem;
import headway.backend.entity.kitchen.KitchenItemCategory;
import headway.backend.service.KitchenSaleService;
import headway.backend.service.KitchenService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
//@CrossOrigin(origins = "*")
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("api/v1/kitchen")
public class KitchenController {
    @Autowired
    private KitchenService kitchenService;
    @Autowired
    private KitchenSaleService saleService;

    @GetMapping("/items")
    public ResponseEntity<List<KitchenItem>> getAllItems(
            @RequestParam(name = "onlyActive", defaultValue = "false") boolean onlyActive) {
        return ResponseEntity.ok(kitchenService.getAllItems(onlyActive));
    }
    @GetMapping("/categories")
    public ResponseEntity<List<KitchenItemCategory>> getAllItemCategories(
            @RequestParam(name = "onlyActive", defaultValue = "false") boolean onlyActive) {
        return ResponseEntity.ok(kitchenService.getAllItemCategories(onlyActive));
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<KitchenItem> getItem(@PathVariable Long id) {
        return ResponseEntity.ok(kitchenService.getItem(id));
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<KitchenItemCategory> getItemCategory(@PathVariable Long id) {
        return ResponseEntity.ok(kitchenService.getItemCategory(id));
    }

    @PostMapping("/items/create")
    public ResponseEntity<KitchenItem> createItem(@RequestBody KitchenItemRequest item) {
        return ResponseEntity.ok(kitchenService.createItem(item));
    }

    @PostMapping("/categories/create")
    public ResponseEntity<KitchenItemCategory> createItemCategory(@RequestBody KitchenItemCategoryRequest category) {
        return ResponseEntity.ok(kitchenService.createItemCategory(category));
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<KitchenItem> updateItem(@PathVariable Long id,
                                                  @RequestBody KitchenItemRequest item) {
        return ResponseEntity.ok(kitchenService.updateItem(id, item));
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<KitchenItemCategory> updateItemCategory(@PathVariable Long id,
                                                  @RequestBody KitchenItemCategoryRequest category) {
        return ResponseEntity.ok(kitchenService.updateItemCategory(id, category));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        kitchenService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteItemCategory(@PathVariable Long id) {
        kitchenService.deleteItemCategory(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/sales")
    public ResponseEntity<KitchenSaleResponse> createSale(
            @RequestBody KitchenSaleRequest request
    ) {
        return ResponseEntity.ok(saleService.createSale(request));
    }

    @GetMapping("/sales")
    public List<KitchenSaleResponse> getSales(
            @RequestParam(required = false) Long hotelId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return saleService.getSalesFiltered(hotelId, from, to);
    }

}
