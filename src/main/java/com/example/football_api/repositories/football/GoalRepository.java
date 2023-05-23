package com.example.football_api.repositories.football;

import com.example.football_api.entities.football.Goal;
import com.example.football_api.entities.football.Match;
import com.example.football_api.entities.football.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    Optional<Goal> findById(Long id);
    List<Goal> findByMatch(Match match);
    Page<Goal> findByPlayer(Player player, Pageable pageable);
    Page<Goal> findAll(Pageable pageable);
}
