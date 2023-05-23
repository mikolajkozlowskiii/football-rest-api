package com.example.football_api.repositories.football;

import com.example.football_api.entities.football.Player;
import com.example.football_api.entities.football.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findById(Long id);
    List<Player> findByTeam(Team team);
}
