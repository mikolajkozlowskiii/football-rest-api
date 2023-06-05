package com.example.football_api.controllers.football;

import com.example.football_api.dto.football.request.MatchRequest;
import com.example.football_api.dto.football.response.MatchResponse;
import com.example.football_api.dto.users.response.ApiResponse;
import com.example.football_api.services.football.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/matches")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;

    @GetMapping("/{id}")
    public ResponseEntity<MatchResponse> findMatchById(@PathVariable Long id){
        return ResponseEntity.ok(matchService.findMatchResponseById(id));
    }

    @GetMapping("/league/{leagueId}/team/{teamId}")
    public ResponseEntity<List<MatchResponse>> findMatchesById(@PathVariable(name = "leagueId") Long leagueId,
                                                             @PathVariable(name = "teamId") Long teamId){
        return ResponseEntity.ok(matchService.findMatchesResponseByLeagueIdAndTeamId(leagueId, teamId));
    }

    @GetMapping("/today")
    public ResponseEntity<List<MatchResponse>> findTodaysMatches(){
        return ResponseEntity.ok(matchService.findAllByDateTodayMatchesResponse());
    }

    @GetMapping("/date/{data}")
    public ResponseEntity<List<MatchResponse>> findMatchesByDate(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate data
    ){
        return ResponseEntity.ok(matchService.findAllByDate(data));
    }

    @GetMapping()
    public ResponseEntity<Page<MatchResponse>> searchMatches(@RequestParam(defaultValue = "0") Integer pageNo,
                                                             @RequestParam(defaultValue = "4") Integer pageSize,
                                                             @RequestParam(defaultValue = "date") String sortBy){
        return ResponseEntity.ok(matchService.findAllResponses(PageRequest.of(pageNo, pageSize, Sort.by(sortBy))));
    }

    @GetMapping("/overview")
    public ResponseEntity<Page<MatchResponse>> searchMatchesOverview(@RequestParam(defaultValue = "0") Integer pageNo,
                                                             @RequestParam(defaultValue = "4") Integer pageSize,
                                                             @RequestParam(defaultValue = "date") String sortBy){
        return ResponseEntity.ok(matchService.findAllResponses(PageRequest.of(pageNo, pageSize, Sort.by(sortBy))));
    }

    @PostMapping()
    public ResponseEntity<MatchResponse> saveMatch(@Valid @RequestBody MatchRequest matchRequest){
        MatchResponse createdMatch = matchService.save(matchRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/matches/{id}")
                .buildAndExpand(createdMatch.getId()).toUri();

        return ResponseEntity.created(location).body(createdMatch);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MatchResponse> saveMatch(@PathVariable Long id,
                                                   @Valid @RequestBody MatchRequest matchRequest){
        MatchResponse updatedMatch = matchService.update(id, matchRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/matches/{id}")
                .buildAndExpand(updatedMatch.getId()).toUri();

        return ResponseEntity.created(location).body(updatedMatch);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> saveMatch(@PathVariable Long id){
        matchService.deleteById(id);
        final ApiResponse apiResponse = new ApiResponse(Boolean.TRUE, "Match id: " + id +" deleted.");
        return ResponseEntity.ok(apiResponse);
    }
}
