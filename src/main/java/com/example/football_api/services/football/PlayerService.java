package com.example.football_api.services.football;

import com.example.football_api.dto.football.request.PlayerRequest;
import com.example.football_api.dto.football.response.PlayerResponse;
import com.example.football_api.entities.football.Player;
import com.example.football_api.entities.football.Team;

import java.util.List;

public interface PlayerService {
    PlayerResponse findPlayerResponseById(Long id);
    List<PlayerResponse> findByTeamIdPlayerResponses(Long teamId);
    PlayerResponse save(PlayerRequest playerRequest);
    PlayerResponse deletePlayer(Long playerId);
    PlayerResponse updatePlayer(Long playerId, PlayerRequest playerRequest);
}
