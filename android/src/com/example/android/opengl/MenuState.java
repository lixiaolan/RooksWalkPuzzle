package com.example.android.opengl;

import android.os.Parcel;
import android.os.Parcelable;

class MenuState implements Parcelable{

    public boolean hints = true;
    public int difficulty = -1;
    public MenuStateEnum state = MenuStateEnum.OPENING;  

    MenuState() {}

    MenuState(Parcel in){
    	difficulty = in.readInt();
    	state = (MenuStateEnum)in.readSerializable();
    	
    }
    
    public int describeContents() {
    	return 0;
    }
    
    public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(difficulty);
		dest.writeSerializable(state);
	}
    
    public static final Parcelable.Creator<MenuState> CREATOR = new Parcelable.Creator<MenuState>() {
   	 
        @Override
        public MenuState createFromParcel(Parcel source) {
            return new MenuState(source); // RECREATE VENUE GIVEN SOURCE
        }
 
        @Override
        public MenuState[] newArray(int size) {
            return new MenuState[size]; // CREATING AN ARRAY OF VENUES
        }
 
    };
    
};

