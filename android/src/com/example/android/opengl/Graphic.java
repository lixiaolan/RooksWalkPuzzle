package com.example.android.opengl;




public abstract class Graphic {
	Tile[] tiles;
	State state;
	
	public void enterAnimation(MyGLRenderer r) {
		state.enterAnimation(tiles);
	}
	
	public void exitAnimation(MyGLRenderer r){
		state.exitAnimation(tiles);
	}
	
	public void draw(MyGLRenderer r){
		state.draw(tiles,r );
	}
	
	public abstract void setState(GameState s);
	
	public State currState(){
		return state;
	}
}
