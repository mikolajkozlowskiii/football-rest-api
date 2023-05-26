package com.example.football_api.repositories.football;


import com.example.football_api.entities.football.Player;
import com.example.football_api.entities.football.PlayerTeamHistory;
import com.example.football_api.entities.football.Team;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PlayerHistoryRepository extends JpaRepository<PlayerTeamHistory, Long> {
    Optional<PlayerTeamHistory> findById(Long id);
    Set<PlayerTeamHistory> findByPlayerId(Long playerId);
    List<PlayerTeamHistory> findAllByTeam(Team team);
    @Query("SELECT pth from PlayerTeamHistory pth" +
            " left join fetch pth.team" +
            " left join fetch pth.player" +
            " where pth.player =:player" +
            " AND pth.start <= :date AND (pth.ends IS NULL OR pth.ends >= :date)")
    List<PlayerTeamHistory> findByPlayerAndDateIsInRange(Player player, LocalDate date); // TODO change List to Optional

    @Query("SELECT CASE WHEN COUNT(pth) > 0 THEN false ELSE true END " +
            "FROM PlayerTeamHistory pth " +
            "WHERE pth.player.id = :playerId " +
            "AND (:starts BETWEEN pth.start AND pth.ends OR :ends BETWEEN pth.start AND pth.ends OR " +
            "(:starts < pth.start and :ends > pth.ends))")
    boolean isDateRangeAvailable(@NotNull @Param("playerId") Long playerId,
                                 @Param("starts") LocalDate starts,
                                 @Param("ends") LocalDate ends);


    /*
    @Query("SELECT CASE WHEN COUNT(pth) > 0 THEN false ELSE true END" +
            " FROM PlayerTeamHistory pth " +
            "LEFT JOIN FETCH pth.team t " +
            "LEFT JOIN FETCH pth.player p " +
            "WHERE pth.team = :team AND (:date BETWEEN pth.start AND pth.ends) AND pth.player = :player")
    boolean isPlayerExistsInTeamInCurrentDate( @Param("team") Team team,
                                               @Param("date") LocalDate date,
                                               @Param("player") Player player);*/
}
