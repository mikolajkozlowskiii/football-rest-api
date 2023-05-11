package com.example.football_api.services.football;

import com.example.football_api.dto.football.request.LeagueRequest;
import com.example.football_api.dto.football.request.TeamRequest;
import com.example.football_api.dto.football.response.LeagueResponse;

import java.util.List;
import java.util.Set;

public interface LeagueService {
    LeagueResponse saveNewLeague(LeagueRequest league);
    LeagueResponse update(Long leagueId, LeagueRequest league);
    LeagueResponse delete(Long id);
    LeagueResponse findById(Long id);
    List<LeagueResponse> searchLeaguesByNameSeasonCountry(String name, String season, String country);
    List<LeagueResponse> findByName(String name);
    List<LeagueResponse> findByCountry(String country);
    List<LeagueResponse> findBySeason(String season);
    LeagueResponse addTeamsToLeague(Long leagueId, List<TeamRequest> teamRequests);
    LeagueResponse addTeamsByIdsToLeague(Long leagueId, Set<Long> ids);
    LeagueResponse removeTeamsFromLeague(Long leagueId, List<TeamRequest> teamRequests);
    LeagueResponse removeTeamsByIdsFromLeague(Long leagueId, Set<Long> ids);
}
