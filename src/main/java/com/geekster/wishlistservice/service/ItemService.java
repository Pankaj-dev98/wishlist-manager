package com.geekster.wishlistservice.service;

import com.geekster.wishlistservice.entity.Customer;
import com.geekster.wishlistservice.entity.StoreItem;
import com.geekster.wishlistservice.repository.ItemRepository;
import com.geekster.wishlistservice.utils.dto.GenericSuccessResponse;
import com.geekster.wishlistservice.utils.dto.ItemDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * An ADMIN is allowed to add or remove items from the store.
 * Also, he may choose to update an existing item in the store. All these provisions are directly accessible using RESTful messaging within the system.
 * To view current listed items, the user shall only be bearer of a valid token.
 */
@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository repository;

    public ResponseEntity<GenericSuccessResponse<List<StoreItem>>> addNewItems(ItemDto... newItems) {
        final List<StoreItem> items = new ArrayList<>(newItems.length);
        Arrays.asList(newItems).forEach(i -> items.add(new StoreItem(i.getItemName(), i.getPrice(), i.getInStock()))
        );
        repository.saveAll(items);


        GenericSuccessResponse<List<StoreItem>> response =
                new GenericSuccessResponse<>("Item was added successfully!",
                        items,
                        LocalDateTime.now(),
                        HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<List<StoreItem>> getAllItems() {
        return ResponseEntity.ok(repository.findAll());
    }

    public ResponseEntity<StoreItem> getItemById(long id) {
        Optional<StoreItem> item = repository.findById(id);
        return ResponseEntity.ok(item.orElseThrow(() -> new NoSuchElementException("Item with id " + id + " does not exist")));
    }

    public ResponseEntity<GenericSuccessResponse<StoreItem>> updateItemById(long id, ItemDto newItem) {
        StoreItem item = repository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Item with id " + id + " does not exist")
        );
        item.setName(newItem.getItemName());
        item.setPrice(BigDecimal.valueOf(newItem.getPrice()));
        item.setInStock(newItem.getInStock());
        item = repository.save(item);

        var response = new GenericSuccessResponse<StoreItem>(
                "Item was updated successfully",
                item,
                LocalDateTime.now(),
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<String> deleteItemById(long id) {
        final StoreItem item;
        Optional<StoreItem> temp = repository.findById(id);
        if(temp.isEmpty())
            throw new NoSuchElementException("Item with id " + id + " does not exist");
        else {
            item = temp.get();
            for(Customer c: item.getCustomers())
                c.getWishList().remove(item);

            repository.delete(item);
            return ResponseEntity.ok("Item was deleted successfully");
        }
    }
}