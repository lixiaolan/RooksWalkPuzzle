package com.example.android.opengl;

class TutorialInfo {
    
    int Counter;

    public TutorialInfo() {
	Counter = -1;
    }

    static String[] banners = {"boob", "dylan", "boob", "dylan", "boob", "dylan", "boob", "dylan", "boob", "dylan", "boob", "dylan"};
    static int[] activeTile = {4, 15, 17, 29, 27, 21, 19, 30, 12, 13, 7, 10};
    //    static String[] arrow = {"left_arrow", "right_arrow", "left_arrow", "right_arrow", "left_arrow", "right_arrow"};
    //    static String[] number = {"none", "3", "none", "3", "none", "3"};

    static int[] solutionNumbers = {
	-1, -1, -1, 2, 1, -1, 
	-1, 3, 0, 0, 1, -1, 
	1, 1, -1, 2, 0, 2, 
	0, 2, 0, 2, -1, 0, 
	0, 0, -1, 1, 0, 2, 
	3,1,-1,-1,-1,-1 };
    

    static String[] solutionArrows = {
	"clear", "clear", "clear", "left_arrow", "down_arrow", "clear", 
	"clear", "up_arrow", "clear", "clear", "right_arrow", "clear", 
	"up_arrow", "right_arrow", "clear", "up_arrow", "clear", "left_arrow", 
	"clear", "left_arrow", "clear", "down_arrow", "clear", "clear", 
	"clear", "clear", "clear", "right_arrow", "clear", "down_arrow", 
	"right_arrow","down_arrow","clear","clear","clear","clear" };


    static String[] initialNumbers = {
	"clear", "clear", "clear", "2", "clear", "clear", 
	"clear", "clear", "clear", "clear", "clear", "clear", 
	"clear", "1", "clear", "2", "clear", "2", 
	"clear", "2", "clear", "clear", "clear", "clear", 
	"clear", "clear", "clear", "clear", "clear", "clear", 
	"clear","1","clear","clear","clear","clear"};

    static String[] initialArrows = {
	"clear", "clear", "clear", "left_arrow", "clear", "clear", 
	"clear", "clear", "clear", "clear", "clear", "clear", 
	"clear", "right_arrow", "clear", "clear", "clear", "left_arrow", 
	"clear", "clear", "clear", "clear", "clear", "clear", 
	"clear", "clear", "clear", "clear", "clear", "clear", 
	"clear","down_arrow","clear","clear","clear","clear"};

}




