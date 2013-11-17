package com.seventhharmonic.android.freebeeline;

import com.seventhharmonic.android.freebeeline.graphics.TextureManager;

public class BoardLineManager {
	Board mBoard;
	BoardTile[] tiles;
	int boardHeight;
	int boardWidth;
	float[] geometry;
	
	public BoardLineManager(Board b){
		mBoard = b;
		tiles = b.tiles;
		boardHeight = mBoard.boardHeight;
		boardWidth = mBoard.boardWidth;
	}
	
	public void setGeometry(float[] geometry){
		this.geometry = geometry;
	}

	public void animatePlay(int at) {
		//Now go though each tile and fill in pointed at tiles.
		float duration = .3f;
		float delay = .15f;
		//clearLines();
		stopFlips();
		drawLines();
		if (tiles[at].hasNumber() && tiles[at].hasArrow()) {
			int num = Integer.parseInt(tiles[at].getNumber());
			if (tiles[at].getArrow().equals(TextureManager.UPARROW)) {
				for (int j = 1; j < num+1; j++) {
					if (at%boardHeight + j < boardHeight) {
						if (!tiles[at+j].isBlack() && tiles[at+j].isBlank()) {
							String[] s1 = new String[2];
							s1[0] = (tiles[at+j].mEndDirectionType == EndDirectionType.NONE) ? 
							TextureManager.VERTDOTS :
							TextureManager.CLEAR;
							s1[1] = tiles[at+j].textures[1];
							float[] pivot = {1.0f,0.0f,0.0f};
							tiles[at+j].setTextures(TextureManager.CLEAR, TextureManager.CLEAR);
							tiles[at+j].setFlipper(geometry[1], pivot, duration, j*delay ,s1);
						}	
					}
					
				}
			}
			if (tiles[at].getArrow().equals(TextureManager.DOWNARROW)) {
				for (int j = 1; j < num+1; j++) {
					if (at%boardHeight - j >= 0) {
						if (!tiles[at-j].isBlack() && tiles[at-j].isBlank()) { 
							String[] s2 = new String[2];
							s2[0] = (tiles[at-j].mEndDirectionType == EndDirectionType.NONE) ? 
									TextureManager.VERTDOTS :
									TextureManager.CLEAR;
							s2[1] = tiles[at-j].textures[1];
							float[] pivot = {1.0f,0.0f,0.0f};
							tiles[at-j].setTextures(TextureManager.CLEAR, TextureManager.CLEAR);
							tiles[at-j].setFlipper(geometry[1], pivot, duration, j*delay ,s2);
						}

					}
				}
			}
			if (tiles[at].getArrow().equals(TextureManager.LEFTARROW)) {
				for (int j = 1; j < num+1; j++) {
					if (at/boardHeight + j < boardWidth) { 
						if (!tiles[at+j*boardHeight].isBlack() && tiles[at+j*boardHeight].isBlank()) {
							String[] s3 = new String[2];
							s3[1] = (tiles[at+j*boardHeight].mEndDirectionType == EndDirectionType.NONE) ? 
									TextureManager.HORZDOTS :
									TextureManager.CLEAR;
							s3[0] = tiles[at+j*boardHeight].textures[0];
							float[] pivot = {0.0f,1.0f,0.0f};
							tiles[at+j*boardHeight].setTextures(TextureManager.CLEAR, TextureManager.CLEAR);
							tiles[at+j*boardHeight].setFlipper(geometry[1], pivot, duration, j*delay ,s3);
						}

					}
				}
			}
			if (tiles[at].getArrow().equals(TextureManager.RIGHTARROW)) {
				for (int j = 1; j < num+1; j++) {
					if (at/boardHeight - j >= 0) {
						if (!tiles[at-j*boardHeight].isBlack() && tiles[at-j*boardHeight].isBlank()) {
							String[] s4 = new String[2];
							s4[1] = (tiles[at-j*boardHeight].mEndDirectionType == EndDirectionType.NONE) ? 
									TextureManager.HORZDOTS :
									TextureManager.CLEAR;
							s4[0] = tiles[at-j*boardHeight].textures[0];
							float[] pivot = {0.0f,1.0f,0.0f};
							tiles[at-j*boardHeight].setTextures(TextureManager.CLEAR, TextureManager.CLEAR);
							tiles[at-j*boardHeight].setFlipper(geometry[1], pivot, duration, j*delay ,s4);
						}
					}
				}
			}
		}
	}
	// Class to draw the dotted lines on the board.  Called after every input
	public void drawLines() {
		//First remove the lines on tiles which are no longer pointed at.
		markPointedAt();
		for (int i = 0; i < tiles.length; i++ ) {
			if (!tiles[i].hasNumber() && !tiles[i].hasArrow() && !tiles[i].isBlack()) {

				if (!tiles[i].vPointedAt) {
					tiles[i].setTextures(TextureManager.CLEAR, tiles[i].textures[1]);
				}
				else {
					tiles[i].setTextures(TextureManager.VERTDOTS, tiles[i].textures[1]);
				}
				if (!tiles[i].hPointedAt) {
					tiles[i].setTextures(tiles[i].textures[0], TextureManager.CLEAR);
				}
				else {
					tiles[i].setTextures(tiles[i].textures[0], TextureManager.HORZDOTS);
				}
				
				switch(tiles[i].mEndDirectionType){
				case UP:
					tiles[i].setTextures(TextureManager.CLEAR, TextureManager.UPDOT);
					break;
				case DOWN:
					tiles[i].setTextures(TextureManager.CLEAR, TextureManager.DOWNDOT);
					break;
				case LEFT:
					tiles[i].setTextures(TextureManager.LEFTDOT, TextureManager.CLEAR);
					break;
				case RIGHT:
					tiles[i].setTextures(TextureManager.RIGHTDOT, TextureManager.CLEAR);
					break;
				case NONE:
					break;
				}
				
				
			}
		}	
	}

