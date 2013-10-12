package com.seventhharmonic.android.freebeeline;
import android.widget.Button;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import java.util.ArrayList;
import android.view.View;


class StateButton {
    public Button mButton;
    public float[] position = new float[2];
    public boolean visible;

    //For animation purposes
    public boolean moved;
    public boolean fadeIn;
    public boolean fadeOut;


    public StateButton(android.widget.Button b) {
	mButton = b;
	position[0] = 0.0f;
	position[1] = 0.0f;
	visible = false;
	moved = true;
	fadeOut = false;
	fadeIn = false;
	mButton.setAlpha(0.0f);
    }

    public void setState(float x, float y, boolean vis, long[] durations) {
	if ((x == position[0])&(y == position[1]))
	    moved = false;
	else
	    moved = true;

	if ((visible == true)&(vis == false)){
	    fadeOut = true;
	}
	else
	    fadeOut = false;

	if ((visible == false)&(vis == true)) {
	    fadeIn = true;
	}
	else 
	    fadeIn = false;

	visible = vis;

	position[0] = x;
	position[1] = y;

	animate(durations);
    }

    public void animate(long[] durations) {
	
    AnimatorSet animSet = new AnimatorSet();
	
	ObjectAnimator fadeOutAnim = new ObjectAnimator();
	ObjectAnimator fadeInAnim = new ObjectAnimator();
	ObjectAnimator xAnim = new ObjectAnimator();
	ObjectAnimator yAnim = new ObjectAnimator();
	
	//Fade outs first!
	
	//fade out
	if (fadeOut) {
	    fadeOutAnim = ObjectAnimator.ofFloat(this, "alpha", 1.0f, 0.0f);
	    fadeOutAnim.setTarget(mButton);
	}
	else {  
	    if (fadeIn) {
		fadeOutAnim = ObjectAnimator.ofFloat(this, "alpha", 0.0f, 0.0f);
		fadeOutAnim.setTarget(mButton);
	    }
	    else {
		if (visible) {
		    fadeOutAnim = ObjectAnimator.ofFloat(this, "alpha", 1.0f, 1.0f);
		    fadeOutAnim.setTarget(mButton);
		}
		else {
		    fadeOutAnim = ObjectAnimator.ofFloat(this, "alpha", 0.0f, 0.0f);
		    fadeOutAnim.setTarget(mButton);
		}
		
	    }		
	}
	fadeOutAnim.setDuration(durations[0]);
	
	//move
	xAnim = ObjectAnimator.ofFloat(this, "x", position[0]);
	yAnim = ObjectAnimator.ofFloat(this, "y", position[1]);
	
	xAnim.setTarget(mButton);
	yAnim.setTarget(mButton);
	
	xAnim.setDuration(durations[1]);
	yAnim.setDuration(durations[1]);
	
	//fade in
	if (fadeIn) {
	    fadeInAnim = ObjectAnimator.ofFloat(this, "alpha", 0.0f, 1.0f);
	    fadeInAnim.setTarget(mButton);
	}
	else {  
	    if (fadeOut) {
		fadeInAnim = ObjectAnimator.ofFloat(this, "alpha", 0.0f, 0.0f);
		fadeInAnim.setTarget(mButton);
	    }
	    else {
		if (visible) {
		    fadeInAnim = ObjectAnimator.ofFloat(this, "alpha", 1.0f, 1.0f);
		    fadeInAnim.setTarget(mButton);
		}
		else {
		    fadeInAnim = ObjectAnimator.ofFloat(this, "alpha", 0.0f, 0.0f);
		    fadeInAnim.setTarget(mButton);
		}
		
	    }
	}
	fadeInAnim.setDuration(durations[2]);
	
	
	//Build animation sets
	animSet.play(fadeOutAnim).before(xAnim);
	animSet.play(xAnim).with(yAnim);
	animSet.play(fadeInAnim).after(xAnim);
	
	if (visible) {
	    animSet.addListener(new Animator.AnimatorListener(){
		    @Override
		    public void onAnimationStart(Animator arg0) {
			ArrayList<Animator> al = ((AnimatorSet)arg0).getChildAnimations();
			Button b = (Button)((ObjectAnimator)al.get(0)).getTarget();
			b.setVisibility(View.VISIBLE);
		    }           
		    @Override
		    public void onAnimationRepeat(Animator arg0) {
		    }           
		    @Override
		    public void onAnimationEnd(Animator arg0) {
		    }
		    @Override
		    public void onAnimationCancel(Animator arg0) {
			
		    }
		    
		});
	}
	else {
	    animSet.addListener(new Animator.AnimatorListener(){
		    @Override
		    public void onAnimationStart(Animator arg0) {
		    }           
		    @Override
		    public void onAnimationRepeat(Animator arg0) {
		    }           
		    @Override
		    public void onAnimationEnd(Animator arg0) {
			ArrayList<Animator> al = ((AnimatorSet)arg0).getChildAnimations();
			Button b = (Button)((ObjectAnimator)al.get(0)).getTarget();
			b.setVisibility(View.INVISIBLE);
		    }
		    @Override
		    public void onAnimationCancel(Animator arg0) {
			
		    }
		    
		});
	}
	animSet.start();
    }
}

