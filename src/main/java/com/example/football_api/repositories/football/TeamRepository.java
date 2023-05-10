package com.example.football_api.repositories.football;

import com.example.football_api.entities.football.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    @Override
    Optional<Team> findById(Long aLong);
    Optional<Team> findByName(String name);
    Boolean existsByName(String name);
}
