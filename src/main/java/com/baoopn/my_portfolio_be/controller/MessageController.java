package com.baoopn.my_portfolio_be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.baoopn.my_portfolio_be.model.MessageRequest;
import com.baoopn.my_portfolio_be.model.Response;
import com.baoopn.my_portfolio_be.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class MessageController {
	private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

	@Autowired
	private MessageService messageService;

	@PostMapping("/api/message")
	public ResponseEntity<Response> sendMessage(@RequestBody MessageRequest messageRequest) {
		// Extract fields from the request
		String name = messageRequest.getName();
		String contact = messageRequest.getContact();
		String subject = messageRequest.getSubject();
		String message = messageRequest.getMessage();

		// Validate required fields
		if (name == null || name.trim().isEmpty() || message == null || message.trim().isEmpty()
				|| contact == null || contact.trim().isEmpty()) {
			logger.warn("Message sending failed: Missing required fields");
			return ResponseEntity.badRequest().body(new Response("Name, contact and message are required", false));
		}

		// Use default subject if not provided
		if (subject == null || subject.trim().isEmpty()) {
			subject = "(No subject)";
		}

		// Determine contact type
		String contactType = messageService.determineContactType(contact);
		if (contactType.equals("unknown")) {
			logger.warn("Message sending failed: Invalid contact information");
			return ResponseEntity.badRequest().body(new Response("Invalid contact information", false));
		}

		// Attempt to send the message
		boolean success = messageService.sendTelegramMessage(name, contact, subject, message, contactType);

		// Handle success or failure
		if (success) {
			logger.info("Message sent successfully from: {}, contact: {}", name, contact);
			return ResponseEntity.ok(new Response("Message sent successfully", true));
		} else {
			logger.error("Failed to send message from: {}, contact: {}", name, contact);
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Response("Failed to send message. Please try again later.", false));
		}
	}
}
