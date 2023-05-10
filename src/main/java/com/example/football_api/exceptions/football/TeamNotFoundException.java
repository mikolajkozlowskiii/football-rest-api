package com.example.football_api.exceptions.football;

public class TeamNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public TeamNotFoundException(Long id){super("Team " + id +" not found!");}
    public TeamNotFoundException(String name){super("Team " + name +" not found!");}
}


