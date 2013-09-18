package com.example.android.opengl;

public class GlobalState {
    public GameState state  = GameState.MAIN_MENU_OPENING;
    boolean hintsOn = true;
    boolean resumeGame = false;
    int difficulty = 0;
    
    public GlobalState() {
    }
}

