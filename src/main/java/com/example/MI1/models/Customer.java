package com.example.MI1.models;

import jakarta.persistence.*;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String email;
    private String password; //means the riddle answer of the customer
    private String bio; //means the riddle of a perticular customer as his login credential
    private boolean riddleSolved = false; // Default: False, they must solve a riddle first

    // Constructors
    public Customer() {}

    public Customer(String customerName, String email, String password, String bio) {
        this.customerName = customerName;
        this.email = email;
        this.password = password;
        this.bio = bio;
        this.riddleSolved = false; // Ensure they start as "not solved"
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCustomerName() { 
        System.out.println("Customer Name: " + customerName); // Debugging
        return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public boolean isRiddleSolved() { return riddleSolved; }
    public void setRiddleSolved(boolean riddleSolved) { this.riddleSolved = riddleSolved; }
}
