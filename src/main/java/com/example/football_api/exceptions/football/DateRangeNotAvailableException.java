package com.example.football_api.exceptions.football;

import com.example.football_api.entities.football.League;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

public class DateRangeNotAvailableException extends RuntimeException{
    private static final long DuplicateLeagueException = 1L;
    public DateRangeNotAvailableException(LocalDate starts, LocalDate ends){
        super("There is actually record with date range beetween %s and %s".formatted(starts, ends));
    }
}
