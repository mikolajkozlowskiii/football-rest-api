package com.example.football_api.services.football.impl;

import com.example.football_api.dto.football.request.GoalRequest;
import com.example.football_api.dto.football.response.GoalResponse;
import com.example.football_api.repositories.football.GoalRepository;
import com.example.football_api.services.football.GoalService;
import com.example.football_api.services.football.MatchService;
import com.example.football_api.services.football.PlayerService;
import com.example.football_api.services.football.mappers.GoalMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {
    private final GoalRepository goalRepository;
    private final PlayerService playerService;
    private final MatchService matchService;
    private final GoalMapper goalMapper;

    @Override
    public GoalResponse findGoalById(Long id) {
        return null;
    }

    @Override
    public GoalResponse saveGoal(GoalRequest goalRequest) {
        return null;
    }

    @Override
    public GoalResponse updateGoal(Long goalId, GoalRequest updateInfo) {
        return null;
    }

    @Override
    public GoalResponse deleteGoal(Long id) {
        return null;
    }

    @Override
    public List<GoalResponse> findGoalsByMatchId(Long matchId) {
        return null;
    }

    @Override
    public Page<GoalResponse> findAllGoals(Pageable pageable) {
        return null;
    }
}
