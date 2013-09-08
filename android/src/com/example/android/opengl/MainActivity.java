package com.example.android.opengl;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.GridView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainactivity);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mainactivity_actions, menu);
		return true;
	}
	
	public void launchEvent(View view) {
	    Intent intent = new Intent(this, ViewActivity.class);
	    startActivity(intent);
	}
	
	public void settingsEvent(View view) {
	    Intent intent = new Intent(this, ViewActivity.class);
	    startActivity(intent);
	}
	
	public void statisticsEvent(View view) {
	    Intent intent = new Intent(this, StatsActivity.class);
	    startActivity(intent);
	}
	
	public void exitEvent(View view) {
		finish();
	}
	
	
}