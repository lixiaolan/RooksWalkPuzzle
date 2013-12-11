package com.seventhharmonic.android.freebeeline.physics;

import android.util.Log;

import com.seventhharmonic.android.freebeeline.*;
import com.seventhharmonic.android.freebeeline.StateWidget.DrawPeriod;
import com.seventhharmonic.android.freebeeline.util.LATools;
import com.seventhharmonic.com.freebeeline.levelresources.*;
import com.seventhharmonic.android.freebeeline.listeners.*;
import com.seventhharmonic.com.freebeeline.levelresources.LevelPack;
import com.seventhharmonic.com.freebeeline.levelresources.LevelPackProvider;
import java.util.HashMap;
import java.util.Map;

public class Physics implements PhysicsInterface {
    
    //This is the internal state system this class will use:
    //MAIN_MENU: Opening game menu
    //BOOK_SELECT: Selecting a puzzle book (uses two overlayed widgits);
    //CHAPTER_SELECT: The state that used to be handled by Table of Contents.

    //The enum and its member

    private FlowerTile[] tiles;

    private Map <String, StateWidget> str2physics = new HashMap<String, StateWidget>();

    StateWidget state;

    public Physics(FlowerTile[] t) {
	tiles = t;

	//These need to go here:
	str2physics.put("main_menu", new FlowerMainMenu(tiles));
	str2physics.put("default", new LevelPack1(tiles));
	str2physics.put("lp2", new LevelPack2(tiles));
	str2physics.put("lp3", new LevelPack3(tiles));
	state = str2physics.get("main_menu");
    }

    @Override
    public void swipeHandler(String direction) {
	state.swipeHandler(direction);
    }
    @Override
    public void touchHandler(float[] pt) {
	state.touchHandler(pt);
    }
    @Override
    public void draw(MyGLRenderer r) {
	state.draw(r);
    }
    @Override
    public void setPhysics(String s) {
	if (str2physics.get(s) != null) {
	    state = str2physics.get(s);
	}
    }
    @Override
    public void resetPhysics(){
	for (StateWidget s : str2physics.values()) {
	    s.reset();
	}
    }

}
