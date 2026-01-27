package com.example.claims.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.claims.model.Claim;
import com.example.claims.model.ClaimSummary;
import com.example.claims.model.CreateClaimRequest;
import com.example.claims.service.ClaimsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/claims")
public class ClaimsController {

    private final ClaimsService claimsService;

    @Autowired
    public ClaimsController(ClaimsService claimsService) {
        this.claimsService = claimsService;
    }

    @GetMapping("/")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/{claimId}")
    public ResponseEntity<Claim> getClaim(@PathVariable String claimId) {
        try {
            Claim claim = claimsService.getClaim(claimId);
            return ResponseEntity.ok(claim);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{claimId}/summarize")
    public ResponseEntity<ClaimSummary> summarizeClaim(@PathVariable String claimId) {
        try {
            ClaimSummary summary = claimsService.summarizeClaim(claimId);
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{claimId}/generate")
    public ResponseEntity<String> generateClaimFiles(@PathVariable String claimId) {
        try {
            claimsService.generateClaimFiles(claimId);
            return ResponseEntity.ok("Files generation initiated successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<Claim> createClaim(@Valid @RequestBody CreateClaimRequest request) {
        try {
            Claim createdClaim = claimsService.createClaim(request);
            return ResponseEntity.ok(createdClaim);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}