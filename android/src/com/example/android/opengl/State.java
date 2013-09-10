package com.example.android.opengl;

	public interface State {
		public void enterAnimation(Tile[] tiles);
		public void exitAnimation(Tile[] tiles);
		
		public void draw(Tile[] tiles, MyGLRenderer r);
	}
