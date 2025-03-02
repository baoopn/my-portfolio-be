package com.baoopn.my_portfolio_be.model;

public class SpotifyCurrentlyPlayingTrackDTO {
	private String albumImageUrl;
	private String artist;
	private boolean isPlaying = false;
	private String songUrl;
	private String title;
	private String id;
	private int progressMs;
	private int durationMs;

	// Getters and Setters
	public String getAlbumImageUrl() {
		return albumImageUrl;
	}

	public void setAlbumImageUrl(String albumImageUrl) {
		this.albumImageUrl = albumImageUrl;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

	public String getSongUrl() {
		return songUrl;
	}

	public void setSongUrl(String songUrl) {
		this.songUrl = songUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getProgressMs() {
		return progressMs;
	}

	public void setProgressMs(int progressMs) {
		this.progressMs = progressMs;
	}

	public int getDurationMs() {
		return durationMs;
	}

	public void setDurationMs(int durationMs) {
		this.durationMs = durationMs;
	}
}
