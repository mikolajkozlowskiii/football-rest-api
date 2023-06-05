package com.example.football_api.services.football.impl;

import com.example.football_api.dto.football.response.overview.*;
import com.example.football_api.entities.football.*;
import com.example.football_api.services.football.*;
import com.example.football_api.services.football.mappers.FootballOverviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchOverviewImpl implements MatchOverviewService {
    private final LeagueService leagueService;
    private final MatchService matchService;
    private final GoalService goalService;
    private final PlayerService playerService;
    private final FootballOverviewMapper overviewMapper;
    private final PlayerTeamHistoryService playerTeamHistoryService;
    @Override
    public MatchOverview getMatchOverviewByMatchId(Long matchId) {
        final Match match = matchService.findMatchById(matchId);
        final Team homeTeam = match.getHomeTeam();
        final Team awayTeam = match.getAwayTeam();
        final List<Goal> goals = goalService.findGoalsByMatchId(matchId);
        final Set<GoalOverview> goalsOverview = goals.stream().map(overviewMapper::map).sorted(Comparator.comparingInt(GoalOverview::getTime)).collect(Collectors.toCollection(LinkedHashSet::new));
        final Set<Player> homeTeamPlayers = new HashSet<>(playerTeamHistoryService.findPlayersByTeamAndDate(homeTeam, match.getDate()));
        final Set<Player> awayTeamPlayers = new HashSet<>(playerTeamHistoryService.findPlayersByTeamAndDate(awayTeam, match.getDate()));
        final TeamOverview homeTeamOverview = overviewMapper.map(homeTeamPlayers, homeTeam);
        final TeamOverview awayTeamOverview = overviewMapper.map(awayTeamPlayers, awayTeam);
        return MatchOverview.builder()
                .matchId(matchId)
                .date(match.getDate())
                .time(match.getTime())
                .awayTeamScore(match.getAwayTeamScore())
                .homeTeamScore(match.getHomeTeamScore())
                .league(overviewMapper.map(match.getLeague()))
                .homeTeam(homeTeamOverview)
                .awayTeam(awayTeamOverview)
                .goals(goalsOverview)
                .build();
    }

    @Override
    public Page<MatchOverview> getAll(Pageable pageable) {
        final List<Match> matches =  matchService.findAll(pageable).stream().toList();
        return new PageImpl<MatchOverview>(
                matches
                        .stream()
                        .map(s->getMatchOverviewByMatchId(s.getId()))
                        .collect(Collectors.toList()));
    }

    @Override
    public Page<MatchOverview> getMatchesOverviewByLeagueId(Long leagueId, Pageable pageable) {
         final League league = leagueService.findLeagueById(leagueId);
         final List<Match> matches =  matchService.findByLeagueId(leagueId, pageable);
         return new PageImpl<MatchOverview>(
                 matches
                 .stream()
                 .map(s->getMatchOverviewByMatchId(s.getId()))
                 .collect(Collectors.toList())
         );
    }

    @Override
    public Page<MatchOverview> getMatchesOverviewByLeagueIdAndTeamId(Long leagueId, Long teamId, Pageable pageable) {
        final Page<Match> matches = matchService.findByLeagueIdAndTeamId(leagueId, teamId, pageable);
        return new PageImpl<MatchOverview>(
              matches
                      .stream()
                      .map(s->getMatchOverviewByMatchId(s.getId()))
                      .collect(Collectors.toList())
        );
    }

    @Override
    public Page<MatchOverview> getAllTodayMatches(Pageable pageable) {
        final List<Match> todayMatches = matchService.findAllByDateToday(pageable);
        return new PageImpl<MatchOverview>(
                todayMatches
                        .stream()
                        .map(s->getMatchOverviewByMatchId(s.getId()))
                        .collect(Collectors.toList())
        );
    }

}
