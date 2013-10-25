package com.seventhharmonic.com.freebeeline.levelresources;

public class Hint {
	int number;
	int index;
	String direction;
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	
	public Hint copy(){
		Hint h = new Hint();
		h.setNumber(number);
		h.setIndex(index);
		h.setDirection(direction);
		return h;
	}
}
