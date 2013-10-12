package com.example.android.opengl;

class TutorialInfo2 {
    

    public TutorialInfo2() {
    }

    //Boob Dylan
    static String[] banners = {"Create a loop and fill the board. The arrows tell you where to go and the numbers indicate how far.",
    							"Touch a square to enter a number. Swipe to enter an arrow.",
    							"There are two easy rules. Rule 1: Every segment of your path must change direction.",
    							"Rule 2: A number can't point to a square with the same number.",
    							"Your turn to solve a puzzle. The yellow tiles are hints. Press the check when done.",
    							"Good Job! You helped Beatrice visit these flowers! Now press quit and try a new puzzle! You can always return to the tutorial for a review. "
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
        	"clear", "2", "clear", "clear", "clear", "clear", 
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear","clear","clear","clear","clear","clear"};

    static String[] initialArrowsSlide3 = {
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear", "up_arrow", "clear", "clear", "clear", "clear", 
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear","clear","clear","clear","clear","clear"};    
/////////////////////////////////////////////
///////////////////////////////////Play Game    
    
    static int[][] pathPlayGame = {{4, 2}, {4,1},{1,1},{1,3},{4,3},{4,5},{0,5},{0,2}};
    
    static int lengthPlayGame = 8;
    
    static String[] playGameBanners = {"Your turn to solve a puzzle. The yellow tiles are hints to help you. Press the check when done.",
    									"Press the check when done",
    									"Remember, you have to turn!",
    									"You visited all the flowers! Good Job!"};
    
    static int[] solutionNumbersPlayGame = {
    	-1, -1, 3, 0, 0, 4,
    	-1, 3, 0, 2, -1, 0,
    	-1, 0, 0, 0, -1, 0,
    	-1, 0, 0, 0, -1, 0,
    	 -1, 1, 4, 3, 0, 2,
    	 -1, -1, -1, -1, -1, -1
    };
    
    static String[] solutionArrowsPlayGame = {
    	"clear", "clear", "up_arrow", "clear", "clear", "left_arrow",
    	"clear", "left_arrow", "clear", "down_arrow", "clear", "clear",
    	"clear", "clear", "clear", "clear", "clear", "clear",
    	"clear", "clear", "clear", "clear", "clear", "clear",
    	"clear", "up_arrow", "right_arrow", "right_arrow", "clear", "down_arrow",
    	"clear", "clear", "clear", "clear", "clear", "clear"
    };
    
    static String[] initialNumbersPlayGame = {
    	"clear", "clear", "clear", "clear", "clear", "4",
    	"clear", "clear", "clear", "2", "clear", "clear",
    	"clear", "clear", "clear", "clear", "clear", "clear",
    	"clear", "clear", "clear", "clear", "clear", "clear",
    	"clear", "1", "4", "clear", "clear", "clear",
    	"clear", "clear", "clear", "clear", "clear", "clear"
    };
    
    static String[] initialArrowsPlayGame = {
    	"clear", "clear", "clear", "clear", "clear", "left_arrow",
    	"clear", "clear", "clear", "down_arrow", "clear", "clear",
    	"clear", "clear", "clear", "clear", "clear", "clear",
    	"clear", "clear", "clear", "clear", "clear", "clear",
    	"clear", "up_arrow", "right_arrow", "clear", "clear", "clear",
    	"clear", "clear", "clear", "clear", "clear", "clear"
    };
    
    static boolean[] clickablePlayGame = {
    	true, true, true, true, true, false, 
    	true, true, true, false, true, true, 
    	true, true, true, true, true, true, 
    	true, true, true, true, true, true, 
    	true, false, false, true, true, true, 
    	true,true,true,true,true,true
    	};

    
}




