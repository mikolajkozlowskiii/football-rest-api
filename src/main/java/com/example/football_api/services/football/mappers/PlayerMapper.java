package com.example.football_api.services.football.mappers;

import com.example.football_api.dto.football.request.PlayerRequest;
import com.example.football_api.dto.football.response.PlayerResponse;
import com.example.football_api.entities.football.Player;
import com.example.football_api.entities.football.Team;
import com.example.football_api.services.football.PlayerService;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {
    public PlayerResponse map(Player player){
       return PlayerResponse.builder()
               .id(player.getId())
               .birthDate(player.getBirthDate())
               .firstName(player.getFirstName())
               .height(player.getHeight())
               .weight(player.getWeight())
               .lastName(player.getLastName())
               .position(player.getPosition())
               .strongerFeet(player.isStrongerFeet())
               .teamId(player.getTeam().getId())
               .build();
    }

    public Player map(PlayerRequest playerRequest, Team playerTeam){
        return Player.builder()
                .team(playerTeam)
                .birthDate(playerRequest.getBirthDate())
                .firstName(playerRequest.getFirstName())
                .lastName(playerRequest.getLastName())
                .height(playerRequest.getHeight())
                .weight(playerRequest.getWeight())
                .strongerFeet(playerRequest.isStrongerFeet())
                .position(playerRequest.getPosition())
                .build();
    }
}
