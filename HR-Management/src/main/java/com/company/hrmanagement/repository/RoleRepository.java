package com.company.hrmanagement.repository;

import com.company.hrmanagement.entity.Role;
import com.company.hrmanagement.entity.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository  extends JpaRepository <Role,Integer> {
    Optional<Role> findByRoles(RoleType roles);
}
