package com.example.football_api.services.football.mappers;

import com.example.football_api.dto.football.request.PlayerRequest;
import com.example.football_api.dto.football.response.PlayerResponse;
import com.example.football_api.entities.football.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PlayerMapper {
    private final PlayerTeamHistoryMapper playerTeamHistoryResponseMapper;
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
               .teamsHistory(Optional.ofNullable(player.getTeams())
                       .map(teams -> teams.stream()
                               .map(playerTeamHistoryResponseMapper::map)
                               .collect(Collectors.toSet()))
                       .orElseGet(HashSet::new))
               .build();
    }

    public Player map(PlayerRequest playerRequest){
        return Player.builder()
                .birthDate(playerRequest.getBirthDate())
                .firstName(playerRequest.getFirstName())
                .lastName(playerRequest.getLastName())
                .height(playerRequest.getHeight())
                .weight(playerRequest.getWeight())
                .strongerFeet(playerRequest.isStrongerFeet())
                .position(playerRequest.getPosition())
                .teams(new HashSet<>())
                .build();
    }
}
