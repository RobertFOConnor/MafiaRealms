package com.yellowbyte.giovannifallout.tween;

import aurelienribon.tweenengine.TweenAccessor;

import com.yellowbyte.giovannifallout.TextButton;

public class ButtonTween implements TweenAccessor<TextButton> {
	
	public static final int POSITION_Y = 1; //** there will one int declaration per object **//

	@Override
	public int getValues(TextButton target, int tweenType, float[] returnValues) {
		switch(tweenType) {
        case POSITION_Y: 
        	returnValues[0] = target.getPosition().y; 
        	return 1; // ** one case for each object - returned one as only 1 value is being changed **//
        default: 
        	assert false; 
        	return -1;
    }
	}

	@Override
	public void setValues(TextButton target, int tweenType, float[] newValues) {
		
		switch (tweenType) {
        case POSITION_Y: 
        	target.getPosition().y = newValues[0];
        	break;
        default: 
        	assert false; 
        	break;
		}

	}
}
