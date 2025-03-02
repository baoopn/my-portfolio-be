package com.baoopn.my_portfolio_be.controller;

import com.baoopn.my_portfolio_be.model.SpotifyCurrentlyPlayingTrackDTO;
import com.baoopn.my_portfolio_be.model.SpotifyRecentlyPlayedTrackDTO;
import com.baoopn.my_portfolio_be.service.SpotifyService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpotifyController {

	@Autowired
	private SpotifyService spotifyService;

	@GetMapping("/api/spotify/currently-playing")
	public SpotifyCurrentlyPlayingTrackDTO getCurrentlyPlayingTrack() {
		return spotifyService.getCurrentlyPlayingTrack();
	}

	@GetMapping("/api/spotify/recently-played")
    public List<SpotifyRecentlyPlayedTrackDTO> getRecentlyPlayedTracks(
		@RequestParam(value = "limit", defaultValue = "5") int limit
	) {
		return spotifyService.getRecentlyPlayedTracks(limit);
	}
}
