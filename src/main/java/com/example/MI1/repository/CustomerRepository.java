package com.example.MI1.repository;

import com.example.MI1.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    // Custom query to find a customer by email
    @Query("SELECT c FROM Customer c WHERE c.email = :email")
    Optional<Customer> findByEmail(@Param("email") String email);
    
    // No need to redeclare findById(Long id) — it's inherited from JpaRepository

    // Method to find all customers by email
    List<Customer> findAllByEmail(String email);
}
