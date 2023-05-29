package com.example.football_api.controllers.football;

import com.example.football_api.dto.football.request.PlayerTeamHistoryRequest;
import com.example.football_api.dto.football.response.PlayerTeamHistoryResponse;
import com.example.football_api.services.football.PlayerTeamHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/contracts")
@RequiredArgsConstructor
public class PlayerTeamHistoryController {
    private final PlayerTeamHistoryService playerTeamHistoryService;

    @GetMapping("/{id}")
    public ResponseEntity<PlayerTeamHistoryResponse> findPlayerTeamHistoryById(@PathVariable Long id){
        return ResponseEntity.ok(playerTeamHistoryService.findResponseById(id));
    }

    @PostMapping()
    public ResponseEntity<PlayerTeamHistoryResponse> save(@RequestBody PlayerTeamHistoryRequest request){
        return ResponseEntity.ok(playerTeamHistoryService.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerTeamHistoryResponse> update(@PathVariable Long id,
                                                    @RequestBody PlayerTeamHistoryRequest request){
        return ResponseEntity.ok(playerTeamHistoryService.updateResponse(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PlayerTeamHistoryResponse> delete(@PathVariable Long id){
        return ResponseEntity.ok(playerTeamHistoryService.deleteResponse(id));
    }
}
