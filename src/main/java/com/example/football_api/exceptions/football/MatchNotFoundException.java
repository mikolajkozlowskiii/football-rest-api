package com.example.football_api.exceptions.football;

public class MatchNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public MatchNotFoundException(Long id){super("Match id" + id +" not found!");}
}
