package com.example.football_api.exceptions.football;

public class MatchNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public MatchNotFoundException(Long id){super("Team " + id +" not found!");}
    public MatchNotFoundException(String name){super("Team " + name +" not found!");}
}
