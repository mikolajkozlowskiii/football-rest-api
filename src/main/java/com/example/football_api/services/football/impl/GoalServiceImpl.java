package com.example.football_api.services.football.impl;

import com.example.football_api.dto.football.request.GoalRequest;
import com.example.football_api.dto.football.response.GoalResponse;
import com.example.football_api.entities.football.*;
import com.example.football_api.exceptions.football.GoalNotFoundException;
import com.example.football_api.repositories.football.GoalRepository;
import com.example.football_api.services.football.GoalService;
import com.example.football_api.services.football.MatchService;
import com.example.football_api.services.football.PlayerService;
import com.example.football_api.services.football.PlayerTeamHistoryService;
import com.example.football_api.services.football.mappers.GoalMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {
    private final GoalRepository goalRepository;
    private final PlayerService playerService;
    private final MatchService matchService;
    private final GoalMapper goalMapper;
    private final PlayerTeamHistoryService playerTeamHistoryService;

    @Override
    public GoalResponse findGoalById(Long id) {
        final Goal goal = findById(id);
        return goalMapper.map(goal);
    }

    public Goal findById(Long id){
        return goalRepository
                .findById(id)
                .orElseThrow(() -> new GoalNotFoundException(id));
    }

    @Override
    public GoalResponse saveGoal(GoalRequest goalRequest) {
        Goal goal = goalMapper.map(goalRequest);
        Match match = matchService.findMatchById(goalRequest.getMatchId());
        Player player = playerService.findPlayerById(goalRequest.getPlayerId());

        goal.setMatch(match);
        goal.setPlayer(player);
        save(goal);
        return goalMapper.map(goal);
    }

    public void validateGoalRequest(Match match, Player player){
        // sprawdzic czy player mogl w ogole grac w tym meczu



        //playerTeamHistoryService.findByDateInRangeStartsAndEnds()


        // sprawdzic czy goal w ogole mogl pasc
        final int homeTeamScore = match.getHomeTeamScore();
        final int awayTeamScore = match.getAwayTeamScore();

        // homeTeam i awayTeam
        // czy player jest w homeTeam w tym okresie
        // czy mogl zdobyc bramke? ( 3:2 -> 5 bramek, czy 5 rekordow goal dla tego meczu)
        // czy mogl zdobyc bramke own lub !own
        // ile mamy own goli dla tego meczu
        // homeTeamScore = 3,List<Goal> allGoalsByMatchId = ....
        // sprawdzenie czy to jest homeTeamScore czy awayTeamScore
        // Player  player = Goal.getPlayer
        // PlayerHistory playerHistory = getPlayerHistoryByDate(goal.getDate)
        // Team currentPlayerTeam = playerHistory.getTeam()
        // check if team == teamHome, else if team == teamaway, else throw error
        // iterate current check goal


        // jesli tak to sprawdzic czy mogl pasc jako samoboj czy nie
        // jesli jako samoboj to player ma data beetwen match data i team id == awayTeamId
        // // jesli jako normalny  to player ma data beetwen match data i team id == h
        LocalDate matchDate = match.getDate();
        Set<PlayerTeamHistory> playerHistory = player.getTeams();
    }

    public Goal save(Goal goal){
        return goalRepository.save(goal);
    }

    @Override
    public GoalResponse updateGoal(Long goalId, GoalRequest updateInfo) {
        Goal goal = findById(goalId);
        final Match match = matchService.findMatchById(updateInfo.getMatchId());
        final Player player = playerService.findPlayerById(updateInfo.getPlayerId());

        Goal updatedGoal = Goal.builder()
                .id(goal.getId())
                .isOwn(updateInfo.isOwn())
                .match(match)
                .player(player)
                .build();
        save(updatedGoal);
        return goalMapper.map(updatedGoal);
    }

    @Override
    public GoalResponse deleteGoal(Long id) {
        final Goal goal = findById(id);
        delete(goal);
        return goalMapper.map(goal);
    }
    public void delete(Goal goal){
        goalRepository.delete(goal);
    }

    @Override
    public List<GoalResponse> findGoalsByMatchId(Long matchId) {
        final Match match = matchService.findMatchById(matchId);
        final List<Goal> goals = goalRepository.findByMatch(match);
        return goals.stream().map(goalMapper::map).toList();
    }

    @Override
    public Page<GoalResponse> findAllGoals(Pageable pageable) {
         final Page<Goal> goals = goalRepository.findAll(pageable);
         return new PageImpl<GoalResponse>(goals.stream().map(goalMapper::map).toList());
    }
}