	public void clearLines() {
		//First remove the lines on tiles which are no longer pointed at.
		markPointedAt();
		for (int i = 0; i < tiles.length; i++ ) {
			if (!tiles[i].hasNumber() && !tiles[i].hasArrow() && !tiles[i].isBlack()) {
				if (!tiles[i].vPointedAt) {
					tiles[i].setTextures(TextureManager.CLEAR, tiles[i].textures[1]);
				}
				if (!tiles[i].hPointedAt) {
					tiles[i].setTextures(tiles[i].textures[0], TextureManager.CLEAR);
				}
				
				if(!(tiles[i].mEndDirectionType == EndDirectionType.NONE)){
					tiles[i].setTextures(TextureManager.CLEAR, TextureManager.CLEAR);
				}
			}
		}	
	}

	public void stopFlips() {
		for (int i = 0; i < tiles.length; i++) {
			tiles[i].stopFlipper();
		}
	}

	public void markPointedAt() {
		//Marks all tiles pointed at either V or H
		for (int i = 0; i < tiles.length; i++) {
			tiles[i].vPointedAt = false;
			tiles[i].hPointedAt = false;
			tiles[i].mEndDirectionType = EndDirectionType.NONE;
		}
		for (int i = 0; i < tiles.length; i++ ) {
			if (tiles[i].hasNumber() && tiles[i].hasArrow()) {
				int num = Integer.parseInt(tiles[i].number);
				if (tiles[i].getArrow().equals(TextureManager.UPARROW)) {
					for (int j = 1; j < num; j++) {
						if (i%boardHeight + j < boardHeight) {
							if (!tiles[i+j].isBlack() && tiles[i+j].isBlank()) 
								tiles[i+j].vPointedAt = true;
						}
					}
					
					//Last square on path corresponds to num. Notice that it doesn't have to blank.
					if(i%boardHeight + num < boardHeight && !tiles[i+num].isBlack()){
						tiles[i+num].mEndDirectionType = EndDirectionType.UP;
					}
				}
				if (tiles[i].getArrow().equals(TextureManager.DOWNARROW)) {
					for (int j = 1; j < num; j++) {
						if (i%boardHeight - j >= 0) {
							if (!tiles[i-j].isBlack() && tiles[i-j].isBlank()) 
								tiles[i-j].vPointedAt = true;
						}
					}
					if(i%boardHeight - num >= 0 && !tiles[i-num].isBlack()){
						tiles[i-num].mEndDirectionType = EndDirectionType.DOWN;
					}

				}
				if (tiles[i].getArrow().equals(TextureManager.LEFTARROW)) {
					for (int j = 1; j < num; j++) {
						if (i/boardHeight + j < boardWidth) {
							if (!tiles[i+j*boardHeight].isBlack() && tiles[i+j*boardHeight].isBlank()) 
								tiles[i+j*boardHeight].hPointedAt = true;
						}
					}
					if(i/boardHeight + num < boardWidth && !tiles[i+num*boardHeight].isBlack()){
						tiles[i + num*boardHeight].mEndDirectionType = EndDirectionType.LEFT;
					}

				}

				if (tiles[i].getArrow().equals(TextureManager.RIGHTARROW)) {
					for (int j = 1; j < num; j++) {
						if (i/boardHeight - j >= 0) {
							if (!tiles[i-j*boardHeight].isBlack() && tiles[i-j*boardHeight].isBlank()) 
								tiles[i-j*boardHeight].hPointedAt = true;
						}
					}
					if(i/boardHeight - num >= 0 && !tiles[i-num*boardHeight].isBlack()){
						tiles[i - num*boardHeight].mEndDirectionType = EndDirectionType.RIGHT;
					}
				}
			}
		}
	}


}
