package com.baoopn.my_portfolio_be.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.baoopn.my_portfolio_be.utils.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class MessageService {
	@Value("${TELEGRAM_BOT_TOKEN}")
	private String botToken;

	@Value("${TELEGRAM_CHAT_ID}")
	private String chatId;

	// Email regex pattern
	private static final Pattern EMAIL_PATTERN = Pattern.compile(Constants.EMAIL_REGEX);

	// Phone number regex pattern - supports various formats
	private static final Pattern PHONE_PATTERN = Pattern.compile(Constants.PHONE_REGEX);

	public boolean sendTelegramMessage(String name, String contact, String subject, String message,
			String contactType) {

		// Format the message with the contact type
		String formattedMessage = String.format(
				Constants.TELEGRAM_MESSAGE_FORMAT,
				escapeMarkdown(name),
				contactType,
				escapeMarkdown(contact),
				escapeMarkdown(subject),
				escapeMarkdown(message));

		// Send the message to Telegram
		try {
			// Create RestTemplate instance
			RestTemplate restTemplate = new RestTemplate();

			// Set the URL for the Telegram Bot API
			String telegramApiUrl = Constants.TELEGRAM_API_BASE_URL + botToken
					+ Constants.TELEGRAM_SEND_MESSAGE_ENDPOINT;

			// Set headers
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			// Create the request body
			Map<String, Object> requestBody = new HashMap<>();
			requestBody.put("chat_id", chatId);
			requestBody.put("text", formattedMessage);
			requestBody.put("parse_mode", "MarkdownV2");

			// Create the HTTP entity with headers and body
			HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

			// Send the POST request
			ResponseEntity<String> response = restTemplate.postForEntity(
					telegramApiUrl,
					entity,
					String.class);

			// Check if the request was successful
			return response.getStatusCode().is2xxSuccessful();

		} catch (RestClientException e) {
			return false;
		}
	}

	public String determineContactType(String contact) {
		if (contact == null || contact.trim().isEmpty()) {
			return "unknown";
		}

		if (EMAIL_PATTERN.matcher(contact).matches()) {
			return "email";
		} else if (PHONE_PATTERN.matcher(contact).matches()) {
			return "phone";
		} else {
			return "unknown";
		}
	}

	/**
	 * Escapes special Markdown characters in a text string.
	 * This prevents characters like _, *, [, ], etc. from being interpreted as
	 * Markdown formatting.
	 * 
	 * @param text The text to escape
	 * @return The escaped text with backslashes before special Markdown characters
	 */
	private String escapeMarkdown(String text) {
		if (text == null) {
			return "";
		}

		return text.replaceAll(Constants.MARKDOWN_SPECIAL_CHARS, "\\\\$1");
	}
}
