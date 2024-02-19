package com.geekster.wishlistservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "STORE_ITEM")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class StoreItem {
    public StoreItem(String name, double price, boolean inStock) {
        this(null, name, new BigDecimal(price), inStock, new HashSet<>());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Convert(converter = org.hibernate.type.YesNoConverter.class)
    @Column(name = "IN_STOCK")
    private boolean inStock;

    @JsonIgnore
    @ManyToMany(mappedBy = "wishList")
    private Set<Customer> customers;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        StoreItem storeItem = (StoreItem) o;

        return getId().equals(storeItem.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}