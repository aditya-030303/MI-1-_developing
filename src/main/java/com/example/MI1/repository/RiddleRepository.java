//riddleRepository
package com.example.MI1.repository;

import com.example.MI1.models.Riddle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RiddleRepository extends JpaRepository<Riddle, Long> {
}
