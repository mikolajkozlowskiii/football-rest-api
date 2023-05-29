package com.example.football_api.repositories.football;

import com.example.football_api.entities.football.Goal;
import com.example.football_api.entities.football.Match;
import com.example.football_api.entities.football.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    Optional<Goal> findById(Long id);
    @Query("SELECT g from Goal g " +
            "left join fetch g.player p " +
            "left join fetch g.match m " +
            "WHERE p = :player and " +
            "m.date between :starts and :ends")
    List<Goal> findByPlayerAndDatesRange(@Param("player") Player player,
                                         @Param("starts") LocalDate starts,
                                         @Param("ends") LocalDate ends);
    List<Goal> findByMatch(Match match);
    Page<Goal> findByPlayer(Player player, Pageable pageable);
    Page<Goal> findAll(Pageable pageable);
}
