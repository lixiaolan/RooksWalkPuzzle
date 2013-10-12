package com.seventhharmonic.android.freebeeline;

public class GlobalState {
    public GameState state  = GameState.MAIN_MENU_OPENING;
    public GameType gametype;

    boolean hintsOn = true;
    boolean linesOn = true;
    boolean resumeGameExists = false;
    boolean saveCurrGame = false;
    boolean showPathToggle = false;
    boolean showGameBanner = false;
    boolean firstRun = false;
    boolean textureCreationFinished = true;
    boolean cheating = false;
    boolean ruleCheck = true;
    int difficulty = 0;
   
    public GlobalState() {
    }
}

