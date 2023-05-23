package com.example.football_api.services.football.impl;

import com.example.football_api.dto.football.request.PlayerRequest;
import com.example.football_api.dto.football.response.PlayerResponse;
import com.example.football_api.entities.football.Player;
import com.example.football_api.entities.football.Team;
import com.example.football_api.exceptions.football.PlayerNotFoundException;
import com.example.football_api.repositories.football.PlayerRepository;
import com.example.football_api.services.football.PlayerService;
import com.example.football_api.services.football.TeamService;
import com.example.football_api.services.football.mappers.PlayerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final TeamService teamService;
    private final PlayerMapper playerMapper;
    @Override
    public PlayerResponse findPlayerResponseById(Long id) {
        return playerMapper.map(findById(id));
    }

    private Player findById(Long id){
        return playerRepository
                .findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));
    }

    @Override
    public List<PlayerResponse> findByTeamIdPlayerResponses(Long teamId) {
        Team team = teamService.findTeamById(teamId);
        List<Player> players = playerRepository.findByTeam(team);
        return players.stream().map(playerMapper::map).toList();
    }

    @Override
    public PlayerResponse save(PlayerRequest playerRequest) {
        Team team = teamService.findTeamById(playerRequest.getTeamId());
        Player player = playerMapper.map(playerRequest, team);
        return playerMapper.map(save(player));
    }
    private Player save(Player player){
        return playerRepository.save(player);
    }

    @Override
    public PlayerResponse deletePlayer(Long playerId) {
        Player player = findById(playerId);
        delete(player);
        return playerMapper.map(player);
    }

    private void delete(Player player){
        playerRepository.delete(player);
    }

    @Override
    public PlayerResponse updatePlayer(Long playerId, PlayerRequest playerRequest) {
        Player player = findById(playerId);
        Team team = teamService.findTeamById(playerRequest.getTeamId());
        Player updatedPlayer = getUpdatedPlayer(playerRequest, player, team);
        save(updatedPlayer);
        return playerMapper.map(updatedPlayer);
    }

    private static Player getUpdatedPlayer(PlayerRequest playerRequest, Player player, Team team) {
        Player updatedPlayer = Player.builder()
                .id(player.getId())
                .firstName(playerRequest.getFirstName())
                .lastName(playerRequest.getLastName())
                .weight(playerRequest.getWeight())
                .height(playerRequest.getHeight())
                .strongerFeet(playerRequest.isStrongerFeet())
                .position(playerRequest.getPosition())
                .birthDate(playerRequest.getBirthDate())
                .team(team)
                .build();
        return updatedPlayer;
    }
}
