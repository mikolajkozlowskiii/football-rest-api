package com.example.football_api.exceptions.football;

public class GoalNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public GoalNotFoundException(Long id){super("Goal " + id +" not found!");}
}
