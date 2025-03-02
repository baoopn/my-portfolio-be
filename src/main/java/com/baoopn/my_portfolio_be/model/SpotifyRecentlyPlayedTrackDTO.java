package com.baoopn.my_portfolio_be.model;

import java.time.ZonedDateTime;

public class SpotifyRecentlyPlayedTrackDTO {
	private String albumImageUrl;
	private String artist;
	private ZonedDateTime playedAt;
	private String songUrl;
	private String title;

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

	public ZonedDateTime getPlayedAt() {
		return playedAt;
	}

	public void setPlayedAt(ZonedDateTime playedAt) {
		this.playedAt = playedAt;
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
}
