package com.example.football_api.exceptions.football;

import java.time.LocalDate;

public class TeamAlreadyExistsException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public TeamAlreadyExistsException(String teamName){super("Team " + teamName +" already exists in DB.");}

}
