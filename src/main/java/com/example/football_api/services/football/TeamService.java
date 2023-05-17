package com.example.football_api.services.football;

import com.example.football_api.dto.football.validation.TeamRequest;
import com.example.football_api.dto.football.response.LeagueResponse;
import com.example.football_api.dto.football.response.TeamResponse;
import com.example.football_api.entities.football.Team;

import java.util.List;

public interface TeamService {
    Team save(Team team);
    TeamResponse save(TeamRequest team);
    TeamResponse findByName(String name);
    TeamResponse findById(Long id);
    Team findTeamById(Long id);
    Team getTeamEntity(TeamRequest teamRequest);
    TeamResponse updateTeam(TeamRequest team);
    TeamResponse deleteTeamById(Long id);
    List<LeagueResponse> getAllTeamLeagues(Long teamId);
}
