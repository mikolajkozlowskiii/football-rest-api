package com.example.football_api.controllers.football;

import com.example.football_api.dto.football.request.GoalRequest;
import com.example.football_api.dto.football.response.GoalResponse;
import com.example.football_api.dto.football.response.GoalShortResponse;
import com.example.football_api.services.football.GoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/goals")
@RequiredArgsConstructor
public class GoalController {
    private final GoalService goalService;
    @GetMapping("/{id}")
    public ResponseEntity<GoalResponse> findGoalResponseById(@PathVariable Long id){
        return ResponseEntity.ok(goalService.findGoalResponseById(id));
    }

    @PostMapping()
    public ResponseEntity<GoalResponse> saveGoal(@RequestBody @Valid GoalRequest goalRequest){
        GoalResponse createdGoal = goalService.saveGoal(goalRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/goals/{id}")
                .buildAndExpand(createdGoal.getId()).toUri();

        return ResponseEntity.created(location).body(createdGoal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GoalResponse> updateGoal(@PathVariable Long id,
                                                   @RequestBody @Valid GoalRequest goalRequest){
        return ResponseEntity.ok(goalService.updateGoal(id, goalRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GoalResponse> deleteGoal(@PathVariable Long id){
        return ResponseEntity.ok(goalService.deleteGoal(id));
    }

    @GetMapping("/matches/{matchId}")
    public ResponseEntity<List<GoalResponse>> findGoalsByMatchId(@PathVariable Long matchId){
        return ResponseEntity.ok(goalService.findGoalsResponsesByMatchId(matchId));
    }

    @GetMapping
    public ResponseEntity<Page<GoalShortResponse>> findAllGoals(@RequestParam(defaultValue = "0") Integer pageNo,
                                                                @RequestParam(defaultValue = "4") Integer pageSize,
                                                                @RequestParam(defaultValue = "id") String sortBy){
        return ResponseEntity.ok(goalService.findAllGoals(PageRequest.of(pageNo, pageSize, Sort.by(sortBy))));
    }

}
