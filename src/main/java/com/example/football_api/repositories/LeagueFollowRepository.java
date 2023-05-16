package com.example.football_api.repositories;

import com.example.football_api.entities.LeagueFollow;
import com.example.football_api.entities.football.League;
import com.example.football_api.entities.users.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface LeagueFollowRepository extends JpaRepository<LeagueFollow, Long> {
    @Query("SELECT lf.league.id from LeagueFollow lf where lf.user.id = :userId")
    Set<Long> findByUserIdFollowedLeaguesId(Long userId);
    @Transactional
    void deleteByLeagueId(Long leagueId);
}
