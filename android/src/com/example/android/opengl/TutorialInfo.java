package com.example.android.opengl;

class TutorialInfo {
    
    int Counter;

    public TutorialInfo() {
    	Counter = -1;
    }

    //Boob Dylan
    static String[] banners = {"Note that the bee follows the numbers and arrows.",
    				"The bee follows some basic rules.",
    				"Rule 1: Column and row sums must match the border numbers",
			       "Rule 1: Column and row sums must match the border numbers",
			       "Rule 2: The bee must change direction",
			       "Remember Rule 1...",
			       "Rule 2: The bee must change direction",
			       "Rule 3: The bee flys over empty spaces",
			       "Remember Rule 1:",
			       "Remember Rule 2:",
			       "Put it all together",
			       "Rule 3: The bee flys over empty spaces",
                               "You are almost there!",
			       "One more step!",
			       "Can you trace out your path?"};

    static String[] bottomBanners = {
    	"Tell the bee to go two to the left from the blue tile.",
    	"The yellow squares are hints.",
    	"Find the number.",
	       "Find the number",
	       "Find the arrow",
	       "Find the number",
	       "Find the arrow",
	       "Find the number and arrow",
	       "Find the number",
	       "Find the arrow",
	       "Find the number and arrow",
	       "Find the number and arrow",
                    "Find the number and arrow",
	       "Find the number and arrow",
	       "Good Job!"};

    
    static int[] activeTile = {17, 17, 29, 27,27,21, 21, 19, 30, 30, 12, 7, 10, 4, 3};
    
    static int[][] path  = {{0,3},{0,4},{1,4},{1,1},{2,1},{2,0},{5,0},{5,1},{3,1},{3,3},{4,3},{4,5},{2,5},{2,3}};
    
    static int length = 14;

    static String[] arrow = {"left_arrow",
    			"none",
    			"down_arrow",
			     "none",
			     "right_arrow",
			     "none",
			     "down_arrow",
			     "left_arrow",
			     "none",
			     "right_arrow",
			     "up_arrow",
			     "up_arrow",
			     "right_arrow",
			     "down_arrow",
			     "none"};
    static String[] number = {"2",
    			"none",
    			  "2",
			      "1",
			      "none",
			      "2",
			      "none",
			      "none",
			      "3",
			      "none",
			      "1",
			      "3",
			      "1",
			      "1",
			      "none"};

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
	"clear", "clear", "clear", "left_arrow", "down_arrow", "clear", 
	"clear", "up_arrow", "clear", "clear", "right_arrow", "clear", 
	"up_arrow", "right_arrow", "clear", "up_arrow", "clear", "left_arrow", 
	"clear", "left_arrow", "clear", "down_arrow", "clear", "clear", 
	"clear", "clear", "clear", "right_arrow", "clear", "down_arrow", 
	"right_arrow","down_arrow","clear","clear","clear","clear" };


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




