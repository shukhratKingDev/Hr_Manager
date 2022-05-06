package com.company.hrmanagement.repository;

import com.company.hrmanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
Optional<User> findByCompany_NameAndCompany_Address(String company_name, String company_address);
 Optional<User> findByEmail(String email);
  Optional<User> findByEmailAndEmailCode(String email, String emailCode);
}
