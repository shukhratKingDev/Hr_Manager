package com.company.hrmanagement.repository;

import com.company.hrmanagement.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
@RepositoryRestResource(path = "company",collectionResourceRel = "listOfCompanies")
public interface CompanyRepository extends JpaRepository<Company, UUID> {
    Optional<Company>findByNameAndAddress(String name, String address);
}
