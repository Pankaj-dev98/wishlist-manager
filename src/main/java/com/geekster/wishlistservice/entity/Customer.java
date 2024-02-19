package com.geekster.wishlistservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "CUSTOMER")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Access(AccessType.PROPERTY)
    @Email
    @Column(name = "EMAIL", unique = true)
    private String email;
    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    @Column(name = "FIRST_NAME", length = 128)
    private String firstName;

    @Column(name = "LAST_NAME", length = 128)
    private String lastName;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "addressLine1", column = @Column(name = "ADDRESS_LINE_1", length = 128)),
            @AttributeOverride(name = "addressLine2", column = @Column(name = "ADDRESS_LINE_2", length = 128)),
            @AttributeOverride(name = "pinCode",      column = @Column(name = "ADDRESS_PINCODE" ))
        }
    )
    private Address address;

    private LocalDate dateOfBirth;

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "CUSTOMER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ITEM_ID")
    )
    private Set<StoreItem> wishList;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Customer customer = (Customer) o;
        return id.equals(customer.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}