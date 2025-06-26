package com.example.MI1.service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
 // Importing BCryptPasswordEncoder


@Service
public class LoginAttemptService {
    private final int MAX_ATTEMPTS = 5;
    private final Map<String, Integer> attemptsCache = new ConcurrentHashMap<>();

    public void registerFailedAttempt(String email) {
        attemptsCache.put(email, attemptsCache.getOrDefault(email, 0) + 1);
    }

    public void resetAttempts(String email) {
        attemptsCache.remove(email);
    }

    public boolean isBlocked(String email) {
        return attemptsCache.getOrDefault(email, 0) >= MAX_ATTEMPTS;
    }
}
