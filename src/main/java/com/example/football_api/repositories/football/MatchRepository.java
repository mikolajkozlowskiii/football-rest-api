package com.example.football_api.repositories.football;

import com.example.football_api.entities.football.Match;
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
public interface MatchRepository extends JpaRepository<Match, Long> {
    @Override
    Optional<Match> findById(Long matchId); // !

    List<Match> findByLeagueId(Long leagueId); // !
    List<Match> findByLeagueId(Long leagueId, Pageable pageable);

    @Query("SELECT m from Match m WHERE m.league.id = :leagueId and (m.awayTeam.id =: teamId or m.homeTeam.id =:teamId)")
    List<Match> findByLeagueIdAndTeamId(@Param("leagueId") Long leagueId, Long teamId);

    @Query("SELECT m from Match m WHERE m.league.id = :leagueId and (m.awayTeam.id =: teamId or m.homeTeam.id =:teamId)")
    Page<Match> findByLeagueIdAndTeamId(@Param("leagueId") Long leagueId, Long teamId, Pageable pageable);

    @Query("SELECT m FROM Match m WHERE m.date = CURRENT_DATE")
    List<Match> findAllByDateToday();

    @Query("SELECT m FROM Match m WHERE m.date = CURRENT_DATE")
    List<Match> findAllByDateToday(Pageable pageable);

    @Query("SELECT m FROM Match m WHERE m.date= :date")
    List<Match> findAllByDate(LocalDate date);
    Page<Match> findAll(Pageable pageable);
}
