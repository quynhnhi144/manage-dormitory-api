package com.managedormitory.repositories;

import com.managedormitory.models.dao.Role;
import com.managedormitory.models.dao.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleName roleName);
}
