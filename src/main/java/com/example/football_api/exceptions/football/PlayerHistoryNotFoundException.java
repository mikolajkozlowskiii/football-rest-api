package com.example.football_api.exceptions.football;

import java.time.LocalDate;

public class PlayerHistoryNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public PlayerHistoryNotFoundException(Long playerId, LocalDate date){
        super("Player id:%d hasn't got any history of employment in any team for date %s".formatted(playerId, date));
    }
}
