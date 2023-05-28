package com.example.football_api.controllers.football;

import com.example.football_api.dto.football.response.overview.MatchOverview;
import com.example.football_api.services.football.MatchOverviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/matches-view")
@RequiredArgsConstructor
public class MatchOverviewController {
    private final MatchOverviewService matchOverviewService;

    @GetMapping("/matches/{matchId}")
    public ResponseEntity<MatchOverview> getMatchOverviewByMatchId(@PathVariable Long matchId){
        return ResponseEntity.ok(matchOverviewService.getMatchOverviewByMatchId(matchId));
    }

    @GetMapping("/leagues/{leagueId}")
    public ResponseEntity<Page<MatchOverview>> getMatchesOverviewByLeagueId(@PathVariable Long leagueId,
                                                                            @RequestParam(defaultValue = "0") Integer pageNo,
                                                                            @RequestParam(defaultValue = "4") Integer pageSize,
                                                                            @RequestParam(defaultValue = "date") String sortBy){
        return ResponseEntity.ok(matchOverviewService.getMatchesOverviewByLeagueId(leagueId, PageRequest.of(pageNo, pageSize, Sort.by(sortBy))));
    }

    @GetMapping("/today")
    public ResponseEntity<Page<MatchOverview>> getAllTodaysMatches(@RequestParam(defaultValue = "0") Integer pageNo,
                                                                   @RequestParam(defaultValue = "4") Integer pageSize,
                                                                   @RequestParam(defaultValue = "date") String sortBy){
        return ResponseEntity.ok(matchOverviewService.getAllTodayMatches(PageRequest.of(pageNo, pageSize, Sort.by(sortBy))));
    }

    @GetMapping("/leagues/{leagueId}/teams/{teamId}")
    public ResponseEntity<Page<MatchOverview>> getAllTodaysMatches(@PathVariable(name = "leagueId") Long leagueId,
                                                                   @PathVariable(name = "teamId") Long teamId,
                                                                   @RequestParam(defaultValue = "0") Integer pageNo,
                                                                   @RequestParam(defaultValue = "4") Integer pageSize,
                                                                   @RequestParam(defaultValue = "date") String sortBy){
        return ResponseEntity.ok(matchOverviewService.getMatchesOverviewByLeagueIdAndTeamId(
                leagueId, teamId, PageRequest.of(pageNo, pageSize, Sort.by(sortBy))));
    }

}
