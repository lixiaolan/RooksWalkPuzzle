package com.seventhharmonic.android.freebeeline;

import com.seventhharmonic.com.freebeeline.levelresources.Puzzle;
import com.seventhharmonic.com.freebeeline.levelresources.PuzzleParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class DailyPuzzleLoadWidget extends WidgetLayout{
	
    TextBox mText;
    Model mModel;
	String errorText = "Download failed! Make sure you are connected to the internet and try again later.";
	
	public DailyPuzzleLoadWidget(Model model){
		mText = new TextBox(0,0,.9f,"Downloading Puzzle...");
		widgetList.add(mText);
		mModel = model;

	}

	public void downloadPuzzle(){
		DailyPuzzleDownloader d = new DailyPuzzleDownloader();
		d.execute("http://beeline-dp.appspot.com/puzzle");
	}
	
	
	@Override
	public void touchHandler(float[] pt) {
		
	}

	@Override
	public void swipeHandler(String direction) {
		
	}


	@Override
	public void draw(MyGLRenderer r) {
		for(Widget w: widgetList)
		    w.draw(r);
	}

	public class DailyPuzzleDownloader extends AsyncTask<String, Void, String>{
		String TAG = "DailyPuzzleDownloader";
		@Override
		protected String doInBackground(String... urls) {
			String response = "";
			
			 for (String url : urls) {
			        DefaultHttpClient client = new DefaultHttpClient();
			        HttpGet httpGet = new HttpGet(url);
			        try {
			          HttpResponse execute = client.execute(httpGet);
			          InputStream content = execute.getEntity().getContent();

			          BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
			          String s = "";
			          while ((s = buffer.readLine()) != null) {
			            response += s;
			          }

			        } catch (Exception e) {
			          e.printStackTrace();
			        }
			      }
			 		
			      return response;
		}

		 protected void onPostExecute(String result) {
			 Log.d(TAG, result);
			 if(result.equals("")){
				 mText.setText(errorText);
			 } else {
				 PuzzleParser parser = new PuzzleParser();
				 try{
					 Puzzle p = parser.parse(result);
					 Log.d(TAG, p.getFlower()+" ");
				 } catch(RuntimeException e){
					 Log.d(TAG,e.getMessage());
					 mText.setText(errorText);
				 }
			 }
		 }
		
	}
	
}
