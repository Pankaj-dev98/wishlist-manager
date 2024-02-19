package com.geekster.wishlistservice.repository;

import com.geekster.wishlistservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRole(Role.RoleType role);
}
