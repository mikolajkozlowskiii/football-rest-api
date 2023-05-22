package com.example.football_api.services.football;

import com.example.football_api.entities.football.LeagueTableView;

import java.util.List;

public interface LeagueTableViewService {
    List<LeagueTableView> getLeagueTableViewByLeagueId(Long leagueId);
}
