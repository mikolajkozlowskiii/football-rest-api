package com.example.football_api.controllers.football;

import com.example.football_api.dto.football.request.PlayerRequest;
import com.example.football_api.dto.football.response.PlayerResponse;
import com.example.football_api.services.football.PlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/players")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;

    @GetMapping("/{id}")
    public ResponseEntity<PlayerResponse> getPlayerById(@PathVariable Long id){
        return ResponseEntity.ok(playerService.findPlayerResponseById(id));
    }

    @GetMapping("/teams/{teamId}")
    public ResponseEntity<List<PlayerResponse>> getPlayersByTeamId(@PathVariable  Long teamId){
        return ResponseEntity.ok(playerService.findByTeamIdPlayerResponses(teamId));
    }

    @PostMapping
    public ResponseEntity<PlayerResponse> savePlayer(@RequestBody @Valid PlayerRequest playerRequest){
        PlayerResponse createdPlayer = playerService.create(playerRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/players/{id}")
                .buildAndExpand(createdPlayer.getId()).toUri();

        return ResponseEntity.created(location).body(createdPlayer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerResponse> updatePlayer(@PathVariable Long id,
                                                       @RequestBody @Valid PlayerRequest playerRequest){
        return ResponseEntity.ok(playerService.updatePlayer(id, playerRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PlayerResponse> deletePlayer(@PathVariable Long id){
        // TODO add role permission
        return ResponseEntity.ok(playerService.deletePlayer(id));
    }



}
