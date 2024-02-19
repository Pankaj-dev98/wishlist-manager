package com.geekster.wishlistservice.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "ROLES")
@NoArgsConstructor
public class Role implements GrantedAuthority {
    public enum RoleType { CUSTOMER, ADMIN }
    public Role(RoleType role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role.toString();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private RoleType role;

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", role=" + role +
                '}';
    }
}
