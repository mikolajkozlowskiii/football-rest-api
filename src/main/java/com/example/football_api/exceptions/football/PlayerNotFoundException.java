package com.example.football_api.exceptions.football;

public class PlayerNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public PlayerNotFoundException(Long id){super("Player " + id +" not found!");}
}
