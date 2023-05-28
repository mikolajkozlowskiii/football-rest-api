package com.example.football_api.services.football.mappers;

import com.example.football_api.dto.football.response.overview.GoalOverview;
import com.example.football_api.dto.football.response.overview.LeagueOverview;
import com.example.football_api.dto.football.response.overview.PlayerOverview;
import com.example.football_api.dto.football.response.overview.TeamOverview;
import com.example.football_api.entities.football.Goal;
import com.example.football_api.entities.football.League;
import com.example.football_api.entities.football.Player;
import com.example.football_api.entities.football.Team;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class FootballOverviewMapper {
    public GoalOverview map(Goal goal){
        return GoalOverview.builder()
                .goalId(goal.getId())
                .isOwn(goal.isOwn())
                .playerId(goal.getId())
                .time(goal.getTime())
                .build();
    }

    public TeamOverview map(Set<Player> players, Team team){
        return TeamOverview.builder()
                .teamId(team.getId())
                .name(team.getName())
                .players(players.stream().map(this::map).collect(Collectors.toSet()))
                .build();
    }

    public LeagueOverview map(League league){
        return LeagueOverview.builder()
                .leagueId(league.getId())
                .name(league.getName())
                .country(league.getCountry())
                .season(league.getSeason())
                .build();
    }

    public PlayerOverview map(Player player){
        return PlayerOverview.builder()
                .playerId(player.getId())
                .firstName(player.getFirstName())
                .position(player.getPosition())
                .lastName(player.getLastName())
                .age(Period.between(player.getBirthDate(), LocalDate.now()).getYears())
                .build();
    }
}
