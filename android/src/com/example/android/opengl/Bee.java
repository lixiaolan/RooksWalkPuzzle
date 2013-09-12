package com.example.android.opengl;

public class Bee extends Graphic<BeeTile>{
	public BeeTile bee ;
	
	public Bee() {
		float[] center= {0.0f,0.0f,0.0f};
		tiles = new BeeTile[1];
		tiles[0] = new BeeTile(center,0.2f);
		bee = tiles[0];
		setState(GameState.MAIN_MENU); 
	}

	public void setState(GameState s){
		System.out.println("In Bee");
		System.out.println(s);
		switch(s){
			case MAIN_MENU : state = new BeeWander();break;
			case PLAY: state = new BeeFixed();
								System.out.println("Found Play"); break;
			case STATS: state  = new BeeWander(); break;
		}
		System.out.println("new State?");
		System.out.println(state.getClass().getName());
	}
	
	
	public boolean touched(float[] pt) {
		if(pt[0]< bee.center[0]+0.25f && pt[0] > bee.center[0]-.25f 
				&& pt[1]< bee.center[1]+0.25f && pt[1] > bee.center[1]-.25f ){
			return true;
		}
		return false;
	}

}

class BeeWander extends State<BeeTile> {
	public long refTime;

	private float targetX = 0;
	private float targetY = 0;

	private float startX = 0;
	private float startY = 0;


	public void enterAnimation(BeeTile[] tiles){
		period = DrawPeriod.DURING;
	}


	public void duringAnimation(BeeTile[] tiles) {
		BeeTile bee = (BeeTile)tiles[0];
		long time = System.currentTimeMillis() - refTime;
		float interval = 5000f;
		if(time < interval){
			bee.center[0] = startX + time/interval*(targetX-startX);
			bee.center[1] = startY + time/interval*(targetY-startY);
		} else {
			startX = bee.center[0];
			startY = bee.center[1];
			refTime = System.currentTimeMillis();
			float r = (float)Math.random();
			targetX = -1.5f*r+(1.0f-r)*1.5f;
			r = (float)Math.random();
			targetY = -1.5f*r+(1-r)*1.5f;
		}
	}
	
	public void exitAnimation(BeeTile[] tiles){

	}

}

class BeeFixed extends State<BeeTile> {
	
	float[] c  = {-.75f,-.75f,0.0f};
	
	public void enterAnimation(BeeTile[] tiles){
		
		tiles[0].center = c;
	}

	public void duringAnimation(BeeTile[] tiles) {
		tiles[0].center = c;
	}
	
	public void exitAnimation(BeeTile[] tiles){

	}

}



