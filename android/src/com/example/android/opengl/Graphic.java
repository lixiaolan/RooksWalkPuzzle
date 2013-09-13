package com.example.android.opengl;




public abstract class Graphic<T extends Tile> {
	T[] tiles;
	State<T> state;
			
	public void draw(MyGLRenderer r){
		state.draw(tiles,r );
	}
	
	public abstract void setState(GameState s);
	
	public State currState(){
		return state;
	}
}
