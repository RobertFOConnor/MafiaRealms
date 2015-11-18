package com.yellowbyte.giovannifallout.touch;


public class TouchManager {

	private TouchState touchState;

	public void setState(TouchState touchState) {
		this.touchState = touchState;
	}
	
	public TouchState getState() {
		return touchState;
	}
}
