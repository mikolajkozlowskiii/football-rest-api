package com.example.football_api.controllers.football;

import com.example.football_api.dto.football.request.TeamRequest;
import com.example.football_api.dto.football.response.LeagueResponse;
import com.example.football_api.dto.football.response.TeamResponse;
import com.example.football_api.entities.football.Team;
import com.example.football_api.services.football.TeamService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;
    @GetMapping("/{id}")
    public ResponseEntity<TeamResponse> findTeamById(@PathVariable Long id){
        return ResponseEntity.ok(teamService.findById(id));
    }

    @PostMapping
    public ResponseEntity<TeamResponse> save(@Valid @RequestBody TeamRequest teamRequest){
        return ResponseEntity.ok(teamService.save(teamRequest));
    }

    @PutMapping
    public ResponseEntity<TeamResponse> update(@Valid @RequestBody TeamRequest teamRequest){
        // TODO
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TeamResponse> delete(@PathVariable Long id){
        return ResponseEntity.ok(teamService.deleteTeamById(id));
    }

    @GetMapping("/{id}/leagues")
    public ResponseEntity<List<LeagueResponse>> getLeagues(@PathVariable Long id){
        return ResponseEntity.ok(teamService.getAllTeamLeagues(id));
    }

}