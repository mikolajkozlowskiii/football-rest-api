package com.example.football_api.services.football.impl;


import com.example.football_api.entities.LeagueFollow;
import com.example.football_api.entities.football.League;
import com.example.football_api.entities.users.User;
import com.example.football_api.repositories.LeagueFollowRepository;
import com.example.football_api.services.football.LeagueFollowService;
import com.example.football_api.services.football.LeagueService;
import com.example.football_api.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class LeagueFollowServiceImpl implements LeagueFollowService {
    private final LeagueService leagueService;
    private final UserService userService;
    private final LeagueFollowRepository leagueFollowRepository;

    @Override
    public Set<Long> findAllUsersFollowedLeaguesIds(Long userId) {
        return leagueFollowRepository.findByUserIdFollowedLeaguesId(userId);
    }

    @Override
    public LeagueFollow followLeague(Long leagueId, Long userId) {
        LeagueFollow leagueFollow = getFollowLeagueEntity(leagueId, userId);

        return leagueFollowRepository.save(leagueFollow);
    }

    private LeagueFollow getFollowLeagueEntity(Long leagueId, Long userId) {
        User user = userService.findUserById(userId);
        League league = leagueService.findLeagueById(leagueId);

        return  LeagueFollow.builder()
                .user(user)
                .league(league)
                .build();
    }

    @Override
    public void unFollowLeague(Long leagueId, Long userId) {
        //LeagueFollow leagueFollow = getFollowLeagueEntity(leagueId, userId);
        System.out.println("123");
        leagueFollowRepository.deleteByLeagueId(leagueId);
        System.out.println("1234");
    }
}
