package com.example.MI1.controller;

import com.example.MI1.models.Customer;
import com.example.MI1.models.Riddle;
import com.example.MI1.service.CustomerService;
import com.example.MI1.service.RiddleService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@SessionAttributes({ "score", "riddleIndex" })
public class RiddleController {

    @Autowired
    private RiddleService RiddleService;

    private List<Riddle> allRiddles;

    @ModelAttribute("score")
    public Integer score() {
        return 0;
    }

    @ModelAttribute("riddleIndex")
    public Integer riddleIndex() {
        return 0;
    }

    @GetMapping("/")
    public String showProjectPage() {
        return "showcase"; // This maps to templates/showcase.html
    }

    



    @GetMapping("/riddles/restart")
public String restartGame(RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("riddleIndex", 0);
    redirectAttributes.addFlashAttribute("score",0) ; // Start from the first riddle
    return "redirect:/riddles/play";
}


    @GetMapping("/riddles/play")
    public String play(HttpSession session ,Model model, @ModelAttribute("riddleIndex") Integer riddleIndex) {
        Long customerId = (Long) session.getAttribute("customerId");

        if (customerId == null) {
            System.out.println("Customer ID in session: " + customerId);
            System.err.println("No customer ID found in session. Redirecting to login."); // Debugging
            return "redirect:/customers/login"; // 🔴 Redirect if no user session
        }
        if (allRiddles == null) {
            allRiddles = RiddleService.getAllRiddles();
        }
System.out.println("Riddle Index: " + riddleIndex); // Debugging
        if (riddleIndex >= allRiddles.size()) {
            model.addAttribute("riddle", null);
        } else {
            model.addAttribute("riddle", allRiddles.get(riddleIndex));
        }

        return "riddle-play";
    }

    @PostMapping("/riddles/check-answer")
    public String checkAnswer(
            @RequestParam Long riddleId,
            @RequestParam String userAnswer,
            Model model,
            @ModelAttribute("score") Integer score,
            @ModelAttribute("riddleIndex") Integer riddleIndex) {
    
        Riddle current = allRiddles.get(riddleIndex);
    
        if (current.getId().equals(riddleId)) {
            String correct = current.getAnswer().trim().toLowerCase();
            String given = userAnswer.trim().toLowerCase();
    
            if (correct.equals(given)) {
                model.addAttribute("feedback", "✅ Correct!");
                score += 10;
            } else {
                model.addAttribute("feedback", "❌ Incorrect! Correct answer was: " + current.getAnswer());
            }
        }
    
        // Don't increment index yet — wait for "Next" button
        model.addAttribute("riddle", current);
        model.addAttribute("score", score);
        model.addAttribute("riddleIndex", riddleIndex);
    
        return "riddle-play"; // show feedback on the same page
    }
    
    @PostMapping("/riddles/skip")
    public String skipRiddle(
            @RequestParam Integer riddleIndex,
            @RequestParam Integer score,
            Model model) {
    
        Riddle current = allRiddles.get(riddleIndex);
        model.addAttribute("riddle", current);
        model.addAttribute("feedback", "⏭️ You skipped the riddle.");
        model.addAttribute("score", score);
        model.addAttribute("riddleIndex", riddleIndex); // Don't increment yet
    
        return "riddle-play"; // Show feedback, wait for "Next"
    }
    

    @PostMapping("/riddles/next")
public String nextRiddle(
        @RequestParam Integer riddleIndex,
        @RequestParam Integer score,
        RedirectAttributes redirectAttributes) {

    riddleIndex++; // Now move to the next riddle

    redirectAttributes.addFlashAttribute("score", score);
    redirectAttributes.addFlashAttribute("riddleIndex", riddleIndex);

    return "redirect:/riddles/play";
}


    private final RiddleService riddleService;
    private final CustomerService customerService; // Inject CustomerService

    public RiddleController(RiddleService riddleService, CustomerService customerService) {
        this.riddleService = riddleService;
        this.customerService = customerService;
    }

    // ✅ Show Add Riddle Page
    @GetMapping("/riddles/add")
    public String showAddRiddlePage(HttpSession session, Model model) {
        Long customerId = (Long) session.getAttribute("customerId");

        if (customerId == null) {
            System.out.println("Customer ID in session: " + customerId);
            System.err.println("No customer ID found in session. Redirecting to login."); // Debugging
            return "redirect:/customers/login"; // 🔴 Redirect if no user session
        }
        System.out.println("Customer ID in session: " + customerId);
        model.addAttribute("riddle", new Riddle());
        return "add-riddle"; // ✅ Show riddle form
    }

    // ✅ Handle Riddle Submission
    @PostMapping("/riddles/save")
    public String saveRiddle(@ModelAttribute Riddle riddle, HttpSession session) {
        Long customerId = (Long) session.getAttribute("customerId");
        System.out.println("Customer ID from session: " + customerId); // Debugging

        if (customerId == null || customerId == 0) {
            return "redirect:/customers/login"; // Redirect if not logged in
        }

        Customer customer = customerService.getCustomerById(customerId).orElse(null);
        if (customer == null) {
            System.err.println("Customer not found for ID: " + customerId); // Debugging
            return "redirect:/customers/register"; // Redirect if customer not found
        }
        System.out.println("Customer found: " + customer.getCustomerName()); // Debugging
        riddle.setCustomer(customer);
        riddleService.saveRiddle(riddle);
        System.out.println("Riddle saved successfully. Redirecting to /riddles/list"); // Debugging

        return "redirect:/riddles/list"; // Ensure this matches the @GetMapping("/list")
    }

    // ✅ Show List of All Riddles
    @GetMapping("/riddles/list")
    public String listRiddles(HttpSession session, Model model) {
        Long customerId = (Long) session.getAttribute("customerId");

        if (customerId == null) {
            System.out.println("Customer ID in session: " + customerId);
            System.err.println("No customer ID found in session. Redirecting to login."); // Debugging
            return "redirect:/customers/login"; // 🔴 Redirect if no user session
        }
        List<Riddle> riddles = riddleService.getAllRiddles();
        model.addAttribute("riddles", riddles);
        return "riddle-list";
    }

    // ✅ Show Riddle Challenge Page
    @GetMapping("/riddles/riddle")
    public String showRiddlePage() {
        return "riddle";
    }

    @GetMapping("/riddles/random")
    public ResponseEntity<Riddle> getRandomRiddle() {
        Riddle randomRiddle = riddleService.getRandomRiddle();
        return ResponseEntity.ok(randomRiddle);
    }

    // ✅ Check Answer and Grant Access
    @PostMapping("/riddles/check-riddle")
    public String checkRiddle(@RequestParam String answer, @RequestParam Long riddleId, HttpSession session,
            Model model) {
        System.out.println("User Answer: " + answer);

        // Fetch the riddle by ID
        Riddle riddle = riddleService.getRiddleById(riddleId);
        if (riddle == null) {
            System.out.println("❌ Riddle not found!");
            model.addAttribute("error", "Riddle not found! Please try again.");
            return "riddle";
        }

        // Compare the user's answer with the correct answer
        if (answer.equalsIgnoreCase(riddle.getAnswer())) {
            session.setAttribute("riddleSolved", true);
            System.out.println("✅ Correct Answer! Redirecting to /showcase");
            return "redirect:/";
        } else {
            System.out.println("❌ Wrong Answer! Stay on riddle page");
            model.addAttribute("error", "Wrong answer! Try again.");
            return "riddle";
        }
    }
}
