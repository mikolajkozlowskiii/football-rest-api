package com.example.football_api.services.football.impl;

import com.example.football_api.dto.football.request.MatchRequest;
import com.example.football_api.dto.football.response.MatchResponse;
import com.example.football_api.entities.football.League;
import com.example.football_api.entities.football.Match;
import com.example.football_api.entities.football.Team;
import com.example.football_api.exceptions.SortByException;
import com.example.football_api.exceptions.football.LeagueNotContainTeamException;
import com.example.football_api.exceptions.football.MatchNotFoundException;
import com.example.football_api.repositories.football.MatchRepository;
import com.example.football_api.services.football.LeagueService;
import com.example.football_api.services.football.MatchService;
import com.example.football_api.services.football.TeamService;
import com.example.football_api.services.football.mappers.MatchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {
    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;
    private final LeagueService leagueService;
    private final TeamService teamService;
    @Override
    public MatchResponse save(MatchRequest matchRequest) {
        Match match = getMatchEntity(matchRequest);

        return matchMapper.map(save(match));
    }

    @Override
    public Match saveEntity(MatchRequest matchRequest) {
        Match match = getMatchEntity(matchRequest);

        return save(match);
    }

    private Match getMatchEntity(MatchRequest matchRequest) {
        final League league = leagueService.findLeagueById(matchRequest.getLeagueId());
        final Team homeTeam = teamService.findTeamById(matchRequest.getHomeTeamId());
        final Team awayTeam = teamService.findTeamById(matchRequest.getAwayTeamId());
        checkIfLeagueContainsTeams(matchRequest);

        Match match = Match.builder()
                .league(league)
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .homeTeamScore(matchRequest.getHomeTeamScore())
                .awayTeamScore(matchRequest.getAwayTeamScore())
                .date(matchRequest.getDate())
                .time(matchRequest.getTime())
                .build();
        return match;
    }

    private void checkIfLeagueContainsTeams(MatchRequest matchRequest) {
        final Long leagueId = matchRequest.getLeagueId();
        final Long homeTeamId = matchRequest.getHomeTeamId();
        final Long awayTeamId = matchRequest.getAwayTeamId();
        if(!teamService.isTeamInLeague(homeTeamId, leagueId)){
            throw new LeagueNotContainTeamException(leagueId, homeTeamId);
        }
        if(!teamService.isTeamInLeague(awayTeamId, leagueId)){
            throw new LeagueNotContainTeamException(leagueId, awayTeamId);
        }
    }

    private Match save(Match match) {
        return matchRepository.save(match);
    }

    @Override
    public Page<MatchResponse> findAll(Pageable pageable) {
        try{
            Page<Match> matches = matchRepository.findAll(pageable);
            return new PageImpl<MatchResponse>(matches.stream().map(matchMapper::map).toList());
        }catch (PropertyReferenceException ex){
            throw new SortByException(pageable.getSort().toString());
        }
    }

    @Override
    public List<MatchResponse> findByLeagueIdAndTeamId(Long leagueId, Long teamId) {
        List<Match> matches = matchRepository.findByLeagueIdAndTeamId(leagueId, teamId);
        return matches.stream().map(matchMapper::map).toList();
    }

    @Override
    public List<MatchResponse> findAllByDateToday() {
        List<Match> matches = matchRepository.findAllByDateToday();
        return matches.stream().map(matchMapper::map).toList();
    }

    @Override
    public List<MatchResponse> findAllByDate(LocalDate date) {
        List<Match> matches = matchRepository.findAllByDate(date);
        return matches.stream().map(matchMapper::map).toList();
    }

    @Override
    public MatchResponse findMatchResponseById(Long matchId) {
        Match match = findMatchByID(matchId);
        return matchMapper.map(match);
    }

    @Override
    public Match findMatchById(Long matchId) {
        return findMatchByID(matchId);
    }

    private Match findMatchByID(Long matchId) {
        return matchRepository
                .findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));
    }

    @Override
    public MatchResponse update(Long matchId, MatchRequest matchRequest) {
        Match match = getMatchEntity(matchRequest);
        match.setId(matchId);

        return matchMapper.map(save(match));
    }

    @Override
    public void deleteById(Long matchId){
        matchRepository.deleteById(matchId);
    }
}
