package com.example.android.opengl;

class TutorialInfo {
    
    int Counter;

    public TutorialInfo() {
    	Counter = -1;
    }

    //Boob Dylan
    static String[] banners = {"Your goal is to find the path the bee is following!",
			       "The bee follows the directions on the tiles with numbers and arrows.",
			       "Next you will learn how to find these numbers and arrows.",
			       "Touch the blue square and enter the number 2",
			       "Now swipe the screen to the left to enter an arrow...",
			       "Great, now you can input numbers and arrows!",
			       "The bee follows some basic rules.",
			       "Rule 1: Column and row sums must match the border numbers.",
			       "Enter a number at the blue square according to rule 1",
			       "Good, the row now adds to 4!",
			       "Rule 2: The direction changes at each tile visited.",
			       "Swipe to enter an arrow according to rule 2.",
			       "Good, the new direction now points down!",//I dont like this one
			       "Rule 1: Enter a number which satisfies the column sum",
			       "Rule 2: Swipe to enter an arrow in a new direction.",
			       "Rule 1: Enter a number which satisfies the row sum.",
			       "Rule 2: Swipe to enter an arrow in a a new direction.",
			       "Here it seems like there are two choices of direction!",
			       "Rule 3: Numbered tiles never point past other numbered tiles.",
			       "Swipe to enter an arrow that does not point past another numbered tile.",
			       "Perfect! the arrow does not point past any numbered tile!",
			       "Let's put it all together now and finish the puzzle.",
			       "Enter a number which satisfies the column sum.",
			       "Swipe to enter an arrow in a new direction.",
			       "Find the number and arrow here.",
			       "Find the number and arrow here.",
			       "Find the number and arrow here.",
			       "Find the number and arrow here.",
			       "Congratulations, you solved the puzzle!",
			       "Watch the bee follow your instructions while we review.",
			       "Rule 1: Column and row sums must match the border numbers.",
			       "Rule 2: The direction changes at each tile visited.",
			       "Rule 3: Numbered tiles never point past other numbered tiles.",
			       "Master the three rules of the bee-line and you will bee enlightened."};
    
     static String[] bottomBanners = {"touch anywhere to continue",
				      "touch anywhere to continue",
				      "touch anywhere to continue",
				      "",
				      "",
				      "touch anywhere to continue",
				      "touch anywhere to continue",
				      "touch anywhere to continue",
				      "",
				      "touch anywhere to continue",
				      "touch anywhere to continue",
				      "",
				      "touch anywhere to continue",
				      "",
				      "",
				      "",
				      "",
				      "touch anywhere to continue",
				      "touch anywhere to continue",
				      "",
				      "touch anywhere to continue",
				      "touch anywhere to continue",
				      "",
				      "",
				      "",
				      "",
				      "",
				      "",
				      "touch anywhere to continue",
				      "touch anywhere to continue",
				      "touch anywhere to continue",
				      "touch anywhere to continue",
				      "touch anywhere to continue",
				      "",};
    
    
    static int[] activeTile = {17,17,17,17,17,17,17,17,29,29,29,29,29, 27,27,21, 21, 19,19,19,19,19, 30, 30, 12, 7, 10, 4, 4, 4, 4, 4, 4, 4};
    
    static int[][] path  = {{0,3},{0,4},{1,4},{1,1},{2,1},{2,0},{5,0},{5,1},{3,1},{3,3},{4,3},{4,5},{2,5},{2,3}};
    
    static int length = 14;
    
    static String[] arrow = {"none",
			     "none",
			     "none",
			     "none",
			     "left_arrow",
			     "none",
			     "none",
			     "none",
			     "none",
			     "none",
			     "none",
			     "down_arrow",
			     "none",
			     "none",
			     "right_arrow",
			     "none",
			     "down_arrow",
			     "none",
			     "none",
			     "left_arrow",
			     "none",
			     "none",
			     "none",
			     "right_arrow",
			     "up_arrow",
			     "up_arrow",
			     "right_arrow",
			     "down_arrow",
			     "none",
			     "none",
			     "none",
			     "none",
			     "none",
			     "none"};

    static String[] number = {"none",
			      "none",
			      "none",
			      "2",
			      "none",
			      "none",
			      "none",
			      "none",
			      "2",
			      "none",
			      "none",
			      "none",
			      "none",
			      "1",
			      "none",
			      "2",
			      "none",
			      "none",
			      "none",
			      "none",
			      "none",
			      "none",
			      "3",
			      "none",
			      "1",
			      "3",
			      "1",
			      "1",
			      "none",
			      "none",
			      "none",
			      "none",
			      "none",
			      "none",};
    
    static String OneTileBanner = "Touch. Swipe. Get excited!";
    static String ShowPathBanner = "The bee solved this puzzle, can you?";
    
    static int[] solutionNumbers = {
	-1, -1, -1, 2, 1, -1, 
	-1, 3, 0, 0, 1, -1, 
	1, 1, -1, 2, 0, 2, 
	0, 2, 0, 2, -1, 0, 
	0, 0, -1, 1, 0, 2, 
	3,1,-1,-1,-1,-1 };
    
    
    static String[] solutionArrows = {
	"clear",      "clear",      "clear", "left_arrow",  "down_arrow",  "clear", 
	"clear",      "up_arrow",   "clear", "clear",       "right_arrow", "clear", 
	"up_arrow",   "right_arrow","clear", "up_arrow",    "clear",       "left_arrow", 
	"clear",      "left_arrow", "clear", "down_arrow",  "clear",       "clear", 
	"clear",      "clear",      "clear", "right_arrow", "clear",       "down_arrow", 
	"right_arrow","down_arrow", "clear", "clear",       "clear",       "clear" };


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
	   if(Counter<banners.length-1)
		   Counter++;
   }
    
}




