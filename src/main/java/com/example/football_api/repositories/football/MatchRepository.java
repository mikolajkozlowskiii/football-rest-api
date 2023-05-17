package com.example.football_api.repositories.football;

import com.example.football_api.entities.football.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    @Override
    Optional<Match> findById(Long matchId);
    List<Match> findByLeagueIdAndHomeTeamIdOrAwayTeamId(Long leagueId, Long homeTeamId, Long awayTeamId);
    @Query("SELECT m from Match m WHERE m.league.id = :leagueId and (m.awayTeam.id =: teamId or m.homeTeam.id =:teamId)")
    List<Match> findByLeagueAndTeam(@Param("leagueId") Long leagueId, Long teamId);
}
