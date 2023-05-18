package com.example.football_api.exceptions.football;

public class LeagueNotContainTeamException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public LeagueNotContainTeamException(Long leagueId, Long teamId){
        super("In league id:%d there is no team id:%d".formatted(leagueId, teamId));
    }
}
