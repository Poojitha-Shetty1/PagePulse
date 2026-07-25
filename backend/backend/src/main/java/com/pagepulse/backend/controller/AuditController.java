package com.pagepulse.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.pagepulse.backend.dto.AuditRequest;
import com.pagepulse.backend.dto.AuditResponse;
import com.pagepulse.backend.service.AuditService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @PostMapping("/audit")
    public ResponseEntity<AuditResponse> auditWebsite(
            @Validated @RequestBody AuditRequest request) throws Exception {

        AuditResponse response = auditService.auditWebsite(request.getUrl());

        return ResponseEntity.ok(response);
    }
}