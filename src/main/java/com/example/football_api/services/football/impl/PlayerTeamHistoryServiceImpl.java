package com.example.football_api.services.football.impl;

import com.example.football_api.dto.football.request.PlayerTeamHistoryRequest;
import com.example.football_api.entities.football.Player;
import com.example.football_api.entities.football.PlayerTeamHistory;
import com.example.football_api.entities.football.Team;
import com.example.football_api.exceptions.football.DateRangeNotAvailableException;
import com.example.football_api.exceptions.football.PlayerHistoryNotFoundException;
import com.example.football_api.exceptions.football.PlayerNotFoundException;
import com.example.football_api.exceptions.football.TeamNotFoundException;
import com.example.football_api.repositories.football.PlayerHistoryRepository;
import com.example.football_api.services.football.PlayerTeamHistoryService;
import com.example.football_api.services.football.TeamService;
import com.example.football_api.services.football.mappers.PlayerTeamHistoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerTeamHistoryServiceImpl implements PlayerTeamHistoryService {
    private final PlayerHistoryRepository playerHistoryRepository;

    private final TeamService teamService;
    private final PlayerTeamHistoryMapper playerTeamHistoryMapper;
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
    public Set<PlayerTeamHistory> getNewPlayerTeamHistories(PlayerTeamHistoryRequest playerTeamHistoryRequest, Player player) {
        final Team requestedTeam = teamService.findTeamById(playerTeamHistoryRequest.getTeamId());
        final PlayerTeamHistory newPlayerTeam = PlayerTeamHistory.builder()
                .team(requestedTeam)
                .player(player)
                .start(playerTeamHistoryRequest.getStart())
                .ends(playerTeamHistoryRequest.getEnds())
                .build();

        return Set.of(newPlayerTeam);
    }
    @Override
    public Set<PlayerTeamHistory> getUpdatedPlayerTeamHistories(Long playerId, PlayerTeamHistoryRequest playerTeamHistoryRequest, Player player) {
        final LocalDate starts = playerTeamHistoryRequest.getStart();
        final LocalDate ends = playerTeamHistoryRequest.getEnds();
        Set<PlayerTeamHistory> playerTeamHistories = new HashSet<>();
        if (!Objects.isNull(playerId)){
            validDateRange(playerId, starts, ends);
            playerTeamHistories = playerHistoryRepository.findByPlayerId(playerId);
        }
        // TODO check if works without player instance to newPlayerTeam variable
        final Team requestedTeam = teamService.findTeamById(playerTeamHistoryRequest.getTeamId());

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
    public List<PlayerTeamHistory> findAllByTeam(Long teamId) {
        final Team team = teamService.findTeamById(teamId);
        return playerHistoryRepository
                .findAllByTeam(team);
    }

    @Override
    public PlayerTeamHistory delete(Long id) {
        final PlayerTeamHistory playerTeamHistory = findById(id);
        playerHistoryRepository.delete(playerTeamHistory);
        return playerTeamHistory;
    }

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
