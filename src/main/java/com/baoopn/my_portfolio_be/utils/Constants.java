package com.baoopn.my_portfolio_be.utils;

public class Constants {
	// Spotify API Endpoints
	public static final String SPOTIFY_TOKEN_ENDPOINT = "https://accounts.spotify.com/api/token";
	public static final String SPOTIFY_API_URL = "https://api.spotify.com/v1/";
	public static final String SPOTIFY_NOW_PLAYING_ENDPOINT = "https://api.spotify.com/v1/me/player/currently-playing";
	public static final String SPOTIFY_RECENTLY_PLAYED_ENDPOINT = "https://api.spotify.com/v1/me/player/recently-played";

	// Spotify Configuration
	public static final int SPOTIFY_RECENTLY_PLAYED_LIMIT = 5;

	// Cache durations
	public static final long TOKEN_CACHE_DURATION_MS = 59 * 60 * 1000; // 59 minutes
	public static final long CURRENTLY_PLAYING_CACHE_DURATION_MS = 1000; // 1 second
	public static final long RECENTLY_PLAYED_CACHE_DURATION_MS = 20 * 1000; // 20 seconds

	// Telegram API Endpoints
	public static final String TELEGRAM_API_BASE_URL = "https://api.telegram.org/bot";
	public static final String TELEGRAM_SEND_MESSAGE_ENDPOINT = "/sendMessage";

	// Telegram Message Format
	public static final String TELEGRAM_MESSAGE_FORMAT = "You have received a new message from your website:\n\n" +
			"*Name:* %s\n" +
			"*Contact \\(%s\\):* `%s`\n" +
			"*Subject:* %s\n\n" +
			"*Message:*\n%s";

	// Regex Patterns
	public static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
	public static final String PHONE_REGEX = "^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$";

	// Markdown special characters for escaping
	public static final String MARKDOWN_SPECIAL_CHARS = "([_*\\[\\]()~`>#+\\-=|{}.!])";
}
