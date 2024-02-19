package com.geekster.wishlistservice.repository;

import com.geekster.wishlistservice.entity.StoreItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<StoreItem, Long> {
}
