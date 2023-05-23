package com.example.football_api.services.football.mappers;

import com.example.football_api.dto.football.request.GoalRequest;
import com.example.football_api.dto.football.response.GoalResponse;
import com.example.football_api.entities.football.Goal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GoalMapper {
    private final PlayerMapper playerMapper;
    private final MatchMapper matchMapper;
    public GoalResponse map(Goal goal){
        return GoalResponse.builder()
                .id(goal.getId())
                .isOwn(goal.isOwn())
                .matchResponse(matchMapper.map(goal.getMatch()))
                .playerResponse(playerMapper.map(goal.getPlayer()))
                .build();
    }

    public Goal map(GoalRequest goalRequest){
        return Goal.builder()
                .isOwn(goalRequest.isOwn())
                .build();
    }

}
