package com.example.football_api.services.football;

import com.example.football_api.dto.football.response.overview.MatchOverview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface MatchOverviewService {
    MatchOverview getMatchOverviewByMatchId(Long matchId);
    Page<MatchOverview> getMatchesOverviewByLeagueId(Long leagueId, Pageable pageable);
    Page<MatchOverview> getMatchesOverviewByLeagueIdAndTeamId(Long leagueId, Long teamId, Pageable pageable);
    Page<MatchOverview> getAllTodayMatches(Pageable pageable);
}
