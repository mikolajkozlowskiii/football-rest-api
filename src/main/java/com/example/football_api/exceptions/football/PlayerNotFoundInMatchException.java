package com.example.football_api.exceptions.football;

public class PlayerNotFoundInMatchException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public PlayerNotFoundInMatchException(Long playerId, Long matchId){
        super("Player id: " + playerId +" couldn't play in match id: " + matchId);
    }
}
