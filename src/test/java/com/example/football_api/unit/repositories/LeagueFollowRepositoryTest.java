package com.example.football_api.unit.repositories;

import com.example.football_api.entities.LeagueFollow;
import com.example.football_api.entities.football.League;
import com.example.football_api.entities.users.User;
import com.example.football_api.repositories.LeagueFollowRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@DataJpaTest()
@Transactional(propagation = Propagation.NEVER)
class LeagueFollowRepositoryTest {
    @Autowired
    private LeagueFollowRepository leagueFollowRepository;

    @Test
    @Sql(value = "classpath:/import-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "classpath:/import-leagues.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "classpath:/import-leagues_follows.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "classpath:/delete-leagues_follows.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findUsersFollowedLeaguesIds_UserFollowedLeagues_ReturnsFollowedLeaguesIds() {
        Set<Long> expectedFollowedLeaguesId = Set.of(1L, 2L);
        Set<Long> actualFollowedLeaguesId = leagueFollowRepository.findByUserIdFollowedLeaguesId(1L);

        Assertions.assertEquals(expectedFollowedLeaguesId, actualFollowedLeaguesId);
    }

    @Test
    @Sql(value = "classpath:/import-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "classpath:/import-leagues.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "classpath:/import-leagues_follows.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "classpath:/delete-leagues_follows.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findUsersFollowedLeaguesIds_UserNotFollowedAnyLeague_ReturnsEmptyList() {
        Set<Long> expectedFollowedLeaguesId = Collections.emptySet();
        Set<Long> actualFollowedLeaguesId = leagueFollowRepository.findByUserIdFollowedLeaguesId(3L);

        Assertions.assertEquals(expectedFollowedLeaguesId, actualFollowedLeaguesId);
    }

    @Test
    @Sql(value = "classpath:/import-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "classpath:/import-leagues.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "classpath:/delete-leagues_follows.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void save_LeagueAndUserExistsInDB_LeagueFollowEntitySaved(){
        LeagueFollow leagueFollow = LeagueFollow.builder()
                .user(User.builder().id(1L).email("mikolajkozlowskiii@gmail.com").firstName("Mikolaj").lastName("Kozlowski").build())
                .league(League.builder().id(1L).name("Premier League").season("2022/23").country("ENG").build())
                .build();
        LeagueFollow savedFollow = leagueFollowRepository.save(leagueFollow);

        Assertions.assertNotNull(savedFollow);
    }

    @Test
    @Sql(value = "classpath:/import-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "classpath:/import-leagues.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "classpath:/import-leagues_follows.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "classpath:/delete-leagues_follows.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void delete_LeagueAndUserExistsInDB_LeagueFollowEntityRemoved(){
        LeagueFollow leagueFollow = LeagueFollow.builder()
                .id(1L)
                .user(User.builder().id(1L).email("mikolajkozlowskiii@gmail.com").firstName("Mikolaj").lastName("Kozlowski").build())
                .league(League.builder().id(1L).name("Premier League").season("2022/23").country("ENG").build())
                .build();


        leagueFollowRepository.delete(leagueFollow);
        Optional<LeagueFollow> expcetedLeagueFollow = Optional.empty();
        Optional<LeagueFollow> actualLeagueFollow = leagueFollowRepository.findById(1L);

        Assertions.assertEquals(expcetedLeagueFollow, actualLeagueFollow);
    }
}