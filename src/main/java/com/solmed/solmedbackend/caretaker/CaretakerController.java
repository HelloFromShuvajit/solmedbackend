package com.solmed.solmedbackend.caretaker;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.solmed.solmedbackend.UserMedicine.UserMedicine;
import com.solmed.solmedbackend.user.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/caretaker")
@RequiredArgsConstructor
public class CaretakerController {

    private final CaretakerService caretakerService;

    @PostMapping("/request-link")
    public ResponseEntity<Void> requestLink(
            @AuthenticationPrincipal User caretaker,
            @RequestBody PatientEmailRequestDto body) {
        String email = body != null ? body.getPatientEmail() : null;
        caretakerService.requestLinkToPatient(caretaker, email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/incoming-link-requests")
    public ResponseEntity<List<IncomingLinkRequestDto>> incomingLinkRequests(@AuthenticationPrincipal User owner) {
        return ResponseEntity.ok(caretakerService.listIncomingLinkRequests(owner));
    }

    @PostMapping("/link-requests/{requestId}/accept")
    public ResponseEntity<Void> acceptLinkRequest(
            @AuthenticationPrincipal User owner,
            @PathVariable Long requestId) {
        caretakerService.acceptLinkRequest(owner, requestId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/link-requests/{requestId}/reject")
    public ResponseEntity<Void> rejectLinkRequest(
            @AuthenticationPrincipal User owner,
            @PathVariable Long requestId) {
        caretakerService.rejectLinkRequest(owner, requestId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my-link-requests")
    public ResponseEntity<List<OutgoingLinkRequestDto>> myLinkRequests(@AuthenticationPrincipal User caretaker) {
        return ResponseEntity.ok(caretakerService.listMyOutgoingLinkRequests(caretaker));
    }

    @DeleteMapping("/revoke/{caretakerId}")
    public ResponseEntity<Void> revoke(@AuthenticationPrincipal User owner, @PathVariable Long caretakerId) {
        caretakerService.revokeCaretaker(owner, caretakerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<CaretakerSummaryDto>> list(@AuthenticationPrincipal User owner) {
        return ResponseEntity.ok(caretakerService.listCaretakers(owner));
    }

    @GetMapping("/patients")
    public ResponseEntity<List<PatientSummaryDto>> patients(@AuthenticationPrincipal User caretaker) {
        return ResponseEntity.ok(caretakerService.listPatientsForCaretaker(caretaker));
    }

    @GetMapping("/patient/{ownerId}/medicines")
    public ResponseEntity<List<UserMedicine>> medicines(
            @AuthenticationPrincipal User caretaker,
            @PathVariable Long ownerId) {
        return ResponseEntity.ok(caretakerService.getMedicinesForPatient(caretaker, ownerId));
    }

    @GetMapping("/patient/{ownerId}/adherence")
    public ResponseEntity<AdherenceReportDto> adherence(
            @AuthenticationPrincipal User caretaker,
            @PathVariable Long ownerId,
            @RequestParam(value = "days", defaultValue = "7") int days) {
        return ResponseEntity.ok(caretakerService.adherenceForPatient(caretaker, ownerId, days));
    }
}
