package com.example.android.opengl;

import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Scanner;
import java.io.IOException;

class Model{
    public Board mBoard;
    public Menu mMenu;

    public Model() {
	mBoard = new Board();
	mMenu = new Menu();
    }

    public void touched(float[] pt) {
	float[] val = mBoard.touched(pt);
	mMenu.touched(val);
    }
    
    public void swiped(float[] pt, int direction) {
	mBoard.swiped(pt, direction);
    }

    public void animate() {
	mMenu.animate();
    }
}
