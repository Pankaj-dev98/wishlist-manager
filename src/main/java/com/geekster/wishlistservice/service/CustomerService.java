package com.geekster.wishlistservice.service;

import com.geekster.wishlistservice.entity.Address;
import com.geekster.wishlistservice.entity.Customer;
import com.geekster.wishlistservice.entity.StoreItem;
import com.geekster.wishlistservice.repository.CustomerRepository;
import com.geekster.wishlistservice.repository.ItemRepository;
import com.geekster.wishlistservice.utils.dto.GenericSuccessResponse;
import com.geekster.wishlistservice.utils.dto.RegisterRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * This class is responsible for managing CRUD operations on the wishlists in the database.
 * A customer is allowed to add and remove available items from their wishlist.
 */
@Service
@RequiredArgsConstructor
public class CustomerService {
    // It is already verified the email is not already part of the system before this method is called.
    private final CustomerRepository repository;
    private final ItemRepository itemRepository;

    public void registerNewCustomer(RegisterRequest request) {
        Customer customer = Customer.builder()
                .address(new Address(request.getAddressLine1(), request.getAddressLine2(), request.getPinCode()))
                .email(request.getEmail())
                .dateOfBirth(request.getDateOfBirth())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();
        repository.save(customer);
    }

    @Transactional
    public ResponseEntity<GenericSuccessResponse<StoreItem>> addItemToWishList(Long itemId) {
        final Customer customer;
        final StoreItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Item with id " + itemId + " does not exist"));

        String email = getCustomerEmail();

        customer = repository.findByEmail(email).orElseThrow();
        customer.getWishList().add(item);
        repository.save(customer);

        return ResponseEntity.ok(new GenericSuccessResponse<>(
                "Item was added successfully to wishlist.",
                item,
                LocalDateTime.now(),
                HttpStatus.OK.value()
        ));
    }

    private String getCustomerEmail() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null && (auth.getPrincipal() instanceof UserDetails userDetails)) {
            return userDetails.getUsername();
        }
        return null;
    }

    @Transactional
    public ResponseEntity<String> removeItemFromWishList(Long itemId) {
        final Collection<StoreItem> wishlist;
        wishlist = repository.findByEmail(
                getCustomerEmail()
        ).orElseThrow().getWishList();

        boolean wasRemoved = wishlist.remove(itemRepository.findById(itemId).orElseThrow(() -> new NoSuchElementException("Invalid item id")));
        return ResponseEntity.ok(wasRemoved?"Item was removed successfully": "User does not have the specified item in their wishlist");
    }

    @Transactional
    public ResponseEntity<List<StoreItem>> getWishList() {
        final String email = getCustomerEmail();
        Customer c = repository.findByEmail(email).orElseThrow();

        return ResponseEntity.ok(c.getWishList().stream().toList());
    }
}