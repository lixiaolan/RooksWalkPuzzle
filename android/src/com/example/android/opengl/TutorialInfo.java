package com.example.android.opengl;

class TutorialInfo {
    
    int Counter;

    public TutorialInfo() {
    	Counter = -1;
    }

    static String[] banners = {"left_arrow+3", "right_arrow", "boob", "dylan", "boob", "dylan", "boob", "dylan", "boob", "dylan", "boob", "dylan"};
    static int[] activeTile = {4, 15, 17, 29, 27, 21, 19, 30, 12, 13, 7, 10};
    static String[] arrow = {"left_arrow", "right_arrow", "left_arrow", "none", "left_arrow", "right_arrow","left_arrow", "right_arrow","left_arrow", "right_arrow","left_arrow", "right_arrow"};
    static String[] number = {"3", "none", "3", "none", "3","none", "3","none", "3","none","3", "none", "3", "none", "3","none", "3","none", "3","none"};

    static String OneTileBanner = "Welcome to Bee-Line.";
    static String ShowPathBanner = "The goal of this game is to create a closed path.";
    
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

    public String getArrow(){
    	return arrow[Counter];
    }
    
    
   public String getNumber(){
	   return number[Counter];
   }
   
   public int getActiveTile(){
	   return activeTile[Counter];
   }
   
   public String getPreviousArrow(){
   	return arrow[Counter-1];
   }
   
   
  public String getPreviousNumber(){
	   return number[Counter-1];
  }
  
  public int getPreviousActiveTile(){
	   return activeTile[Counter-1];
  }
   
   public int getCounter(){
	   return Counter;
   }
   
   public void incrementCounter(){
	   Counter++;
   }
    
}




