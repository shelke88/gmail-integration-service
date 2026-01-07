package com.ss.gmailcount.controller;

import com.ss.gmailcount.service.GmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/gmail")
public class GmailController {

    private final GmailService gmailService;

    public GmailController(GmailService gmailService) {

        this.gmailService = gmailService;
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> count(
            @RequestParam String sender) throws Exception {

        long count = gmailService.countEmails(sender);

        return ResponseEntity.ok(
                Map.of(
                        "sender", sender,
                        "count", count
                )
        );
    }
}
