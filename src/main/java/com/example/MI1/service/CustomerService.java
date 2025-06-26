package com.example.MI1.service;

import com.example.MI1.models.Customer;
import com.example.MI1.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Importing BCryptPasswordEncoder

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(); // Initialize password encoder
    }

    public Customer registerCustomer(Customer customer) {
        // Check if email already exists
        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already registered!");
        }
        
        // Hash the password before storing
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        
        // Ensure riddleSolved is properly set
        customer.setRiddleSolved(true);
        
        return customerRepository.save(customer); // Return saved customer
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public Optional<Customer> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }
}
