package com.pagepulse.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.jupiter.api.Test;

import com.pagepulse.backend.dto.AuditResponse;

public class AuditServiceTest {

    private final AuditService auditService = new AuditService();

    @Test
    void testAuditWebsiteSuccess() throws IOException {

        AuditResponse response = auditService.auditWebsite("https://example.com");

        assertEquals(200, response.getStatus());
        assertNotNull(response.getTitle());
        assertTrue(response.getWordCount() > 0);

    }

    @Test
    void testInvalidUrl() {

        assertThrows(MalformedURLException.class, () -> {
            auditService.auditWebsite("abc");
        });

    }

    @Test
    void testNonHtmlUrl() {

        assertThrows(IOException.class, () -> {
            auditService.auditWebsite(
                "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf"
            );
        });

    }

}