package com.yellowbyte.giovannifallout.media;

public class Settings {
	private boolean musicEnabled = true;
	private boolean soundFXEnabled = true;
	
	public Settings() {
		this.musicEnabled = true;
		this.soundFXEnabled = true;
	}
	
	public boolean isMusicEnabled() {
		return musicEnabled;
	}
	public void setMusicEnabled(boolean musicEnabled) {
		this.musicEnabled = musicEnabled;
	}
	public boolean isSoundFXEnabled() {
		return soundFXEnabled;
	}
	public void setSoundFXEnabled(boolean soundFXEnabled) {
		this.soundFXEnabled = soundFXEnabled;
	}

	
}
