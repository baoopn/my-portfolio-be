package com.baoopn.my_portfolio_be.handler;

import com.baoopn.my_portfolio_be.model.SpotifyCurrentlyPlayingTrackDTO;
import com.baoopn.my_portfolio_be.model.SpotifyRecentlyPlayedTrackDTO;
import com.baoopn.my_portfolio_be.service.SpotifyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;

@Component
public class SpotifyWebSocketHandler extends TextWebSocketHandler {

    private final SpotifyService spotifyService;
    private final ObjectMapper objectMapper;

    public SpotifyWebSocketHandler(SpotifyService spotifyService, ObjectMapper objectMapper) {
        this.spotifyService = spotifyService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
        if (payload.equals("currently-playing")) {
            SpotifyCurrentlyPlayingTrackDTO currentlyPlayingTrack = spotifyService.getCurrentlyPlayingTrack();
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(currentlyPlayingTrack)));
        } else if (payload.startsWith("recently-played:")) {
            int limit = Integer.parseInt(payload.split(":")[1]);
            List<SpotifyRecentlyPlayedTrackDTO> recentlyPlayedTracks = spotifyService.getRecentlyPlayedTracks(limit);
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(recentlyPlayedTracks)));
        }
    }
}