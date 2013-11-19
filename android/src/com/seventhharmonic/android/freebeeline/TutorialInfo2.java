package com.seventhharmonic.android.freebeeline;

public class TutorialInfo2 {
    

    public TutorialInfo2() {
    }

    //Boob Dyla
   public static String[] banners = {"Create a loop and fill the board. The arrows tell you where to go and the numbers indicate how far.",
    								"Touch an empty square to enter a number. Swipe to enter a direction.",
    								"The yellow squares are parts of the path we have given you. Squares with a circle are the next segment of the path.",
    								"Don't forget Beatrice's Rule:^ A square with a number can't face towards a square with the same number.",
    							};
    
   
    
    
    
////////////////////////// SLIDE1   
   static int[][] pathSlide1  = {{1,3},{4,3},{4,4},{2,4},{2,1},{1,1}};
   static int[] hintsSlide1 = {7,13,25,28,16,10};
   
   static int lengthSlide1 = 6;
  
   static int[] solutionNumbersSlide1 = {
	-1, -1, -1, -1, -1, -1, 
	-1, 1, 0, 0, 3, -1, 
	-1, 2, -1, -1, 1, -1, 
	-1, 0, -1, -1, 0, -1, 
	-1, 3, 0, 0, 2, -1, 
	-1,-1,-1,-1,-1,-1 };
        
    static String[] solutionArrowsSlide1 = {
	"clear",      "clear",            "clear", "clear",       "clear",       "clear", 
	"clear",      "left_arrow",       "clear", "clear",       "down_arrow",       "clear", 
	"clear",      "left_arrow",      "clear", "clear",       "right_arrow",       "clear", 
	"clear",      "clear",            "clear", "clear",       "clear",       "clear", 
	"clear",      "up_arrow",      "clear", "clear",       "right_arrow",    "clear", 
	"clear",      "clear",            "clear", "clear",       "clear",       "clear" };

    static String[] initialNumbersSlide1 = {
    	"clear", "clear", "clear", "clear", "clear", "clear", 
    	"clear", "clear", "clear", "clear", "clear", "clear", 
    	"clear", "clear", "clear", "clear", "clear", "clear", 
    	"clear", "clear", "clear", "clear", "clear", "clear", 
    	"clear", "clear", "clear", "clear", "clear", "clear", 
    	"clear","clear",  "clear", "clear", "clear", "clear"};

    static String[] initialArrowsSlide1 = {
    	"clear", "clear", "clear", "clear", "clear", "clear", 
    	"clear", "clear", "clear", "clear", "clear", "clear", 
    	"clear", "clear", "clear", "clear", "clear", "clear", 
    	"clear", "clear", "clear", "clear", "clear", "clear", 
    	"clear", "clear", "clear", "clear", "clear", "clear", 
    	"clear", "clear", "clear", "clear", "clear", "clear"};
    
//////////////////////////////////////    
 //Slide 2 
    static int[] solutionNumbersSlide2 = {
    	-1, -1, -1, -1, -1, -1, 
    	-1,  0,  0,  0, -1, -1, 
    	-1,  0, -1,  0,  -1, -1, 
    	-1,  0, -1,  0,  -1, -1, 
    	-1,  0,  0,  0,  -1, -1, 
    	-1, -1, -1, -1, -1,-1 };
            
    static String[] solutionArrowsSlide2 = {
    	"clear",      "clear",      "clear", "clear",  "clear",  "clear", 
    	"clear",      "clear",   "clear", "clear",       "clear", "clear", 
    	"clear",   "clear","clear", "clear",    "clear",       "clear", 
    	"clear",      "clear", "clear", "clear",  "clear",       "clear", 
    	"clear",      "clear",      "clear", "clear", "clear",       "clear", 
    	"clear","clear", "clear", "clear",       "clear",       "clear" };

