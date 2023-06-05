package com.example.football_api.services.football;

import com.example.football_api.dto.football.request.MatchRequest;
import com.example.football_api.dto.football.response.MatchResponse;
import com.example.football_api.entities.football.Match;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface MatchService {
     MatchResponse save(MatchRequest match);
     Match saveEntity(MatchRequest matchRequest);
     Page<MatchResponse> findAllResponses(Pageable pageable);
     Page<Match> findAll(Pageable pageable);
     List<MatchResponse> findMatchesResponseByLeagueIdAndTeamId(Long leagueId, Long teamId);
     List<Match> findByLeagueIdAndTeamId(Long leagueId, Long teamId);
     Page<Match> findByLeagueIdAndTeamId(Long leagueId, Long teamId, Pageable pageable);
     List<Match> findByLeagueId(Long leagueId);
     List<Match> findByLeagueId(Long leagueId, Pageable pageable);
     List<MatchResponse> findAllByDateTodayMatchesResponse();
     List<MatchResponse> findAllByDate(LocalDate date);
     List<Match> findAllByDateToday();
     List<Match> findAllByDateToday(Pageable pageable);
     MatchResponse findMatchResponseById(Long matchId);
     Match findMatchById(Long matchId);
     MatchResponse update(Long matchId, MatchRequest matchRequest);
     void deleteById(Long matchId);
}
