package com.example.football_api.unit.repositories;

import com.example.football_api.entities.football.Match;
import com.example.football_api.repositories.football.MatchRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

@DataJpaTest()
public class MatchRepositoryTest {
    @Autowired
    private MatchRepository matchRepository;

    @Test
    @Sql(value = "classpath:/import-matches.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByDate(){
        List<Long> expectedMatchesIds = List.of(1L);

        List<Match> actualMatches = matchRepository.findAllByDate(LocalDate.of(2023, 5, 8));
        System.out.println(actualMatches);
        List<Long> actualMatchesIds = actualMatches.stream().map(Match::getId).toList();

        Assertions.assertEquals(expectedMatchesIds, actualMatchesIds);
    }
    @Test
    @Sql(value = "classpath:/import-matches.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findByLeague(){
        List<Long> expectedMatchesIds = List.of(1L, 2L);

        List<Match> actualMatches = matchRepository.findByLeagueId(1L);
        List<Long> actualMatchesIds = actualMatches.stream().map(Match::getId).toList();

        Assertions.assertEquals(expectedMatchesIds, actualMatchesIds);
    }

    @Test
    @Sql(value = "classpath:/import-matches.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findByLeagueAndTeam(){
        List<Long> expectedMatchesIds = List.of(3L, 4L);

        List<Match> actualMatches = matchRepository.findByLeagueIdAndTeamId(2L, 3L);
        List<Long> actualMatchesIds = actualMatches.stream().map(Match::getId).toList();

        Assertions.assertEquals(expectedMatchesIds, actualMatchesIds);
    }

    @Test
    @Sql(value = "classpath:/import-matches.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAll_PageableParam(){
        List<Long> expectedMatchesIds = List.of(5L, 1L, 4L, 2L, 3L);

        Page<Match> actualMatches = matchRepository.findAll(PageRequest.of(0,10, Sort.by("dateTime")));
        List<Long> actualMatchesIds = actualMatches.stream().map(Match::getId).toList();

        System.out.println(actualMatchesIds);
        Assertions.assertEquals(expectedMatchesIds, actualMatchesIds);
    }
}
