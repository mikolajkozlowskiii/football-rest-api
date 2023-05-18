package com.example.football_api.services.football.mappers;

import com.example.football_api.dto.football.response.MatchResponse;
import com.example.football_api.entities.football.Match;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MatchMapper {
    private final TeamMapper teamMapper;
    private final LeagueMapper leagueMapper;
    public MatchResponse map(Match match){
        return MatchResponse.builder()
                .id(match.getId())
                .league(leagueMapper.map(match.getLeague()))
                .awayTeam(teamMapper.map(match.getAwayTeam()))
                .homeTeam(teamMapper.map(match.getHomeTeam()))
                .awayTeamScore(match.getAwayTeamScore())
                .homeTeamScore(match.getHomeTeamScore())
                .date(match.getDate())
                .time(match.getTime())
                .build();
    }
}
