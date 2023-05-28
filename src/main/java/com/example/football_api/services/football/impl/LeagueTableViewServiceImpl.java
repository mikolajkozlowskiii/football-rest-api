package com.example.football_api.services.football.impl;

import com.example.football_api.entities.football.LeagueTableView;
import com.example.football_api.repositories.football.LeagueTableViewRepository;
import com.example.football_api.services.football.LeagueService;
import com.example.football_api.services.football.LeagueTableViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeagueTableViewServiceImpl implements LeagueTableViewService {
    private final LeagueTableViewRepository leagueTableViewRepository;
    private final LeagueService leagueService;
    @Override
    public List<LeagueTableView> getLeagueTableViewByLeagueId(Long leagueId) {
        leagueService.findLeagueById(leagueId);
        System.out.println(leagueTableViewRepository.findByLeagueId(leagueId).size());
        return leagueTableViewRepository.findByLeagueId(leagueId);
    }
}
