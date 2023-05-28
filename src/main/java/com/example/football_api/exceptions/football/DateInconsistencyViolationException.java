package com.example.football_api.exceptions.football;

import java.time.LocalDate;

public class DateInconsistencyViolationException extends RuntimeException{
    private static final long DateInconsistencyViolationException = 1L;
    public DateInconsistencyViolationException(LocalDate date){
        super("Requested date: " + date + " makes inconsistency violation due to players scorrers have diffrent teams.");
    }
}
