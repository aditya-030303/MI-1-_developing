package com.example.MI1.controller;

import jakarta.servlet.http.HttpSession;
import com.example.MI1.models.Customer;
import com.example.MI1.service.CustomerService;
import com.example.MI1.service.LoginAttemptService;
import com.example.MI1.repository.CustomerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;
    private final LoginAttemptService loginAttemptService;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CustomerController(CustomerService customerService, LoginAttemptService loginAttemptService) {
        this.customerService = customerService;
        this.loginAttemptService = loginAttemptService;
    }

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        Object customerIdAttr = session.getAttribute("customerId");
        System.out.println("Customer ID from session:(profile controller) " + customerIdAttr); // Debugging
    
        if (customerIdAttr == null) {
            return "redirect:/login"; // No one is logged in
        }
    
        Long customerId;
        try {
            customerId = Long.parseLong(customerIdAttr.toString());
        } catch (NumberFormatException e) {
            return "redirect:/login"; // Invalid ID in session
        }
    
        Optional<Customer> optionalCustomer = customerService.findById(customerId);
    
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            model.addAttribute("user", customer); // Pass user to Thymeleaf
            return "profile"; // Renders profile.html
        } else {
            return "redirect:/login"; // ID is invalid or deleted
        }
    }

    @GetMapping("/register")
    public String showRegistrationForm(HttpSession session, Model model) {
        if (!Boolean.TRUE.equals(session.getAttribute("riddleSolved"))) {
            return "redirect:/riddles/riddle"; // Redirect if riddle not solved
        }
        model.addAttribute("customer", new Customer());
        return "register";
    }

    @PostMapping("/register")
    public String registerCustomer(@ModelAttribute Customer customer, HttpSession session, Model model) {
        if (!Boolean.TRUE.equals(session.getAttribute("riddleSolved"))) {
            return "redirect:/riddles/riddle";
        }
        try {
            Customer savedCustomer = customerService.registerCustomer(customer);
            if (savedCustomer == null || savedCustomer.getId() == null) {
                model.addAttribute("errorMessage", "Registration failed. Please try again.");
                return "register";
            }
            session.setAttribute("customerId", savedCustomer.getId());
            session.setAttribute("registered", true);
            return "redirect:/customers/login";
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println("Registrationnn error: " + e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/success")
    public String registrationSuccess(HttpSession session, Model model) {
        Long customerId = (Long) session.getAttribute("customerId");
        if (customerId == null) {
            return "redirect:/customers/login"; // Redirect to login if not logged in
        }
        // model.addAttribute("message", "Registration Successful!");
        return "success";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/riddles/riddle";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam(required = false) String email,
            @RequestParam(required = false) String answer,
            Model model, HttpSession session) {

        if (email != null && !email.isEmpty()) {
            System.out.println("email block executed");
            String sessionEmail = (String) session.getAttribute("email");
            System.out.println("Session email before null check: " + sessionEmail);
            System.out.println("Provided answer: " + answer);

            if (loginAttemptService.isBlocked(email)) {
                model.addAttribute("error", "Too many failed attempts. Try again later.");
                return "login";
            }

            Optional<Customer> customer = customerRepo.findByEmail(email);
            if (customer.isPresent()) {
                session.setAttribute("email", email);
                model.addAttribute("riddle", customer.get().getBio());
                return "login";
            } else {
                model.addAttribute("error", "Email not found.");
                return "login";
            }
        } else if (answer != null && !answer.isEmpty()) {
            System.out.println("Answer block executed");
            String sessionEmail = (String) session.getAttribute("email");
            System.out.println("Session email before null check: " + sessionEmail);
            System.out.println("Provided answer: " + answer);

            if (sessionEmail == null || sessionEmail.isEmpty()) {
                System.out.println("Session email is null or empty");
                model.addAttribute("error", "Session expired or invalid. Please log in again.");
                return "login";
            }

            Optional<Customer> customer = customerRepo.findByEmail(sessionEmail);
            if (customer.isPresent()) {
                System.out.println("Stored hashed password: " + customer.get().getPassword());
                if (passwordEncoder.matches(answer, customer.get().getPassword())) {
                    System.out.println("Answer matches the stored password");
                    loginAttemptService.resetAttempts(sessionEmail);
                    session.setAttribute("customerId", customer.get().getId());
                    return "redirect:/customers/success";
                } else {
                    System.out.println("Answer does not match the stored password");
                    loginAttemptService.registerFailedAttempt(sessionEmail);
                    model.addAttribute("error", "Incorrect answer. Please try again.");
                    return "login";
                }
            } else {
                System.out.println("Customer not found for session email: " + sessionEmail);
                model.addAttribute("error", "Customer not found. Please log in again.");
                return "login";
            }
        }

        model.addAttribute("error", "Invalid request. Please provide valid credentials.");
        return "login";
    }

   
    
}
