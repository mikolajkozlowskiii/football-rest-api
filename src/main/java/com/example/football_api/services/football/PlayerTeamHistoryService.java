package com.example.football_api.services.football;

import com.example.football_api.dto.football.request.PlayerTeamHistoryRequest;
import com.example.football_api.entities.football.Player;
import com.example.football_api.entities.football.PlayerTeamHistory;
import com.example.football_api.entities.football.Team;
import org.springframework.security.core.parameters.P;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface PlayerTeamHistoryService {
    PlayerTeamHistory save(PlayerTeamHistory playerTeamHistory);
   // PlayerTeamHistory save(Player player, Team currentTeam);
    PlayerTeamHistory createNewPlayerTeamHistory(Player player, Team newTeam);

    Set<PlayerTeamHistory> getNewPlayerTeamHistories(PlayerTeamHistoryRequest playerTeamHistoryRequest, Player player);
    Set<PlayerTeamHistory> getUpdatedPlayerTeamHistories(Long playerId, PlayerTeamHistoryRequest playerTeamHistoryRequest, Player player);
  //  PlayerHistory startsNewPlayerHistory(Player player);
   // PlayerHistory endsPlayerHistory(Player player);
   // PlayerHistory addNewTeamToPlayerHistory(Player player, Team team);
    List<PlayerTeamHistory> findByPlayerAndDateInRangeBeetweenStartsAndEnds(Player player, LocalDate date);
    PlayerTeamHistory findById(Long id);
    List<PlayerTeamHistory> findAllByTeam(Long teamId);
    PlayerTeamHistory delete(Long id);
    PlayerTeamHistory update(Long playerHistoryId, PlayerTeamHistory updateInfo);
}
