package com.example.football_api.services.football;

import com.example.football_api.dto.football.response.LeagueResponse;
import com.example.football_api.entities.LeagueFollow;
import com.example.football_api.security.userDetails.UserDetailsImpl;

import java.util.List;
import java.util.Set;

public interface LeagueFollowService {
    public Set<Long> findAllUsersFollowedLeaguesIds(Long userId);
    public LeagueFollow followLeague(Long leagueId, Long userId);
    public void unFollowLeague(Long leagueId, Long userId);
}
