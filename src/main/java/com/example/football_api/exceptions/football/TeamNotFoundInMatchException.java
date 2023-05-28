package com.example.football_api.exceptions.football;

import java.time.LocalDate;

public class TeamNotFoundInMatchException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public TeamNotFoundInMatchException(Long teamId, Long matchId){
        super("Team id: " + teamId +" not found in match id: " + matchId);
    }
}
