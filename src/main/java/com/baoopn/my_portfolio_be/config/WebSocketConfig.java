package com.baoopn.my_portfolio_be.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.baoopn.my_portfolio_be.handler.SpotifyWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	private final SpotifyWebSocketHandler spotifyWebSocketHandler;

	@Value("${ALLOWED_ORIGINS}")
	private String allowedOrigins;

	public WebSocketConfig(SpotifyWebSocketHandler spotifyWebSocketHandler) {
		this.spotifyWebSocketHandler = spotifyWebSocketHandler;
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(spotifyWebSocketHandler, "/ws/spotify")
				.setAllowedOrigins(allowedOrigins.split(","));
	}
}