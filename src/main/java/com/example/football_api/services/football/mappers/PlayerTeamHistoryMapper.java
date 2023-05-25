package com.example.football_api.services.football.mappers;

import com.example.football_api.dto.football.request.PlayerTeamHistoryRequest;
import com.example.football_api.dto.football.response.PlayerTeamHistoryResponse;
import com.example.football_api.entities.football.PlayerTeamHistory;
import org.springframework.stereotype.Component;

@Component
public class PlayerTeamHistoryMapper {
    public PlayerTeamHistoryResponse map(PlayerTeamHistory playerTeamHistory) {
        return PlayerTeamHistoryResponse.builder()
                .id(playerTeamHistory.getId())
                .teamId(playerTeamHistory.getTeam().getId())
                .playerId(playerTeamHistory.getPlayer().getId())
                .ends(playerTeamHistory.getEnds())
                .start(playerTeamHistory.getStart())
                .build();
    }
}
