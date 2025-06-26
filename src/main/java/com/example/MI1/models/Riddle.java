package com.example.MI1.models;

import jakarta.persistence.*;

@Entity
@Table(name = "riddle") 
public class Riddle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;
    private String answer;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false) // ✅ customer_id must not be NULL
    private Customer customer; // <-- This stores the full Customer

// Constructors, Getters & Setters
public Customer getCustomer() { return customer; }
public void setCustomer(Customer customer) { this.customer = customer; }


    public Riddle() {}

    public Riddle(String question, String answer, String addedBy) {
        this.question = question;
        this.answer = answer;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

}
