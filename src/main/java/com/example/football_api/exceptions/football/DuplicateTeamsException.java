package com.example.football_api.exceptions.football;

public class DuplicateTeamsException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public DuplicateTeamsException(Long id){super("You cant build match entity with 2 same teams, id" + id);}
}
