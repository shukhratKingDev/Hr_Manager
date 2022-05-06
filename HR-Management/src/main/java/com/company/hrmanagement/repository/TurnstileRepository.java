package com.company.hrmanagement.repository;

import com.company.hrmanagement.entity.Turnstile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RepositoryRestResource(path = "turnstile",collectionResourceRel = "list of turnstiles")
public interface TurnstileRepository extends JpaRepository<Turnstile, UUID> {
    List<Turnstile> findAllByUserId(UUID user_id);
    Optional<Turnstile> findByUser_Id(UUID user_id);
}
