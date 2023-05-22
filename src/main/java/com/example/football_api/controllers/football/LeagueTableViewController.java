package com.example.football_api.controllers.football;

import com.example.football_api.entities.football.LeagueTableView;
import com.example.football_api.services.football.LeagueTableViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/leagues-view")
@RequiredArgsConstructor
public class LeagueTableViewController {
    private final LeagueTableViewService leagueTableViewService;
    @GetMapping("/{id}")
    public ResponseEntity<List<LeagueTableView>> getLeagueView(@PathVariable Long id){
        return ResponseEntity.ok(leagueTableViewService.getLeagueTableViewByLeagueId(id));
    }
}
