package com.example.football_api.exceptions.football;

public class SavedGoalsInconsistencyViolationException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public SavedGoalsInconsistencyViolationException(int requestedGoals, int savedGoals){
        super("There is already " + savedGoals + " saved goals in DB. You can't request for " + requestedGoals + " before delete some of them.");
    }
}
