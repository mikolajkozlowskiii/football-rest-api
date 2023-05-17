package com.example.football_api.services.football.impl;

import com.example.football_api.dto.football.validation.TeamRequest;
import com.example.football_api.dto.football.response.LeagueResponse;
import com.example.football_api.dto.football.response.TeamResponse;
import com.example.football_api.entities.football.Team;
import com.example.football_api.exceptions.football.TeamNotFoundException;
import com.example.football_api.repositories.football.TeamRepository;
import com.example.football_api.services.football.TeamService;
import com.example.football_api.services.football.mappers.LeagueMapper;
import com.example.football_api.services.football.mappers.TeamMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    private final LeagueMapper leagueMapper;
    @Override
    public Team save(Team team) {
        return teamRepository.save(team);
    }

    @Override
    public TeamResponse save(TeamRequest teamRequest) {
        Team team = teamMapper.map(teamRequest);
        save(team);
        return teamMapper.map(team);
    }

    @Override
    public TeamResponse findByName(String name) {
        return teamMapper.map(findTeamByName(name));
    }

    public Team findTeamByName(String name) {
        return teamRepository
                .findByName(name)
                .orElseThrow(()->new TeamNotFoundException(name));
    }

    @Override
    public TeamResponse findById(Long id) {
        return teamMapper.map(findTeamById(id));
    }

    public Team findTeamById(Long id) {
        return teamRepository
                .findById(id)
                .orElseThrow(()->new TeamNotFoundException(id));
    }

    @Override
    public Team getTeamEntity(TeamRequest teamRequest) {
        String teamName = teamRequest.getName();
        try{
            return getExistingTeamEntity(teamName);
        }catch (TeamNotFoundException ex){
            return getNewTeamEntity(teamRequest);
        }
    }

    private Team getNewTeamEntity(TeamRequest teamRequest) {
        return teamMapper.map(teamRequest);
    }

    private Team getExistingTeamEntity(String teamName) {
        return findTeamByName(teamName);
    }

    @Override
    public TeamResponse updateTeam(TeamRequest team) {
        return null;
    }

    @Override
    public TeamResponse deleteTeamById(Long id) {
        Team team = findTeamById(id);
        delete(team);
        return teamMapper.map(team);
    }

    public void delete(Team team){
        teamRepository.delete(team);
    }

    @Override
    public List<LeagueResponse> getAllTeamLeagues(Long teamId) {
        Team team = findTeamById(teamId);
        System.out.println(team.getLeagues().size());
        return team
                .getLeagues()
                .stream()
                .map(leagueMapper::map)
                .toList();
    }
}
