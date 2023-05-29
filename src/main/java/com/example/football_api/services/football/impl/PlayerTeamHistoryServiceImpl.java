package com.example.football_api.services.football.impl;

import com.example.football_api.dto.football.request.PlayerTeamHistoryRequest;
import com.example.football_api.dto.football.request.TeamHistoryRequest;
import com.example.football_api.dto.football.response.PlayerTeamHistoryResponse;
import com.example.football_api.entities.football.Goal;
import com.example.football_api.entities.football.Player;
import com.example.football_api.entities.football.PlayerTeamHistory;
import com.example.football_api.entities.football.Team;
import com.example.football_api.exceptions.football.DateRangeNotAvailableException;
import com.example.football_api.exceptions.football.PlayerNotFoundException;
import com.example.football_api.exceptions.football.TeamNotFoundException;
import com.example.football_api.repositories.football.PlayerHistoryRepository;
import com.example.football_api.services.football.GoalService;
import com.example.football_api.services.football.PlayerService;
import com.example.football_api.services.football.PlayerTeamHistoryService;
import com.example.football_api.services.football.TeamService;
import com.example.football_api.services.football.mappers.PlayerTeamHistoryMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlayerTeamHistoryServiceImpl implements PlayerTeamHistoryService {
    private PlayerHistoryRepository playerHistoryRepository;
    private TeamService teamService;
    private PlayerTeamHistoryMapper playerTeamHistoryMapper;
    private GoalService goalService;
    private PlayerService playerService;

    public PlayerTeamHistoryServiceImpl(PlayerHistoryRepository playerHistoryRepository,
                                        TeamService teamService,
                                        PlayerTeamHistoryMapper playerTeamHistoryMapper,
                                        @Lazy GoalService goalService,
                                        @Lazy PlayerService playerService) {
        this.playerHistoryRepository = playerHistoryRepository;
        this.teamService = teamService;
        this.playerTeamHistoryMapper = playerTeamHistoryMapper;
        this.goalService = goalService;
        this.playerService = playerService;
    }

    @Override
    public PlayerTeamHistory save(PlayerTeamHistory playerTeamHistory) {
        return playerHistoryRepository.save(playerTeamHistory);
    }


    @Override
    public Team findPlayerTeamByDate(Long playerId, LocalDate date) {
        return playerHistoryRepository
                .findByPlayerAndDate(playerId, date)
                .map(PlayerTeamHistory::getTeam)
                .orElseThrow(() -> new TeamNotFoundException(playerId, date));
    }

    @Override
    public PlayerTeamHistoryResponse save(PlayerTeamHistoryRequest request) {
        validDateRange(request.getPlayerId(), request.getStart(), request.getEnds());
        final Player player = playerService.findPlayerById(request.getPlayerId());
        final Team requestedTeam = teamService.findTeamById(request.getTeamId());
        final PlayerTeamHistory newPlayerTeam = PlayerTeamHistory.builder()
                .team(requestedTeam)
                .player(player)
                .start(request.getStart())
                .ends(request.getEnds())
                .build();
        return map(save(newPlayerTeam));
    }

    @Override
    public Set<PlayerTeamHistory> getNewPlayerTeamHistories(TeamHistoryRequest teamHistoryRequest, Player player) {
        final Team requestedTeam = teamService.findTeamById(teamHistoryRequest.getTeamId());
        final PlayerTeamHistory newPlayerTeam = PlayerTeamHistory.builder()
                .team(requestedTeam)
                .player(player)
                .start(teamHistoryRequest.getStart())
                .ends(teamHistoryRequest.getEnds())
                .build();

        return Set.of(newPlayerTeam);
    }
    // TODO check if 2 args works
    @Override
    public Set<PlayerTeamHistory> getUpdatedPlayerTeamHistories(Long playerId, TeamHistoryRequest teamHistoryRequest, Player player) {
        final LocalDate starts = teamHistoryRequest.getStart();
        final LocalDate ends = teamHistoryRequest.getEnds();
        Set<PlayerTeamHistory> playerTeamHistories = new HashSet<>();
        if (!Objects.isNull(playerId)){
            validDateRange(playerId, starts, ends);
            playerTeamHistories = playerHistoryRepository.findByPlayerId(playerId);
        }
        // TODO check if works without player instance to newPlayerTeam variable
        final Team requestedTeam = teamService.findTeamById(teamHistoryRequest.getTeamId());

        final PlayerTeamHistory newPlayerTeam = PlayerTeamHistory.builder()
                .team(requestedTeam)
                .player(player)
                .start(starts)
                .ends(ends)
                .build();
        playerTeamHistories.add(newPlayerTeam);

        return playerTeamHistories;
    }

    private void validDateRange(Long playerId, LocalDate starts, LocalDate ends) {
        boolean isDateRangeAvailable = playerHistoryRepository.isDateRangeAvailable(playerId, starts, ends);
        if(!isDateRangeAvailable){
            throw new DateRangeNotAvailableException(starts, ends);
        }
    }

    private void validUpdateDateRange(Long playerId, LocalDate starts, LocalDate ends, PlayerTeamHistory beforeUpdateTeamHistory) {
        int numOfPlayerTeamHistorySavedInDB = (int) playerHistoryRepository
                .getAllPlayerTeamHistoryInRange(playerId, starts, ends)
                .stream()
                .filter(s->!s.getTeam().getId().equals(beforeUpdateTeamHistory.getTeam().getId()))
                .count();
        if(numOfPlayerTeamHistorySavedInDB>0){
            throw new DateRangeNotAvailableException(starts, ends);
        }
    }

    @Override
    public List<PlayerTeamHistory> findByPlayerAndDate(Player player, LocalDate date) {
        final List<PlayerTeamHistory> playerTeamHistories =
                playerHistoryRepository.findByPlayerAndDateIsInRange(player, date);
        return playerTeamHistories;
    }

    @Override
    public List<Player> findPlayersByTeamAndDate(Team team, LocalDate date){
        return  playerHistoryRepository.findByTeamAndDate(team, date).stream().map(PlayerTeamHistory::getPlayer).collect(Collectors.toList());
    }

    public PlayerTeamHistory createNewPlayerTeamHistory(Player player, Team currentTeam) {
        return PlayerTeamHistory.builder()
                .player(player)
                .team(currentTeam)
                .start(LocalDate.now())
                .ends(null)
                .build();
    }

    @Override
    public PlayerTeamHistory findById(Long id) {
        return playerHistoryRepository
                .findById(id)
                .orElseThrow(()-> new PlayerNotFoundException(id));
    }

    @Override
    public PlayerTeamHistoryResponse findResponseById(Long id) {
        return map(findById(id));
    }

    @Override
    public List<PlayerTeamHistory> findAllByTeam(Long teamId) {
        final Team team = teamService.findTeamById(teamId);
        return playerHistoryRepository
                .findAllByTeam(team);
    }

    @Override
    public PlayerTeamHistory delete(Long id) {
        final PlayerTeamHistory playerTeamHistory = findById(id);
        delete(playerTeamHistory);
        return playerTeamHistory;
    }

    @Override
    public PlayerTeamHistoryResponse deleteResponse(Long id) {
        return map(delete(id));
    }

    public void delete(PlayerTeamHistory playerTeamHistory){
        deleteGoalsScoredWhileTeamHistoryDateRange(playerTeamHistory);
        playerHistoryRepository.delete(playerTeamHistory);
    }

    private void deleteGoalsScoredWhileTeamHistoryDateRange(PlayerTeamHistory playerTeamHistory) {
        List<Goal> goals = goalService.findByPlayerAndDatesRange(
                playerTeamHistory.getPlayer(),
                playerTeamHistory.getStart(),
                playerTeamHistory.getEnds()
        );
        goals.forEach(s->goalService.deleteGoal(s.getId()));
    }

    @Override
    public PlayerTeamHistory update(Long playerTeamHistoryId, PlayerTeamHistoryRequest request) {
        PlayerTeamHistory playerTeamHistory = findById(playerTeamHistoryId);
        validUpdateDateRange(playerTeamHistory.getPlayer().getId(), request.getStart(), request.getEnds(), playerTeamHistory);
        removeGoals(request, playerTeamHistory);
        final Player player = playerService.findPlayerById(request.getPlayerId());
        final Team team = teamService.findTeamById(request.getTeamId());
        playerTeamHistory.setPlayer(player);
        playerTeamHistory.setTeam(team);
        playerTeamHistory.setStart(request.getStart());
        playerTeamHistory.setEnds(request.getEnds());
        return save(playerTeamHistory);
    }

    @Override
    public PlayerTeamHistoryResponse updateResponse(Long playerTeamHistoryId, PlayerTeamHistoryRequest playerTeamHistoryRequest) {
        return map(update(playerTeamHistoryId, playerTeamHistoryRequest));
    }

    private PlayerTeamHistoryResponse map(PlayerTeamHistory playerTeamHistory){
        return PlayerTeamHistoryResponse.builder()
                .id(playerTeamHistory.getId())
                .teamId(playerTeamHistory.getTeam().getId())
                .playerId(playerTeamHistory.getPlayer().getId())
                .start(playerTeamHistory.getStart())
                .ends(playerTeamHistory.getEnds())
                .build();
    }
    // TODO zle dziala!!!
    private void removeGoals(PlayerTeamHistoryRequest request, PlayerTeamHistory playerTeamHistory) {
        final LocalDate requestedStarts = request.getStart();
        final LocalDate requestedEnds = request.getEnds();
        final LocalDate savedStarts = playerTeamHistory.getStart();
        final LocalDate savedEnds = playerTeamHistory.getEnds();
        if(!request.getTeamId().equals(playerTeamHistory.getTeam().getId())){
            deleteGoalsScoredWhileTeamHistoryDateRange(playerTeamHistory);
        }
        if (request.getTeamId().equals(playerTeamHistory.getTeam().getId()) &&
                savedStarts.isBefore(requestedStarts)){
            List<Goal> goals = goalService.findByPlayerAndDatesRange(
                    playerTeamHistory.getPlayer(),
                    savedStarts,
                    requestedStarts
            );
            System.out.println("1");
            goals.forEach(s-> System.out.println(s.getId()));
            goals.forEach(s->goalService.deleteGoal(s.getId()));
        }
        if (request.getTeamId().equals(playerTeamHistory.getTeam().getId()) &&
                savedEnds.isAfter(requestedEnds)){
            List<Goal> goals = goalService.findByPlayerAndDatesRange(
                    playerTeamHistory.getPlayer(),
                    requestedEnds,
                    savedEnds
            );
            System.out.println("2");
            goals.forEach(s-> System.out.println(s.getId()));
            goals.forEach(s->goalService.deleteGoal(s.getId()));
        }
    }
   // "start": "2023-02-09",
    @Override
    public PlayerTeamHistory update(Long playerHistoryId, PlayerTeamHistory updateInfo) {
        PlayerTeamHistory playerTeamHistory = findById(playerHistoryId);
        playerTeamHistory.setPlayer(updateInfo.getPlayer());
        playerTeamHistory.setTeam(playerTeamHistory.getTeam());
        playerTeamHistory.setEnds(updateInfo.getEnds());
        playerTeamHistory.setStart(updateInfo.getStart());
        save(playerTeamHistory);
        return playerTeamHistory;
    }
}
