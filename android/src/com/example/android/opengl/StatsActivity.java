package com.example.android.opengl;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class StatsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statsactivity);
		LineGraphView lgv = (LineGraphView)findViewById(R.id.lineGraphView1);
		float[] pts = {1.1f,15.029f,.0013f, 4.789f,.99901f, 15.9999f};
		lgv.setData(pts);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.statsactivity, menu);
		return true;
	}

}
