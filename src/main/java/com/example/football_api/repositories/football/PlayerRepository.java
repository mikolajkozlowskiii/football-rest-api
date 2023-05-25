package com.example.football_api.repositories.football;

import com.example.football_api.entities.football.Player;
import com.example.football_api.entities.football.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findById(Long id);

    @Query("SELECT DISTINCT p FROM Player p " +
            "LEFT JOIN FETCH p.teams th " +
            "LEFT JOIN FETCH th.team t " +
            "WHERE t = :team AND CURRENT_DATE() BETWEEN th.start AND th.ends")
    List<Player> findAllCurrentPlayersInTeam(Team team);

    @Query("SELECT DISTINCT p FROM Player p " +
            "LEFT JOIN FETCH p.teams th " +
            "LEFT JOIN FETCH th.team t " +
            "WHERE t = :team AND :date BETWEEN th.start AND th.ends")
    List<Player> findAllPlayersInTeamByDate(Team team, LocalDate date);
}
