package com.example.android.opengl;

public class GlobalState {
    public GameState state  = GameState.MAIN_MENU_OPENING;
    boolean hintsOn = true;
    boolean linesOn = true;
    boolean resumeGameExists = false;
    boolean saveCurrGame = false;
    boolean showPathToggle = false;
    boolean showGameBanner = false;
    boolean firstRun = false;
    boolean textureCreationFinished = true;
    int difficulty = 0;
   
    public GlobalState() {
    }
}

