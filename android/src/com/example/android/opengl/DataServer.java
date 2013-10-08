package com.example.android.opengl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class DataServer {

	public Context mContext;
	public String savefile = "savefile";
	public String settingsfile = "settingsfile";
	
	public void setContext(Context context){
		mContext = context;
	}
	
	public void saveGame(Board mBoard) {
		File file = new File(mContext.getFilesDir(), "savefile");
		int[] solutions = mBoard.dumpSolution();
		String[] numbers  = mBoard.dumpNumbers();
		String[] arrows  = mBoard.dumpArrows();
		String[] trueArrows = mBoard.dumpTrueArrows();
		int[][] path  = mBoard.dumpPath();
		boolean[] clickable = mBoard.dumpClickable();

		try {
			PrintWriter outputStream = new PrintWriter(new FileWriter(file));
			for(int i =0; i< solutions.length;i++){
				outputStream.println(Integer.toString(solutions[i]));
			}

			for(int i =0; i< numbers.length;i++){
				outputStream.println(numbers[i]);
			}

			for(int i =0; i< arrows.length;i++){
				outputStream.println(arrows[i]);
			}

			for(int i =0; i< trueArrows.length;i++){
				outputStream.println(trueArrows[i]);
			}

			for(int i =0; i< clickable.length;i++){
				outputStream.println(clickable[i]);
			}

			outputStream.println(path.length);
			outputStream.println(path[0].length);

			for(int i =0;i< path.length ;i++){
				for(int j=0;j<path[0].length;j++){
					outputStream.println(path[i][j]);
				}
			}

			outputStream.close();
			SharedPreferences s  = mContext.getSharedPreferences("settingsfile", 0);
			SharedPreferences.Editor editor = s.edit();
			editor.putBoolean("savedGameExists", true);
			editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void restoreGame(Board mBoard) {
		try{
     	    int[] solution = new int[36];
     	    String[] numbers = new String[36];
     	    String[] arrows = new String[36];
     	    String[] trueArrows = new String[36];
     	    int[][] path;
     	    boolean[] clickable = new boolean[36];
     	    
     	    File file = new File(mContext.getFilesDir(), "savefile");
	    
     	    if(file.exists()){
     		Scanner scanner = new Scanner(new BufferedReader(new FileReader(file)));
     		for(int i=0; i<36;i++){
     		    int m = scanner.nextInt();
     		    solution[i] = m;
     		}
     		for(int i=0; i<36;i++){
     		    String m = scanner.next();
     		    numbers[i] = m;
     		}
     		for(int i=0; i<36;i++){
     		    String m = scanner.next();
     		    arrows[i] = m;
     		}
     		for(int i=0; i<36;i++){
     		    String m = scanner.next();
     		    trueArrows[i] = m;
     		}
     		
     		for(int i =0; i< clickable.length;i++){
     			boolean m = scanner.nextBoolean();
    		    clickable[i] = m;
    		}
     		
     		int path_length = scanner.nextInt();
     		int twiddle = scanner.nextInt();
     		path = new int[path_length][twiddle];
     		for(int i=0; i< path_length;i++){
     			for(int j = 0;j< twiddle;j++){
     				path[i][j] = scanner.nextInt();
     			}
     		}
     		scanner.close();
     		mBoard.restoreBoard(solution, numbers, arrows, trueArrows, path, clickable);
     		//restorePuzzle(solution, numbers, arrows, trueArrows, path, clickable);
     	    } 
     	}
	
     	catch (FileNotFoundException e) {
     	    Log.e("Exception", "File not found: " + e.toString());
     	} 
     	catch (InputMismatchException e) {
     	    System.out.print(e.getMessage()); //try to find out specific reason.
     	}
	
	
	}

	public boolean resumeExists(){
		SharedPreferences s  = mContext.getSharedPreferences(settingsfile, 0);
		return s.getBoolean("savedGameExists", false);	
	}

	public boolean firstRun() {
		SharedPreferences settings = mContext.getSharedPreferences(settingsfile, 0); // Get preferences file (0 = no option flags set)
	    boolean firstRun = settings.getBoolean("firstRun", true);
		if(firstRun){
			SharedPreferences.Editor editor = settings.edit(); // Open the editor for our settings
	        editor.putBoolean("firstRun", false); // It is no longer the first run
	        editor.commit();
			return true;
		}
		return false;
	}
	
	public int getTotalLines() {
		SharedPreferences s  = mContext.getSharedPreferences(settingsfile, 0);
		int totalLines = s.getInt("totalLines", 0);	
		return totalLines;
	}
	
	public void setLines(int difficulty){
		SharedPreferences s  = mContext.getSharedPreferences(settingsfile, 0);
		int totalLines = 0;
		SharedPreferences.Editor editor = s.edit();
		int r = difficulty;
		switch(difficulty ) {
		case Model.SHORT:
			totalLines = s.getInt("shortLines", 0);
			editor.putInt("shortLines", totalLines+r); 
			break;
		case Model.MEDIUM:
			totalLines = s.getInt("mediumLines", 0);
			editor.putInt("mediumLines", totalLines+r); 
			break;
		case Model.LONGER:
			totalLines = s.getInt("longerLines", 0);
			editor.putInt("longerLines", totalLines+r); 
			break;
		case Model.LONGEST:
			totalLines = s.getInt("longestLines", 0);	
			editor.putInt("longestLines", totalLines+r); 
			break;
		}
       // It is no longer the first run
        editor.commit();
	}

	public int getShortLines(){
		SharedPreferences s  = mContext.getSharedPreferences(settingsfile, 0);	
		return s.getInt("shortLines",0);
	}
	
	public int getMediumLines() {
		SharedPreferences s  = mContext.getSharedPreferences(settingsfile, 0);	
		return s.getInt("mediumLines",0);
		
	}

	public int getLongerLines() {
		SharedPreferences s  = mContext.getSharedPreferences(settingsfile, 0);	
		return s.getInt("longerLines",0);
	}
	
	public int getLongestLines() {
		SharedPreferences s  = mContext.getSharedPreferences(settingsfile, 0);	
		return s.getInt("longestLines",0);
	}
	
}
	
