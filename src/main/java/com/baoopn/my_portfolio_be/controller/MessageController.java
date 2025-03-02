package com.baoopn.my_portfolio_be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.baoopn.my_portfolio_be.model.MessageRequest;
import com.baoopn.my_portfolio_be.model.Response;
import com.baoopn.my_portfolio_be.service.MessageService;

@RestController
public class MessageController {
	@Autowired
	private MessageService messageService;

	@PostMapping("/api/message")
	public ResponseEntity<Response> sendMessage(@RequestBody MessageRequest messageRequest) {
		// Extract fields from the request
		String name = messageRequest.getName();
		String contact = messageRequest.getContact();
		String subject = messageRequest.getSubject();
		String message = messageRequest.getMessage();

		if (name == null || name.trim().isEmpty() || message == null || message.trim().isEmpty()
				|| contact == null || contact.trim().isEmpty()) {
			return ResponseEntity.badRequest().body(new Response("Name, contact and message are required", false));
		}

		if (subject == null || subject.trim().isEmpty()) {
			subject = "(No subject)";
		}

		String contactType = messageService.determineContactType(contact);
		if (contactType.equals("unknown")) {
			return ResponseEntity.badRequest().body(new Response("Invalid contact information", false));
		}

		boolean response = messageService.sendTelegramMessage(name, contact, subject, message, contactType);

		String responseMessage = response ? "Message sent successfully" : "Failed to send message";

		return ResponseEntity.ok(new Response(responseMessage, true));
	}
}
