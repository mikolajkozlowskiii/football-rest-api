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
     Page<MatchResponse> findAll(Pageable pageable);
     List<MatchResponse> findByLeagueIdAndTeamId(Long leagueId, Long teamId);
     List<MatchResponse> findAllByDateToday();
     List<MatchResponse> findAllByDate(LocalDate date);
     MatchResponse findMatchResponseById(Long matchId);
     Match findMatchById(Long matchId);
     MatchResponse update(Long matchId, MatchRequest matchRequest);
     void deleteById(Long matchId);
}
