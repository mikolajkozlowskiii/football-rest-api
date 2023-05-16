package com.example.football_api.controllers;

import com.example.football_api.dto.users.response.ApiResponse;
import com.example.football_api.entities.LeagueFollow;
import com.example.football_api.entities.football.League;
import com.example.football_api.security.userDetails.CurrentUser;
import com.example.football_api.security.userDetails.UserDetailsImpl;
import com.example.football_api.services.football.LeagueFollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/followed-leagues")
@RequiredArgsConstructor
public class LeagueFollowController {
    private final LeagueFollowService leagueFollowService;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Set<Long>> getUsersFollowedLeaguesIds( @CurrentUser UserDetailsImpl currentUser){
        System.out.println("cokolwiek");
        System.out.println(currentUser);
        System.out.println("id: "+currentUser.getId());
        return ResponseEntity.ok(leagueFollowService.findAllUsersFollowedLeaguesIds(currentUser.getId()));
    }

    @PutMapping("/{leagueId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> followLeague(@PathVariable(value = "leagueId") Long leagueId,
                                                    @CurrentUser UserDetailsImpl currentUser){
        final LeagueFollow leagueFollow = leagueFollowService.followLeague(leagueId, currentUser.getId());
        final League followedLeague = leagueFollow.getLeague();
        ApiResponse apiResponse = new ApiResponse(
                Boolean.TRUE, "League id:%d has been followed by %s".formatted(followedLeague.getId(), currentUser.getEmail())
        );
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{leagueId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> unFollowLeague(@PathVariable(value = "leagueId") Long leagueId,
                                                    @CurrentUser UserDetailsImpl currentUser){
        leagueFollowService.unFollowLeague(leagueId, currentUser.getId());
        ApiResponse apiResponse = new ApiResponse(
                Boolean.TRUE, "League id:%d has been unollowed by %s".formatted(leagueId, currentUser.getEmail())
        );
        return ResponseEntity.ok(apiResponse);
    }
}
