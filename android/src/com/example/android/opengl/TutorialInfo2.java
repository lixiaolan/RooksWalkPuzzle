package com.example.android.opengl;

class TutorialInfo2 {
    

    public TutorialInfo2() {
    }

    //Boob Dylan
    static String[] banners = {"Create a loop and fill the board.",
    							"Touch to enter a number. Swipe to enter an arrow.",
    							"Always change directions.",
    							"Matching numbers can't point towards each other.",
    							"Your turn."
    							};
    
   
    
    
    
////////////////////////// SHOW_PATH    
   static int[][] path  = {{1,3},{4,3},{4,4},{2,4},{2,1},{1,1}};
    
   static int length = 6;
  
   static int[] solutionNumbers = {
	-1, -1, -1, -1, -1, -1, 
	-1, 1, 0, 2, -1, -1, 
	-1, 3, 0, 0, 2, -1, 
	-1, -1, -1, 0, 0, -1, 
	-1, -1, -1, 3, 1, -1, 
	-1,-1,-1,-1,-1,-1 };
        
    static String[] solutionArrows = {
	"clear",      "clear",      "clear", "clear",  "clear",  "clear", 
	"clear",      "left_arrow",   "clear", "down_arrow",       "clear", "clear", 
	"clear",   "up_arrow","clear", "clear",    "left_arrow",       "clear", 
	"clear",      "clear", "clear", "clear",  "clear",       "clear", 
	"clear",      "clear",      "clear", "right_arrow", "down_arrow",       "clear", 
	"clear","clear", "clear", "clear",       "clear",       "clear" };

    static String[] initialNumbers = {
    	"clear", "clear", "clear", "2", "clear", "clear", 
    	"clear", "clear", "clear", "clear", "clear", "clear", 
    	"clear", "1", "clear", "2", "clear", "clear", 
    	"clear", "2", "clear", "clear", "clear", "clear", 
    	"clear", "clear", "clear", "clear", "clear", "clear", 
    	"clear","1","clear","clear","clear","clear"};

    static String[] initialArrows = {
    	"clear", "clear", "clear", "left_arrow", "clear", "clear", 
    	"clear", "clear", "clear", "clear", "clear", "clear", 
    	"clear", "right_arrow", "clear", "up_arrow", "clear", "clear", 
    	"clear", "clear", "clear", "clear", "clear", "clear", 
    	"clear", "clear", "clear", "clear", "clear", "clear", 
    	"clear","down_arrow","clear","clear","clear","clear"};
//////////////////////////////////////    

    
///////////////////////////Slide 3   
    static int[] solutionNumbersSlide3 = {
    	-1, -1, -1, -1, -1, -1, 
    	-1, 0, 0, 0, -1, -1, 
    	-1, 0, 0, 0, 0, -1, 
    	-1, -1, -1, 0, 0, -1, 
    	-1, -1, -1, 0, 0, -1, 
    	-1,-1,-1,-1,-1,-1 };
            
    static String[] solutionArrowsSlide3 = {
    	"clear",      "clear",      "clear", "clear",  "clear",  "clear", 
    	"clear",      "clear",   "clear", "clear",       "clear", "clear", 
    	"clear",   "clear","clear", "clear",    "clear",       "clear", 
    	"clear",      "clear", "clear", "clear",  "clear",       "clear", 
    	"clear",      "clear",      "clear", "clear", "clear",       "clear", 
    	"clear","clear", "clear", "clear",       "clear",       "clear" };

    static String[] initialNumbersSlide3 = {
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear","clear","clear","clear","clear","clear"};

    static String[] initialArrowsSlide3 = {
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear","clear","clear","clear","clear","clear"};    
/////////////////////////////////////////////
///////////////////////////////////Play Game    
    
    static boolean[] clickable = {
    	true, true, true, true, true, true, 
    	true, true, true, true, true, true, 
    	true, true, true, true, true, true, 
    	true, true, true, true, true, true, 
    	true, true, true, true, true, true, 
    	true,true,true,true,true,true};

    
}