    static String[] initialNumbersSlide2 = {
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear", "clear", "clear", "clear","clear","clear"};

    static String[] initialArrowsSlide2 = {
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear","clear","clear","clear","clear","clear"};    
/////////////////////////////////////////////

    
///////////////////////////Slide 3   
    //Slide 2 
    static int[] solutionNumbersSlide3 = {
    	-1, -1, -1, -1, -1, -1, 
    	-1,  0,  0,  2, -1, -1, 
    	-1,  0, -1,  0,  -1, -1, 
    	-1,  0, -1,  0,  -1, -1, 
    	-1,  0,  0,  3,  -1, -1, 
    	-1, -1, -1, -1, -1,-1 };
            
    static String[] solutionArrowsSlide3 = {
    	"clear",      "clear",      "clear", "clear",  "clear",  "clear", 
    	"clear",      "clear",   "clear", "down_arrow",       "clear", "clear", 
    	"clear",      "clear",    "clear", "clear",    "clear",       "clear", 
    	"clear",      "clear", "clear", "clear",  "clear",       "clear", 
    	"clear",      "clear",      "clear", "right_arrow", "clear",       "clear", 
    	"clear","clear", "clear", "clear",       "clear",       "clear" };

    static String[] initialNumbersSlide3 = {
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear", "clear", "clear", "3", "clear", "clear", 
        	"clear","clear","clear","clear","clear","clear"};

    static String[] initialArrowsSlide3 = {
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear", "clear", "clear", "clear", "clear", "clear", 
        	"clear", "clear", "clear", "right_arrow", "clear", "clear", 
        	"clear","clear","clear","clear","clear","clear"};    

/////////////////////////////////////////////
//Slide4
    static int[] solutionNumbersSlide4 = {
         -1, -1, -1, -1, -1, -1, 
         -1, 1, 0, 2, -1, -1, 
         -1, 3, 0, 0, 2, -1, 
         -1, -1, -1, 0, 0, -1, 
         -1, -1, -1, 3, 1, -1, 
         -1,-1,-1,-1,-1,-1 };
         
     static String[] solutionArrowsSlide4 = {
         "clear",      "clear",      "clear", "clear",  "clear",  "clear", 
         "clear",      "left_arrow",   "clear", "down_arrow",       "clear", "clear", 
         "clear",   "up_arrow","clear", "clear",    "left_arrow",       "clear", 
         "clear",      "clear", "clear", "clear",  "clear",       "clear", 
         "clear",      "clear",      "clear", "right_arrow", "down_arrow",       "clear", 
         "clear","clear", "clear", "clear",       "clear",       "clear" };

     static String[] initialNumbersSlide4 = {
             "clear", "clear", "clear", "clear", "clear", "clear", 
             "clear", "clear", "clear", "clear", "clear", "clear", 
             "clear", "clear", "clear", "clear", "clear", "clear", 
             "clear", "clear", "clear", "clear", "clear", "clear", 
             "clear", "clear", "clear", "clear", "clear", "clear", 
             "clear","clear","clear","clear","clear","clear"};

     static String[] initialArrowsSlide4 = {
             "clear", "clear", "clear", "clear", "clear", "clear", 
             "clear", "clear", "clear", "clear", "clear", "clear", 
             "clear", "clear", "clear", "clear", "clear", "clear", 
             "clear", "clear", "clear", "clear", "clear", "clear", 
             "clear", "clear", "clear", "clear", "clear", "clear", 
             "clear","clear","clear","clear","clear","clear"};
    
    
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
    	"clear", "clear", "clear", "clear", "clear", "clear",
    	"clear", "clear", "clear", "clear", "clear", "clear",
    	"clear", "clear", "clear", "clear", "clear", "clear",
    	"clear", "clear", "clear", "clear", "clear", "clear",
    	"clear", "clear", "clear", "clear", "clear", "clear"
    };
    
    static String[] initialArrowsPlayGame = {
    	"clear", "clear", "clear", "clear", "clear", "clear",
    	"clear", "clear", "clear", "clear", "clear", "clear",
    	"clear", "clear", "clear", "clear", "clear", "clear",
    	"clear", "clear", "clear", "clear", "clear", "clear",
    	"clear", "clear", "clear", "clear", "clear", "clear",
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




