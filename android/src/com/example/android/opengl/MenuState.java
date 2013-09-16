package com.example.android.opengl;

import android.os.Parcel;
import android.os.Parcelable;

class MenuState{

    public boolean hints = true;
    public boolean open = false;
    public int difficulty = -1;
    public MenuStateEnum state = MenuStateEnum.OPENING;  
    public boolean createGame = false; 
    MenuState() {}

   
    
}

