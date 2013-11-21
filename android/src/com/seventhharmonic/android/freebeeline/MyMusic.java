package com.seventhharmonic.android.freebeeline;

import java.util.List;
import java.util.TimerTask;
import java.util.Timer;

import com.seventhharmonic.android.freebeeline.graphics.Geometry;
import com.seventhharmonic.android.freebeeline.graphics.TextureManager;
import com.seventhharmonic.android.freebeeline.listeners.GameEventListener;
import com.seventhharmonic.com.freebeeline.levelresources.Hint;
import com.seventhharmonic.com.freebeeline.levelresources.Puzzle;

import android.media.MediaPlayer;
import android.media.AudioManager;
import android.content.Context;
import android.os.Vibrator;
import android.util.Log;
import android.app.Activity;
import android.app.Service;
import android.os.Binder;
import android.os.IBinder;
import android.content.Intent;


public class MyMusic{
    
    MediaPlayer mMediaPlayer;
    Context context;
    private String currSong = "";
    private int length = 0;
    private int iVolume;
    private int fadeDuration = 666;
    private final static int INT_VOLUME_MAX = 100;
    private final static int INT_VOLUME_MIN = 0;
    private final static float FLOAT_VOLUME_MAX = 1;
    private final static float FLOAT_VOLUME_MIN = 0;
    


    public MyMusic(Context c) {
	context = c;
    }
    
    public void playSong(String song) {
	if (song.equals(currSong) ) {
	    
	}
	else {
	    if (song.equals("default") ) {
		stopMusic();
		mMediaPlayer = MediaPlayer.create(context, R.raw.themesong);
		onCreate();
	    }
	    currSong = song;
	}
    }
    

    // public class ServiceBinder extends Binder {
    // 	MyMusic getService()
    // 	{
    // 	    return MyMusic.this;
    // 	}
    // }
    
    public void onCreate() {

	if (mMediaPlayer != null) {
	    mMediaPlayer.setLooping(true);
	    mMediaPlayer.setVolume(100,100);
	}

	mMediaPlayer.start();
    }

    public void pauseMusic()
    {

	if (mMediaPlayer != null) {
	    //Set current volume, depending on fade or not
	    if (fadeDuration > 0) 
		iVolume = INT_VOLUME_MAX;
	    else 
		iVolume = INT_VOLUME_MIN;
	    
	    updateVolume(0);
	    
	    //Start increasing volume in increments
	    if(fadeDuration > 0)
		{
		    final Timer timer = new Timer(true);
		    TimerTask timerTask = new TimerTask() 
			{
			    @Override
			    public void run() 
			    {   
				updateVolume(-1);
				if (iVolume == INT_VOLUME_MIN)
				    {
					//Pause music
					if (mMediaPlayer.isPlaying()) mMediaPlayer.pause();
					timer.cancel();
					timer.purge();
				    }
			    }
			};
		    
		    // calculate delay, cannot be zero, set to 1 if zero
		    int delay = fadeDuration/INT_VOLUME_MAX;
		    if (delay == 0) delay = 1;
		    
		    timer.schedule(timerTask, delay, delay);
		}     
	    // if(mMediaPlayer.isPlaying())
	    // 	{
	    // 	    mMediaPlayer.pause();
	    // 	    length=mMediaPlayer.getCurrentPosition();
	    
	    // 	}
	}
    }
    
    public void resumeMusic()
    {
	if (mMediaPlayer != null) {
	    
	    //Set current volume, depending on fade or not
	    if (fadeDuration > 0) 
		iVolume = INT_VOLUME_MIN;
	    else 
		iVolume = INT_VOLUME_MAX;
	    
	    updateVolume(0);
	    
	    //Play music
	    if(!mMediaPlayer.isPlaying()) mMediaPlayer.start();
	    
	    //Start increasing volume in increments
	    if(fadeDuration > 0)
		{
		    final Timer timer = new Timer(true);
		    TimerTask timerTask = new TimerTask() 
			{
			    @Override
			    public void run() 
			    {
				updateVolume(1);
				if (iVolume == INT_VOLUME_MAX)
				    {
					timer.cancel();
					timer.purge();
				    }
			    }
			};
		    
		    // calculate delay, cannot be zero, set to 1 if zero
		    int delay = fadeDuration/INT_VOLUME_MAX;
		    if (delay == 0) delay = 1;
		    
		    timer.schedule(timerTask, delay, delay);
		}
	    
	    // if(mMediaPlayer.isPlaying()==false)
	    // 	{
	    // 	    mMediaPlayer.seekTo(length);
	    // 	    mMediaPlayer.start();
	    // 	}
	}
    }

    public void resetMusic() {
	mMediaPlayer.stop();
	
    }
    
    public void stopMusic()
    {
	currSong = "";
	if (mMediaPlayer != null) {
	    mMediaPlayer.stop();
	    mMediaPlayer.release();
	}
	
	mMediaPlayer = null;
    }
    
    public void onDestroy ()
    {
	
	if(mMediaPlayer != null)
	    {
		try{
		    mMediaPlayer.stop();
		    mMediaPlayer.release();
		}finally {
		    mMediaPlayer = null;
		}
	    }
    }    


    private void updateVolume(int change) {
	//increment or decrement depending on type of fade
	iVolume = iVolume + change;
	
	//ensure iVolume within boundaries
	if (iVolume < INT_VOLUME_MIN)
	    iVolume = INT_VOLUME_MIN;
	else if (iVolume > INT_VOLUME_MAX)
	    iVolume = INT_VOLUME_MAX;
	
	//convert to float value
	float fVolume = 1 - ((float) Math.log(INT_VOLUME_MAX - iVolume) / (float) Math.log(INT_VOLUME_MAX));
	
	//ensure fVolume within boundaries
	if (fVolume < FLOAT_VOLUME_MIN)
	    fVolume = FLOAT_VOLUME_MIN;
	else if (fVolume > FLOAT_VOLUME_MAX)
	    fVolume = FLOAT_VOLUME_MAX;     
	
	mMediaPlayer.setVolume(fVolume, fVolume);
    }
}

