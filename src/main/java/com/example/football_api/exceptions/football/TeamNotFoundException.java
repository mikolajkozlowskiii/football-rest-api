package com.example.football_api.exceptions.football;

import java.time.LocalDate;

public class TeamNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public TeamNotFoundException(Long id){super("Team " + id +" not found!");}
    public TeamNotFoundException(Long playerId, LocalDate date){super("Player " + playerId +" is without team, at date " + date);}
    public TeamNotFoundException(String name){super("Team " + name +" not found!");}
}


