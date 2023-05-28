package com.example.football_api.exceptions.football;

import java.time.LocalDate;

public class TooManyGoalsException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public TooManyGoalsException(Long matchId){super("Can't save more goals for your team in match id: " + matchId);}

}
