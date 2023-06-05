package com.example.football_api.unit.services;

import com.example.football_api.dto.football.request.MatchRequest;
import com.example.football_api.entities.football.League;
import com.example.football_api.entities.football.Match;
import com.example.football_api.entities.football.Team;
import com.example.football_api.exceptions.football.LeagueNotContainTeamException;
import com.example.football_api.repositories.football.MatchRepository;
import com.example.football_api.services.football.LeagueService;
import com.example.football_api.services.football.TeamService;
import com.example.football_api.services.football.impl.MatchServiceImpl;
import com.example.football_api.services.football.mappers.MatchMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class MatchServiceTest {
    @InjectMocks
    private MatchServiceImpl matchService;
    @Mock
    private MatchRepository matchRepository;
    @Mock
    private MatchMapper matchMapper;
    @Mock
    private LeagueService leagueService;
    @Mock
    private TeamService teamService;

    @Test
    public void save_MatchEntity_ReturnsMatchResponse(){
        MatchRequest matchRequest = MatchRequest.builder()
                .homeTeamId(1L)
                .awayTeamId(2L)
                .leagueId(1L)
                .homeTeamScore(3)
                .awayTeamScore(0)
                .date(LocalDate.of(2023, 5, 18))
                .time(LocalTime.of(21, 30))
                .build();

        Match match = Match.builder()
                .homeTeam(Team.builder().id(1L).build())
                .awayTeam(Team.builder().id(2L).build())
                .league(League.builder().id(1L).build())
                .homeTeamScore(3)
                .awayTeamScore(0)
                .date(LocalDate.of(2023, 5, 18))
                .time(LocalTime.of(21, 30))
                .build();

        when(teamService.isTeamInLeague(anyLong(), anyLong())).thenReturn(true);
        when(matchRepository.save(any())).thenReturn(match);

        Match expectedMatch = match;
        Match actualMatch = matchService.saveEntity(matchRequest);

        Assertions.assertEquals(expectedMatch, actualMatch);
    }

    @Test
    public void save_LeagueNotContainsTeams_ThrownLeagueNotContainsTeamException(){
        MatchRequest matchRequest = MatchRequest.builder()
                .homeTeamId(1L)
                .awayTeamId(2L)
                .leagueId(1L)
                .homeTeamScore(3)
                .awayTeamScore(0)
                .date(LocalDate.of(2023, 5, 18))
                .time(LocalTime.of(21, 30))
                .build();

        when(teamService.isTeamInLeague(anyLong(), anyLong())).thenReturn(false);

        Assertions.assertThrows(LeagueNotContainTeamException.class, () -> matchService.saveEntity(matchRequest));
    }

    @Test
    @Disabled
    public void findAll(){
        Match match = Match.builder()
                .homeTeam(Team.builder().id(1L).build())
                .awayTeam(Team.builder().id(2L).build())
                .league(League.builder().id(1L).build())
                .homeTeamScore(3)
                .awayTeamScore(0)
                .date(LocalDate.of(2023, 5, 18))
                .time(LocalTime.of(21, 30))
                .build();

       // when(matchRepository.findAll(any(PageRequest.of(0,10, Sort.by("leagueId"))))).thenReturn(Page.of)
        matchService.findAllResponses(PageRequest.of(0,10, Sort.by("leagueId")));

    }


}
