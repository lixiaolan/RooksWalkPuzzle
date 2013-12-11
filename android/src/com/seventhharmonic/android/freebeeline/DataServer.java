package com.seventhharmonic.android.freebeeline;

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
		int[] solutions = dumpSolution(mBoard);
		String[] numbers  = dumpNumbers(mBoard);
		String[] arrows  = dumpArrows(mBoard);
		String[] trueArrows = dumpTrueArrows(mBoard);
		int[][] path  = dumpPath(mBoard);
		boolean[] clickable = dumpClickable(mBoard);

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
	
	public void saveLinesOption(boolean lines){
		SharedPreferences s  = mContext.getSharedPreferences(settingsfile, 0);
		SharedPreferences.Editor editor = s.edit();
		editor.putBoolean("lines", lines);
        editor.commit();
	}
	
	public void saveErrorCheckingOption(boolean error){
		SharedPreferences s  = mContext.getSharedPreferences(settingsfile, 0);
		SharedPreferences.Editor editor = s.edit();
		editor.putBoolean("errorChecking", error);
        editor.commit();
	}
	
	public void saveMusicToggle(boolean error){
		SharedPreferences s  = mContext.getSharedPreferences(settingsfile, 0);
		SharedPreferences.Editor editor = s.edit();
		editor.putBoolean("music", error);
        editor.commit();
	}
	
	public boolean getLinesOption(){
		SharedPreferences s  = mContext.getSharedPreferences(settingsfile, 0);
		return s.getBoolean("lines", true);
	}
	
	public boolean getMusicToggle(){
		SharedPreferences s  = mContext.getSharedPreferences(settingsfile, 0);
		return s.getBoolean("music", true);
	}
	
	public boolean getErrorCheckingOption(){
		SharedPreferences s  = mContext.getSharedPreferences(settingsfile, 0);
		return s.getBoolean("errorChecking", true);
	}
	
	public int getFlowersVisited() {
		SharedPreferences s  = mContext.getSharedPreferences(settingsfile, 0);
		int totalLines = s.getInt("flowersVisited", 0);	
		return totalLines;
	}
	
	public void setFlowers(int value){		
		SharedPreferences s  = mContext.getSharedPreferences(settingsfile, 0);
		int totalFlowers = 0;
		SharedPreferences.Editor editor = s.edit();
		totalFlowers = s.getInt("flowersVisited", 0);
		editor.putInt("flowersVisited", value+totalFlowers);
        editor.commit();
	}

	
	
	
	public int[] dumpSolution(Board mBoard) {
		int[] solution = new int[36];
		for(int i =0;i<36;i++){
			solution[i] = mBoard.tiles[i].getTrueSolution();
		}
		return solution;
	}

	public String[] dumpArrows(Board mBoard) {
		String[] arrows = new String[36];
		for(int i =0;i<36;i++){
			arrows[i] = mBoard.tiles[i].getArrow();
		}
		return arrows;
	}

	public String[] dumpNumbers(Board mBoard) {
		String[] numbers = new String[36];
		for(int i =0; i<36; i++){
			numbers[i] = mBoard.tiles[i].getNumber();
		}
		return numbers;
	}

	public String[] dumpTrueArrows(Board mBoard) {
		String[] arrows = new String[36];
		for(int i =0; i<36; i++){
			arrows[i] = mBoard.tiles[i].getTrueArrow();
		}
		return arrows;
	}

	public boolean[] dumpClickable(Board mBoard) {
		boolean[] clickable = new boolean[36];
		for(int i =0; i<36; i++){
			clickable[i] = mBoard.tiles[i].isClickable();
		}
		return clickable;
	}

	public int[][] dumpPath(Board mBoard) {
		return mBoard.path;
	}

	
	
}
	
