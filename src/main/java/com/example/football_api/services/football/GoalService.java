package com.example.football_api.services.football;

import com.example.football_api.dto.football.request.GoalRequest;
import com.example.football_api.dto.football.response.GoalResponse;
import com.example.football_api.entities.football.Goal;
import com.example.football_api.entities.football.Match;
import com.example.football_api.entities.football.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GoalService {
    GoalResponse findGoalById(Long id);
    GoalResponse saveGoal(GoalRequest goalRequest);
    GoalResponse updateGoal(Long goalId, GoalRequest updateInfo);
    GoalResponse deleteGoal(Long id);
    List<GoalResponse> findGoalsByMatchId(Long matchId);
    Page<GoalResponse> findAllGoals(Pageable pageable);
}
