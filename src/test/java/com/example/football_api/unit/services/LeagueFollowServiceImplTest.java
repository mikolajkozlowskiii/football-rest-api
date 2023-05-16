package com.example.football_api.unit.services;

import com.example.football_api.entities.LeagueFollow;
import com.example.football_api.entities.football.League;
import com.example.football_api.entities.users.User;
import com.example.football_api.exceptions.football.LeagueNotFoundException;
import com.example.football_api.exceptions.users.UserNotFoundException;
import com.example.football_api.repositories.LeagueFollowRepository;
import com.example.football_api.security.userDetails.UserDetailsImpl;
import com.example.football_api.services.football.LeagueService;
import com.example.football_api.services.football.impl.LeagueFollowServiceImpl;
import com.example.football_api.services.users.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class LeagueFollowServiceImplTest {
    @InjectMocks
    private LeagueFollowServiceImpl leagueFollowService;
    @Mock
    private LeagueService leagueService;
    @Mock
    private UserService userService;
    @Mock
    private LeagueFollowRepository leagueFollowRepository;

    @Test
    public void followLeague_UserAndLeagueExistsInDB_FollowGiven(){
        final User currentUser = User.builder().id(1L).build();
        final League league = League.builder().id(1L).name("La Liga").season("2022/23").country("ESP").build();
        final LeagueFollow leagueFollow = LeagueFollow.builder().league(league).user(currentUser).build();

        when(userService.findUserById(1L)).thenReturn(currentUser);
        when(leagueService.findLeagueById(1L)).thenReturn(league);
        when(leagueFollowRepository.save(leagueFollow))
                .thenReturn(leagueFollow);

        LeagueFollow expectedLeagueFollow = LeagueFollow.builder().league(league).user(currentUser).build();
        LeagueFollow actualLeagueFollow = leagueFollowService.followLeague(1L, 1L);

        Assertions.assertEquals(expectedLeagueFollow, actualLeagueFollow);
    }

    @Test
    public void followLeague_UserNotExistsInDB_FollowGiven(){
        when(userService.findUserById(1L)).thenThrow(new UserNotFoundException("mikolajkozlowskiii@gmail.com"));

        Assertions.assertThrows(UserNotFoundException.class, () -> leagueFollowService.followLeague(1L, 1L));
    }

    @Test
    public void followLeague_LeagueNotExistsInDB_FollowGiven(){
        final User currentUser = User.builder().id(1L).build();

        when(userService.findUserById(1L)).thenReturn(currentUser);
        when(leagueService.findLeagueById(1L)).thenThrow(new LeagueNotFoundException("La Liga"));

        Assertions.assertThrows(LeagueNotFoundException.class, () -> leagueFollowService.followLeague(1L, 1L));
    }

    @Test
    public void unFollowLeague_UserAndLeagueExistsInDB_FollowTaken(){
        final User currentUser = User.builder().id(1L).build();
        final League league = League.builder().id(1L).name("La Liga").season("2022/23").country("ESP").build();
        final LeagueFollow leagueFollow = LeagueFollow.builder().league(league).user(currentUser).build();

        when(userService.findUserById(1L)).thenReturn(currentUser);
        when(leagueService.findLeagueById(1L)).thenReturn(league);

        leagueFollowService.unFollowLeague(1L, 1L);
        verify(leagueFollowRepository).delete(leagueFollow);
    }

    @Test
    public void unFollowLeague_UserNotExistsInDB_FollowTaken(){
        final League league = League.builder().id(1L).name("La Liga").season("2022/23").country("ESP").build();

        when(userService.findUserById(1L)).thenThrow(new UserNotFoundException("mikolajkozlowskiii@gmail.com"));

        Assertions.assertThrows(UserNotFoundException.class, () -> leagueFollowService.unFollowLeague(1L, 1L));
    }

    @Test
    public void unFollowLeague_LeagueNotExistsInDB_FollowTaken(){
        final User currentUser = User.builder().id(1L).build();

        when(userService.findUserById(1L)).thenReturn(currentUser);
        when(leagueService.findLeagueById(1L)).thenThrow(new LeagueNotFoundException("La Liga"));

        Assertions.assertThrows(LeagueNotFoundException.class, () -> leagueFollowService.unFollowLeague(1L, 1L));
    }

    @Test
    public void getUsersFollowedLeagues_UserFollowedSomeLeagues_ReturnsLeaguesIds(){
        when(leagueFollowRepository.findByUserIdFollowedLeaguesId(1L)).thenReturn(Set.of(1L, 2L));
        Set<Long> expectedLeagueIds = Set.of(1L, 2L);
        Set<Long> actualLeagueIds = leagueFollowService.findAllUsersFollowedLeaguesIds(1L);

        Assertions.assertEquals(expectedLeagueIds, actualLeagueIds);
    }

    @Test
    public void getUsersFollowedLeagues_UserNotFollowedAnyLeagues_ReturnsEmptySet(){
        when(leagueFollowRepository.findByUserIdFollowedLeaguesId(1L)).thenReturn(Collections.emptySet());
        Set<Long> expectedLeagueIds = Collections.emptySet();
        Set<Long> actualLeagueIds = leagueFollowService.findAllUsersFollowedLeaguesIds(1L);

        Assertions.assertEquals(expectedLeagueIds, actualLeagueIds);
    }

}
