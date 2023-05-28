package com.example.football_api.services.football.impl;

import com.example.football_api.dto.football.request.GoalRequest;
import com.example.football_api.dto.football.response.GoalResponse;
import com.example.football_api.dto.football.response.GoalShortResponse;
import com.example.football_api.entities.football.*;
import com.example.football_api.exceptions.football.GoalNotFoundException;
import com.example.football_api.exceptions.football.PlayerNotFoundInMatchException;
import com.example.football_api.exceptions.football.TeamNotFoundInMatchException;
import com.example.football_api.exceptions.football.TooManyGoalsException;
import com.example.football_api.repositories.football.GoalRepository;
import com.example.football_api.services.football.GoalService;
import com.example.football_api.services.football.MatchService;
import com.example.football_api.services.football.PlayerService;
import com.example.football_api.services.football.PlayerTeamHistoryService;
import com.example.football_api.services.football.mappers.GoalMapper;
import com.sun.jdi.InternalException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {
    private final GoalRepository goalRepository;
    private final PlayerService playerService;
    private final MatchService matchService;
    private final GoalMapper goalMapper;
    private final PlayerTeamHistoryService playerTeamHistoryService;

    @Override
    public GoalResponse findGoalResponseById(Long id) {
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
        Match match = matchService.findMatchById(goalRequest.getMatchId());
        Player player = playerService.findPlayerById(goalRequest.getPlayerId());
        Goal goal = goalMapper.map(goalRequest);

        goal.setMatch(match);
        goal.setPlayer(player);

        validateGoal(goal);
        return goalMapper.map(save(goal));
    }

    @Transactional
    private void validateUpdatedGoal(Long changedGoalId, Goal updatedGoal) {
        final Player scorer = updatedGoal.getPlayer();
        final Match match = updatedGoal.getMatch();
        final Team teamScorer = getTeamScorer(scorer.getId(), match, updatedGoal.isOwn());
        int teamScorerGoalsInMatch;
        teamScorerGoalsInMatch = getTeamScorerGoalsToPossibleSave(match, teamScorer);
        long teamScorerSavedGoals = goalRepository
                .findByMatch(match)
                .stream()
                .filter(s->!s.getId().equals(changedGoalId))
                .map(s->getTeamScorer(s.getPlayer().getId(), s.getMatch(), s.isOwn()))
                .filter(s->s.equals(teamScorer))
                .count();

        checkIfScorerTeamHasAlreadySavedAllGoals(match.getId(), teamScorerGoalsInMatch, teamScorerSavedGoals);
    }

    @Transactional
    private void validateGoal(Goal goal) {
        final Player scorer = goal.getPlayer();
        final Match match = goal.getMatch();
        final Team teamScorer = getTeamScorer(scorer.getId(), match, goal.isOwn());
        int teamScorerGoalsInMatch;
        teamScorerGoalsInMatch = getTeamScorerGoalsToPossibleSave(match, teamScorer);
        long teamScorerSavedGoals = goalRepository
                .findByMatch(match)
                .stream()
                .map(s->getTeamScorer(s.getPlayer().getId(), s.getMatch(), s.isOwn()))
                .filter(s->s.equals(teamScorer))
                .count();

        checkIfScorerTeamHasAlreadySavedAllGoals(match.getId(), teamScorerGoalsInMatch, teamScorerSavedGoals);
    }

    private  void checkIfScorerTeamHasAlreadySavedAllGoals(Long matchId, int teamScorerGoalsToPossibleSave, long teamScorerSavedGoals) {
        if(teamScorerGoalsToPossibleSave == teamScorerSavedGoals){
            throw new TooManyGoalsException(matchId);
        }
        else if(teamScorerGoalsToPossibleSave < teamScorerSavedGoals){
            throw new InternalException("Too many goals in DB saved for match: " + matchId);
        }
    }

    private int getTeamScorerGoalsToPossibleSave(Match match, Team teamScorer) {
        int teamScorerGoalsToPossibleSave;
        if(teamScorer.equals(match.getAwayTeam())){
            teamScorerGoalsToPossibleSave = match.getHomeTeamScore();
        } else if (teamScorer.equals(match.getHomeTeam())) {
            teamScorerGoalsToPossibleSave = match.getAwayTeamScore();
        } else {
            throw new TeamNotFoundInMatchException(teamScorer.getId(), match.getId());
        }
        return teamScorerGoalsToPossibleSave;
    }

    public int getTeamSavedNumOfGoals(Match match, Team team){
        List<Goal> allSavedGoals = goalRepository.findByMatch(match);
        return (int) allSavedGoals
                .stream()
                .map(s->getTeamScorer(s.getPlayer().getId(), match, s.isOwn()))
                .filter(s->s.equals(team)).count();
    }

    private Team getTeamScorer(Long playerId, Match match, boolean isOwnGoal) {
        final Team homeTeamDuringMatch = match.getHomeTeam();
        final Team awayTeamDuringMatch = match.getAwayTeam();
        final Team playerTeamDuringMatch = playerTeamHistoryService.findPlayerTeamByDate(playerId, match.getDate());
        if(playerTeamDuringMatch.equals(homeTeamDuringMatch)){
            if(!isOwnGoal){
                return homeTeamDuringMatch;
            }
            else{
                return awayTeamDuringMatch;
            }
        }
        else if(playerTeamDuringMatch.equals(awayTeamDuringMatch)){
            if(!isOwnGoal){
                return awayTeamDuringMatch;
            }
            else{
                return homeTeamDuringMatch;
            }
        }
        else{
            throw new PlayerNotFoundInMatchException(playerId, match.getId());
        }
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
                .time(updateInfo.getTime())
                .build();

        validateUpdatedGoal(goalId, updatedGoal);
        return goalMapper.map(save(updatedGoal));
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
    public List<GoalResponse> findGoalsResponsesByMatchId(Long matchId) {
        final Match match = matchService.findMatchById(matchId);
        final List<Goal> goals = goalRepository.findByMatch(match);
        return goals.stream().map(goalMapper::map).toList();
    }

    @Override
    public List<Goal> findGoalsByMatchId(Long matchId) {
        final Match match = matchService.findMatchById(matchId);
        return goalRepository.findByMatch(match);
    }

    @Override
    public Page<GoalShortResponse> findAllGoals(Pageable pageable) {
         final Page<Goal> goals = goalRepository.findAll(pageable);
         return new PageImpl<GoalShortResponse>(goals.stream().map(goalMapper::mapToShortResponse).toList());
    }
}
