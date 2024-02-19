package com.geekster.wishlistservice.controllers;

import com.geekster.wishlistservice.entity.StoreItem;
import com.geekster.wishlistservice.service.ItemService;
import com.geekster.wishlistservice.utils.dto.GenericSuccessResponse;
import com.geekster.wishlistservice.utils.dto.ItemDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * Items present in the store can be manipulated here by users with ADMIN privileges.
 * However, only a valid token is required to view available items.
 */
@RestController
@RequestMapping("/api/items")
@AllArgsConstructor
public class ItemController {
    private ItemService service;

    @PostMapping("")
    public ResponseEntity<GenericSuccessResponse<List<StoreItem>>> addNewItems(@RequestBody @Valid ItemDto[] newItems) {
        return service.addNewItems(newItems);
    }

    @GetMapping("")
    public ResponseEntity<List<StoreItem>> getAllItems() {
        return service.getAllItems();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreItem> getItemById(@PathVariable long id) {
        return service.getItemById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenericSuccessResponse<StoreItem>> updateItemById(@PathVariable long id, @RequestBody @Valid ItemDto newItem) {
        return service.updateItemById(id, newItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItemById(@PathVariable long id) {
        return service.deleteItemById(id);
    }
}