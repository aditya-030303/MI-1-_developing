package com.example.MI1.service;

import com.example.MI1.models.Riddle;
import com.example.MI1.repository.RiddleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
// import java.util.Optional;
import java.util.Random;

@Service
public class RiddleService {
    private RiddleRepository riddleRepository;
    private final Random random = new Random(); // For selecting random riddles

    public RiddleService(RiddleRepository riddleRepository) {
       this.riddleRepository = riddleRepository;
    }

    // ✅ Save a New Riddle
    public void saveRiddle(Riddle riddle) {
        riddleRepository.save(riddle);
    }

    // ✅ Fetch All Riddles
    public List<Riddle> getAllRiddles() {
        List<Riddle> riddles = riddleRepository.findAll();
        System.out.println("Fetching all riddles..."); // Debugging
        if (riddles.isEmpty()) {
            // If no riddles are found, return an empty list
            System.out.println("No riddles found in the repository."); // Debugging
            return new ArrayList<>();
        }
        System.out.println("Fetched riddles: " + riddles); // Debugging
        return riddles;
    }

    // ✅ Fetch a Riddle by ID
    public Riddle getRiddleById(Long id) {
        return riddleRepository.findById(id).orElse(null);
    }

    // ✅ Fetch a Random Riddle
    public Riddle getRandomRiddle() {
        List<Riddle> riddles = riddleRepository.findAll();
        if (riddles.isEmpty()) {
            return null; // No riddles available
        }
        return riddles.get(random.nextInt(riddles.size()));
    }
}