package com.example.football_api.controllers.football;

import com.example.football_api.dto.football.validation.LeagueRequest;
import com.example.football_api.dto.football.validation.TeamRequest;
import com.example.football_api.dto.football.response.LeagueResponse;
import com.example.football_api.exceptions.validators.ValidList;
import com.example.football_api.services.football.LeagueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Set;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/leagues")
@RequiredArgsConstructor
public class LeagueController {
    private final LeagueService leagueService;

    @GetMapping("/{id}")
    public ResponseEntity<LeagueResponse> findLeagueById(@PathVariable Long id){
        return ResponseEntity.ok(leagueService.findResponseById(id));
    }

    @PostMapping
    public ResponseEntity<LeagueResponse> createLeague(@Valid @RequestBody LeagueRequest leagueRequest){
        LeagueResponse createdLeague = leagueService.saveNewLeague(leagueRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/leagues/{id}")
                .buildAndExpand(createdLeague.getId()).toUri();

        return ResponseEntity.created(location).body(createdLeague);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeagueResponse> updateLeagueById(@PathVariable Long id,
                                                           @Valid @RequestBody LeagueRequest leagueInfoRequest){
        return ResponseEntity.ok(leagueService.update(id, leagueInfoRequest));
    }

    @PutMapping("/{id}/teams")
    public ResponseEntity<LeagueResponse> addTeams(@PathVariable Long id,
                                                   @Valid @RequestBody ValidList<TeamRequest> teams){
        System.out.println("dzieje sie");
        LeagueResponse leagueResponse = leagueService.addTeamsToLeague(id, teams);
        return ResponseEntity.ok(leagueResponse);
    }

    @DeleteMapping("/{id}/teams")
    public ResponseEntity<LeagueResponse> removeTeams(@PathVariable Long id,
                                                      @Valid @RequestBody List<TeamRequest> teams){
        LeagueResponse leagueResponse = leagueService.removeTeamsFromLeague(id, teams);
        return ResponseEntity.ok(leagueResponse);
    }

    @PutMapping("/{id}/teams/ids")
    public ResponseEntity<LeagueResponse> addTeamsById(@PathVariable Long id,
                                                       @Valid @RequestBody Set<Long> teamsId){
        LeagueResponse leagueResponse = leagueService.addTeamsByIdsToLeague(id, teamsId);
        return ResponseEntity.ok(leagueResponse);
    }

    @DeleteMapping("/{id}/teams/ids")
    public ResponseEntity<LeagueResponse> removeTeamsById(@PathVariable Long id,
                                                          @RequestBody Set<Long> teamsId){
        LeagueResponse leagueResponse = leagueService.removeTeamsByIdsFromLeague(id, teamsId);
        return ResponseEntity.ok(leagueResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<LeagueResponse> deleteLeagueById(@PathVariable Long id){
        return ResponseEntity.ok(leagueService.delete(id));
    }

    @GetMapping()
    public ResponseEntity<List<LeagueResponse>> findLeagueByNameAndCountryAndSeason(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "season", required = false) String season,
            @RequestParam(value = "country", required = false) String country,
            @RequestParam(value = "official", required = false) boolean isOfficial
    ){
        List<LeagueResponse> leagues = leagueService.searchLeaguesByNameSeasonCountryOfficial(name, season, country, isOfficial);

        return ResponseEntity.ok(leagues);
    }

    @PutMapping("/{id}/official")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<LeagueResponse> setOfficialLeagueStatus(@PathVariable Long id){
        LeagueResponse leagueResponse = leagueService.setOfficiallLeagueStatus(id);

        return ResponseEntity.ok(leagueResponse);
    }
}
