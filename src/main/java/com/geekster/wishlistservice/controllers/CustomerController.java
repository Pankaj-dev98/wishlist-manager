package com.geekster.wishlistservice.controllers;

import com.geekster.wishlistservice.entity.StoreItem;
import com.geekster.wishlistservice.service.CustomerService;
import com.geekster.wishlistservice.utils.dto.GenericSuccessResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Users ie. customers of the application can manipulate their respective wishlists using endpoints declared in this REST controller.
 */

@RestController
@RequestMapping("/api/customers")
@AllArgsConstructor
public class CustomerController {
    private CustomerService customerService;

    @PostMapping("/wishlist-items/{itemId}")
    public ResponseEntity<GenericSuccessResponse<StoreItem>> addItemToWishList(@PathVariable Long itemId) {
        return customerService.addItemToWishList(itemId);
    }

    @DeleteMapping("/wishlist-items/{itemId}")
    public ResponseEntity<String> removeItemFromWishList(@PathVariable Long itemId) {
        return customerService.removeItemFromWishList(itemId);
    }

    @GetMapping("/wishlist-items")
    public ResponseEntity<List<StoreItem>> getWishList() {
        return customerService.getWishList();
    }
}