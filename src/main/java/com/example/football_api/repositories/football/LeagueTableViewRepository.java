package com.example.football_api.repositories.football;

import com.example.football_api.entities.football.LeagueTableView;
import com.example.football_api.repositories.ReadOnlyRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeagueTableViewRepository extends ReadOnlyRepository<LeagueTableView, Long> {
    List<LeagueTableView> findByLeagueId(Long leagueId);
}
