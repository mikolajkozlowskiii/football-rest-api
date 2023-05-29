package com.example.football_api.services.football.impl;

import com.example.football_api.dto.football.request.PlayerRequest;
import com.example.football_api.dto.football.response.PlayerResponse;
import com.example.football_api.entities.football.Player;
import com.example.football_api.entities.football.PlayerTeamHistory;
import com.example.football_api.entities.football.Team;
import com.example.football_api.exceptions.football.PlayerNotFoundException;
import com.example.football_api.repositories.football.PlayerRepository;
import com.example.football_api.services.football.PlayerTeamHistoryService;
import com.example.football_api.services.football.PlayerService;
import com.example.football_api.services.football.TeamService;
import com.example.football_api.services.football.mappers.PlayerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final TeamService teamService;
    private final PlayerMapper playerMapper;
    private final PlayerTeamHistoryService playerTeamHistoryService;
    @Override
    public PlayerResponse findPlayerResponseById(Long id) {
        return playerMapper.map(findPlayerById(id));
    }

    @Override
    public Player findPlayerById(Long id) {
        return playerRepository
                .findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));
    }

    @Override
    public List<PlayerResponse> findByTeamIdPlayerResponses(Long teamId) {
        Team team = teamService.findTeamById(teamId);
        List<Player> players = playerRepository.findAllCurrentPlayersInTeam(team);
        return players.stream().map(playerMapper::map).toList();
    }

    @Override
    public List<Player> findByTeamIdPlayer(Long teamId) {
        Team team = teamService.findTeamById(teamId);
        return playerRepository.findAllCurrentPlayersInTeam(team);
    }

    private Player save(Player player){
        return playerRepository.save(player);
    }

    @Override
    public PlayerResponse create(PlayerRequest playerRequest) {
        Player player = playerMapper.map(playerRequest);
        save(player);
        System.out.println(playerMapper.map(player));
        // TODO change Set to just PlayerTeamHistory, should work and also change getTeams.add()
        if(Objects.isNull(playerRequest.getTeamHistoryRequest())){
            return playerMapper.map(player);
        }
        Set<PlayerTeamHistory> teams = playerTeamHistoryService.getNewPlayerTeamHistories(playerRequest.getTeamHistoryRequest(), player);
        player.getTeams().addAll(teams);
        System.out.println(playerMapper.map(player));
        return playerMapper.map(save(player));
    }

    @Override
    public PlayerResponse updatePlayer(Long playerId, PlayerRequest playerRequest) {
        Player player = findPlayerById(playerId);
        Player updatedPlayer = playerMapper.map(playerRequest);
        updatedPlayer.setId(player.getId());
        updatedPlayer.setTeams(player.getTeams());
        save(updatedPlayer);
        // TODO change Set to just PlayerTeamHistory, should work and also change getTeams.add()
        if(Objects.isNull(playerRequest.getTeamHistoryRequest())){
            return playerMapper.map(updatedPlayer);
        }
        Set<PlayerTeamHistory> teams = playerTeamHistoryService.getUpdatedPlayerTeamHistories(playerId, playerRequest.getTeamHistoryRequest(), updatedPlayer);
        updatedPlayer.getTeams().addAll(teams);
        System.out.println(updatedPlayer.getTeams());
        return playerMapper.map(save(updatedPlayer));
    }

    @Override
    public PlayerResponse deletePlayer(Long playerId) {
        Player player = findPlayerById(playerId);
        delete(player);
        return playerMapper.map(player);
    }

    private void delete(Player player){
        playerRepository.delete(player);
    }
}
