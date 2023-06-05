package com.example.football_api.services.football.impl;

import com.example.football_api.dto.football.request.MatchRequest;
import com.example.football_api.dto.football.response.GoalResponse;
import com.example.football_api.dto.football.response.MatchResponse;
import com.example.football_api.entities.football.*;
import com.example.football_api.exceptions.SortByException;
import com.example.football_api.exceptions.football.*;
import com.example.football_api.repositories.football.GoalRepository;
import com.example.football_api.repositories.football.MatchRepository;
import com.example.football_api.services.football.*;
import com.example.football_api.services.football.mappers.MatchMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MatchServiceImpl implements MatchService {
    private MatchRepository matchRepository;
    private MatchMapper matchMapper;
    private LeagueService leagueService;
    private TeamService teamService;
    private GoalService goalService;
    private PlayerTeamHistoryService playerTeamHistoryService;

    public MatchServiceImpl(MatchRepository matchRepository, MatchMapper matchMapper,
                            LeagueService leagueService, TeamService teamService,
                            @Lazy GoalService goalService, @Lazy PlayerTeamHistoryService playerTeamHistoryService) {
        this.matchRepository = matchRepository;
        this.matchMapper = matchMapper;
        this.leagueService = leagueService;
        this.teamService = teamService;
        this.goalService = goalService;
        this.playerTeamHistoryService = playerTeamHistoryService;
    }

    @Override
    public MatchResponse save(MatchRequest matchRequest) {
        Match match = getMatchEntity(matchRequest);

        return matchMapper.map(save(match));
    }

    @Override
    public Match saveEntity(MatchRequest matchRequest) {
        Match match = getMatchEntity(matchRequest);

        return save(match);
    }

    private Match getMatchEntity(MatchRequest matchRequest) {
        final League league = leagueService.findLeagueById(matchRequest.getLeagueId());
        final Team homeTeam = teamService.findTeamById(matchRequest.getHomeTeamId());
        final Team awayTeam = teamService.findTeamById(matchRequest.getAwayTeamId());
        checkIfLeagueContainsTeams(matchRequest);

        Match match = Match.builder()
                .league(league)
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .homeTeamScore(matchRequest.getHomeTeamScore())
                .awayTeamScore(matchRequest.getAwayTeamScore())
                .date(matchRequest.getDate())
                .time(matchRequest.getTime())
                .build();
        return match;
    }

    private void checkIfLeagueContainsTeams(MatchRequest matchRequest) {
        final Long leagueId = matchRequest.getLeagueId();
        final Long homeTeamId = matchRequest.getHomeTeamId();
        final Long awayTeamId = matchRequest.getAwayTeamId();
        if(!teamService.isTeamInLeague(homeTeamId, leagueId)){
            throw new LeagueNotContainTeamException(leagueId, homeTeamId);
        }
        if(!teamService.isTeamInLeague(awayTeamId, leagueId)){
            throw new LeagueNotContainTeamException(leagueId, awayTeamId);
        }
    }

    private Match save(Match match) {
        return matchRepository.save(match);
    }

    @Override
    public Page<MatchResponse> findAllResponses(Pageable pageable) {
        try{
            Page<Match> matches = matchRepository.findAll(pageable);
            return new PageImpl<MatchResponse>(matches.stream().map(s->matchMapper.map(s, goalService.findGoalsByMatchId(s.getId()))).toList());
        }catch (PropertyReferenceException ex){
            throw new SortByException(pageable.getSort().toString());
        }
    }

    @Override
    public Page<Match> findAll(Pageable pageable) {
        try{
            return matchRepository.findAll(pageable);
        }catch (PropertyReferenceException ex){
            throw new SortByException(pageable.getSort().toString());
        }
    }

    @Override
    public List<MatchResponse> findMatchesResponseByLeagueIdAndTeamId(Long leagueId, Long teamId) {
        List<Match> matches = matchRepository.findByLeagueIdAndTeamId(leagueId, teamId);
        return matches.stream().map(s->matchMapper.map(s, goalService.findGoalsByMatchId(s.getId()))).toList();
    }

    @Override
    public List<Match> findByLeagueIdAndTeamId(Long leagueId, Long teamId) {
        return matchRepository.findByLeagueIdAndTeamId(leagueId, teamId);
    }

    @Override
    public Page<Match> findByLeagueIdAndTeamId(Long leagueId, Long teamId, Pageable pageable) {
        return matchRepository.findByLeagueIdAndTeamId(leagueId, teamId, pageable);
    }

    @Override
    public List<Match> findByLeagueId(Long leagueId) {
        return matchRepository.findByLeagueId(leagueId);
    }

    @Override
    public List<Match> findByLeagueId(Long leagueId, Pageable pageable) {
        return matchRepository.findByLeagueId(leagueId, pageable);
    }

    @Override
    public List<MatchResponse> findAllByDateTodayMatchesResponse() {
        List<Match> matches = matchRepository.findAllByDateToday();
        return matches.stream().map(s->matchMapper.map(s, goalService.findGoalsByMatchId(s.getId()))).toList();
    }
    @Override
    public List<MatchResponse> findAllByDate(LocalDate date) {
        List<Match> matches = matchRepository.findAllByDate(date);
        return matches.stream().map(s->matchMapper.map(s, goalService.findGoalsByMatchId(s.getId()))).toList();
    }

    @Override
    public List<Match> findAllByDateToday() {
        return matchRepository.findAllByDateToday();
    }

    @Override
    public List<Match> findAllByDateToday(Pageable pageable) {
        return matchRepository.findAllByDateToday(pageable);
    }

    @Override
    public MatchResponse findMatchResponseById(Long matchId) {
        Match match = findMatchByID(matchId);
        return matchMapper.map(match, goalService.findGoalsByMatchId(matchId));
    }

    @Override
    public Match findMatchById(Long matchId) {
        return findMatchByID(matchId);
    }

    private Match findMatchByID(Long matchId) {
        return matchRepository
                .findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));
    }

    @Override
    public MatchResponse update(Long matchId, MatchRequest matchRequest) {
        checkIfLeagueContainsTeams(matchRequest);
        final Match match = findMatchByID(matchId);

        validateRequestUpdateNumOfGoals(matchRequest, match);
        validateRequestUpdateDate(matchRequest, match);

        Match updatedMatch = getMatchEntity(matchRequest);
        updatedMatch.setId(matchId);

        return matchMapper.map(save(updatedMatch));
    }

    private void validateRequestUpdateDate(MatchRequest matchRequest, Match match) {
        final List<Goal> goals = goalService.findGoalsByMatchId(match.getId());
        final Set<Long> scorrersIds = goals.stream().map(s->s.getPlayer().getId()).collect(Collectors.toSet());
        for(Long scorrerId : scorrersIds){
            final Team beforeUpdateTeam = playerTeamHistoryService.findPlayerTeamByDate(scorrerId, match.getDate());
            final Team afterUpdateTeam = playerTeamHistoryService.findPlayerTeamByDate(scorrerId, matchRequest.getDate());
            if(!beforeUpdateTeam.equals(afterUpdateTeam)){
                throw new DateInconsistencyViolationException(matchRequest.getDate());
            }
        }
    }

    private void validateRequestUpdateNumOfGoals(MatchRequest matchRequest, Match match) {
        final int savedHomeTeamGoals = goalService.getTeamSavedNumOfGoals(match, match.getHomeTeam());
        final int savedAwayTeamGoals = goalService.getTeamSavedNumOfGoals(match, match.getAwayTeam());
        final int requestedHomeTeamGoals = matchRequest.getHomeTeamScore();
        final int requestedAwayTeamGoals = matchRequest.getAwayTeamScore();
        if(savedHomeTeamGoals > requestedHomeTeamGoals){
            throw new SavedGoalsInconsistencyViolationException(requestedHomeTeamGoals, savedHomeTeamGoals);
        }
        if(savedAwayTeamGoals > requestedAwayTeamGoals){
            throw new SavedGoalsInconsistencyViolationException(requestedAwayTeamGoals, savedAwayTeamGoals);
        }
    }

    @Override
    public void deleteById(Long matchId){
        matchRepository.deleteById(matchId);
    }
}
