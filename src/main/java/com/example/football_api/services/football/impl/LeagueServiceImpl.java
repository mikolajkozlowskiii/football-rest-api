package com.example.football_api.services.football.impl;

import com.example.football_api.dto.football.validation.LeagueRequest;
import com.example.football_api.dto.football.validation.TeamRequest;
import com.example.football_api.dto.football.response.LeagueResponse;
import com.example.football_api.entities.football.League;
import com.example.football_api.entities.football.Team;
import com.example.football_api.exceptions.football.DuplicateLeagueException;
import com.example.football_api.exceptions.football.LeagueNotFoundException;
import com.example.football_api.repositories.football.LeagueRepository;
import com.example.football_api.services.football.LeagueService;
import com.example.football_api.services.football.TeamService;
import com.example.football_api.services.football.mappers.LeagueMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeagueServiceImpl implements LeagueService {
    private final LeagueRepository leagueRepository;
    private final LeagueMapper leagueMapper;
    private final TeamService teamService;
    @Override
    public LeagueResponse saveNewLeague(LeagueRequest leagueRequest) {
        Set<Team> teamsEntities = getTeamsEntities(leagueRequest);
        League league = leagueMapper.map(leagueRequest);
        league.setTeams(teamsEntities);
        saveNewLeague(league);
        return leagueMapper.map(league);
    }

    @Override
    public LeagueResponse setOfficiallLeagueStatus(Long id){
        League league = findLeagueById(id);
        save(league);
        return leagueMapper.map(league);
    }

    private Set<Team> getTeamsEntities(LeagueRequest leagueRequest) {
        Set<TeamRequest> teamsRequest = leagueRequest.getTeams();
        System.out.println(teamsRequest);
        return Optional.ofNullable(teamsRequest).orElse(Collections.emptySet()).stream().map(teamService::getTeamEntity).collect(Collectors.toSet());
    }

    private League saveNewLeague(League league) {
        boolean isLeagueExists = leagueRepository.existsLeagueByNameAndCountryAndSeason(
                league.getName(), league.getCountry(), league.getSeason()
        );
        if(isLeagueExists){
            throw new DuplicateLeagueException(league);
        }
        return save(league);
    }

    private League save(League league){
        return leagueRepository.save(league);
    }

    private League saveLeagueChanges(League league){
        return save(league);
    }

    @Override
    public LeagueResponse update(Long leagueId, LeagueRequest updateLeagueInfo) {
        League leagueToBeUpdated = findLeagueById(leagueId);
        League updatedLeague = leagueMapper.update(leagueToBeUpdated, updateLeagueInfo);
        saveLeagueChanges(updatedLeague);
        return leagueMapper.map(updatedLeague);
    }

    @Override
    public LeagueResponse delete(Long id) {
        League league = findLeagueById(id);
        deleteLeague(league);
        return leagueMapper.map(league);
    }

    private void deleteLeague(League league) {
        leagueRepository.delete(league);
    }

    @Override
    public LeagueResponse findResponseById(Long id) {
        League league = findLeagueById(id);
        return leagueMapper.map(league);
    }

    @Override
    public List<LeagueResponse> searchLeaguesByNameSeasonCountryOfficial
            (String name, String season, String country, boolean isOfficial) {
        List<LeagueResponse> leagueResponses = searchLeaguesByNameSeasonAndCountry(name, season, country);

        return (isOfficial)?
                leagueResponses
                        .stream()
                        .filter(LeagueResponse::isOfficial)
                        .toList():
                leagueResponses
                        .stream()
                        .filter(s->!s.isOfficial())
                        .toList();
    }

    private List<LeagueResponse> searchLeaguesByNameSeasonAndCountry(String name, String season, String country) {
        List<LeagueResponse> leagueResponses;
        if (name != null && season != null && country != null) {
            leagueResponses = List.of(findByNameAndSeasonAndCountry(name, season, country));
        } else if (name != null && season != null) {
            leagueResponses = findByNameAndSeason(name, season);
        } else if (name != null && country != null) {
            leagueResponses = findByNameAndCountry(name, country);
        } else if (season != null && country != null) {
            leagueResponses = findBySeasonAndCountry(season, country);
        } else if (name != null) {
            leagueResponses = findByName(name);
        } else if (season != null) {
            leagueResponses = findBySeason(season);
        } else if (country != null) {
            leagueResponses = findByCountry(country);
        } else {
            leagueResponses = findAll();
        }
        return leagueResponses;
    }

    private List<LeagueResponse> findAll() {
        return leagueRepository.findAll().stream().map(s -> leagueMapper.map(s)).toList();
    }

    private LeagueResponse findByNameAndSeasonAndCountry(String name, String season, String country) {
        return leagueMapper.map(searchLeagues(name, season, country));
    }


    private List<LeagueResponse> findBySeasonAndCountry(String season, String country) {
        return leagueRepository
                .findByCountryAndSeason(country, season)
                .stream()
                .map(leagueMapper::map)
                .toList();
    }

    private List<LeagueResponse> findByNameAndCountry(String name, String country) {
        return leagueRepository
                .findByNameAndCountry(name, country)
                .stream()
                .map(leagueMapper::map)
                .toList();
    }

    private List<LeagueResponse> findByNameAndSeason(String name, String season) {
        return leagueRepository
                .findByNameAndSeason(name, season)
                .stream()
                .map(leagueMapper::map)
                .toList();
    }

    private League searchLeagues(String name, String season, String country) {
        return leagueRepository
                .findByNameAndCountryAndSeason(name, country, season)
                .orElseThrow(() -> new LeagueNotFoundException(name));
    }

    @Override
    public List<LeagueResponse> findByName(String name) {
        return findLeagueByName(name)
                .stream()
                .map(leagueMapper::map)
                .toList();
    }

    @Override
    public List<LeagueResponse> findByCountry(String country) {
        return findLeagueByCountry(country)
                .stream()
                .map(leagueMapper::map)
                .toList();
    }

    @Override
    public List<LeagueResponse> findBySeason(String season) {
        return findLeagueBySeason(season)
                .stream()
                .map(leagueMapper::map)
                .toList();
    }

    @Override
    public LeagueResponse addTeamsToLeague(Long leagueId, List<TeamRequest> teamRequests) {
        League league = findLeagueById(leagueId);
        Set<Team> teams = teamRequests.stream().map(teamService::getTeamEntity).collect(Collectors.toSet());
        teams.forEach(s->league.getTeams().add(s));
        saveLeagueChanges(league);
        return leagueMapper.map(league);
    }

    @Override
    public LeagueResponse addTeamsByIdsToLeague(Long leagueId, Set<Long> ids) {
        League league = findLeagueById(leagueId);
        Set<Team> teams = ids
                .stream()
                .map(teamService::findTeamById)
                .collect(Collectors.toSet());
        league.getTeams().addAll(teams);
        saveLeagueChanges(league);
        return leagueMapper.map(league);
    }

    @Override
    public LeagueResponse removeTeamsFromLeague(Long leagueId, List<TeamRequest> teamRequests) {
        League league = findLeagueById(leagueId);
        Set<Team> teams = teamRequests.stream().map(teamService::getTeamEntity).collect(Collectors.toSet());
        teams.forEach(s->league.getTeams().remove(s));
        saveLeagueChanges(league);
        return leagueMapper.map(league);
    }

    @Override
    public LeagueResponse removeTeamsByIdsFromLeague(Long leagueId, Set<Long> ids) {
        League league = findLeagueById(leagueId);
        Set<Team> teams = ids
                .stream()
                .map(teamService::findTeamById)
                .collect(Collectors.toSet());
        league.getTeams().removeAll(teams);
        saveLeagueChanges(league);
        return leagueMapper.map(league);
    }

    public League findLeagueById(Long id) {
        return leagueRepository
                .findById(id)
                .orElseThrow(() -> new LeagueNotFoundException(id));
    }

    private List<League> findLeagueByName(String name) {
        return leagueRepository.findByName(name);
    }

    private List<League> findLeagueByCountry(String country) {
        return leagueRepository.findByCountry(country);
    }

    private List<League> findLeagueBySeason(String season) {
        return leagueRepository.findBySeason(season);
    }
}
