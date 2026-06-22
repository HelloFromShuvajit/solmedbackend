package com.solmed.solmedbackend.caretaker;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.solmed.solmedbackend.UserMedicine.UserMedicine;
import com.solmed.solmedbackend.UserMedicine.UserMedicineRepository;
import com.solmed.solmedbackend.dose.DoseLog;
import com.solmed.solmedbackend.dose.DoseLogRepository;
import com.solmed.solmedbackend.dose.DoseStatus;
import com.solmed.solmedbackend.medicine.Medicine;
import com.solmed.solmedbackend.user.User;
import com.solmed.solmedbackend.user.UserRepository;
import com.solmed.solmedbackend.user.UserRole;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CaretakerService {

    private final CaretakerLinkRequestRepository linkRequestRepository;
    private final CaretakerRelationshipRepository relationshipRepository;
    private final UserRepository userRepository;
    private final UserMedicineRepository userMedicineRepository;
    private final DoseLogRepository doseLogRepository;
    private static final ZoneId APP_ZONE = ZoneId.of("Asia/Kolkata");

    /**
     * Creates a pending link request from caretaker to patient (owner). Idempotent if already linked or pending.
     */
    @Transactional
    public void createPendingLinkRequest(User caretaker, String patientEmail) {
        if (caretaker.getRole() != UserRole.CARETAKER) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User must be a caretaker.");
        }
        User owner = resolveOwnerFromPatientEmail(patientEmail, caretaker.getEmail());
        if (relationshipRepository.findByOwner_IdAndCaretaker_Id(owner.getId(), caretaker.getId()).isPresent()) {
            return;
        }
        if (linkRequestRepository
                .findByCaretaker_IdAndOwner_IdAndStatus(caretaker.getId(), owner.getId(), InviteStatus.PENDING)
                .isPresent()) {
            return;
        }
        CaretakerLinkRequest req = new CaretakerLinkRequest();
        req.setCaretaker(caretaker);
        req.setOwner(owner);
        req.setStatus(InviteStatus.PENDING);
        linkRequestRepository.save(req);
    }

    @Transactional
    public void requestLinkToPatient(User caretaker, String patientEmail) {
        if (caretaker.getRole() != UserRole.CARETAKER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only caretakers can request a patient link.");
        }
        createPendingLinkRequest(caretaker, patientEmail);
    }

    @Transactional(readOnly = true)
    public List<IncomingLinkRequestDto> listIncomingLinkRequests(User owner) {
        if (owner.getRole() == UserRole.CARETAKER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only patients can view incoming link requests.");
        }
        return linkRequestRepository.findByOwner_IdAndStatus(owner.getId(), InviteStatus.PENDING).stream()
                .map(r -> {
                    User c = r.getCaretaker();
                    return new IncomingLinkRequestDto(
                            r.getId(),
                            c.getId(),
                            c.getEmail(),
                            c.getName(),
                            r.getCreatedAt());
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OutgoingLinkRequestDto> listMyOutgoingLinkRequests(User caretaker) {
        if (caretaker.getRole() != UserRole.CARETAKER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only caretakers have outgoing link requests.");
        }
        return linkRequestRepository.findByCaretaker_IdOrderByCreatedAtDesc(caretaker.getId()).stream()
                .map(r -> {
                    User o = r.getOwner();
                    return new OutgoingLinkRequestDto(
                            r.getId(),
                            o.getEmail(),
                            o.getName(),
                            r.getStatus(),
                            r.getCreatedAt());
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void acceptLinkRequest(User owner, Long requestId) {
        if (owner.getRole() == UserRole.CARETAKER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the patient can accept.");
        }
        CaretakerLinkRequest req = linkRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Link request not found."));
        if (!req.getOwner().getId().equals(owner.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your link request.");
        }
        if (req.getStatus() != InviteStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request is not pending.");
        }
        linkRelationship(req.getOwner(), req.getCaretaker());
        req.setStatus(InviteStatus.ACCEPTED);
        linkRequestRepository.save(req);
    }

    @Transactional
    public void rejectLinkRequest(User owner, Long requestId) {
        if (owner.getRole() == UserRole.CARETAKER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the patient can reject.");
        }
        CaretakerLinkRequest req = linkRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Link request not found."));
        if (!req.getOwner().getId().equals(owner.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your link request.");
        }
        if (req.getStatus() != InviteStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request is not pending.");
        }
        req.setStatus(InviteStatus.REJECTED);
        linkRequestRepository.save(req);
    }

    private User resolveOwnerFromPatientEmail(String patientEmailRaw, String caretakerEmail) {
        String email = patientEmailRaw == null ? "" : patientEmailRaw.trim().toLowerCase();
        if (email.isEmpty() || !email.contains("@")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valid patient email required.");
        }
        if (email.equalsIgnoreCase(caretakerEmail != null ? caretakerEmail.trim().toLowerCase() : "")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Patient email must differ from your own.");
        }
        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No account found for that email."));
        if (owner.getRole() != UserRole.OWNER) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "That email is not a patient account.");
        }
        return owner;
    }

    private void linkRelationship(User owner, User caretaker) {
        if (relationshipRepository.findByOwner_IdAndCaretaker_Id(owner.getId(), caretaker.getId()).isPresent()) {
            return;
        }
        CaretakerRelationship rel = new CaretakerRelationship();
        rel.setOwner(owner);
        rel.setCaretaker(caretaker);
        relationshipRepository.save(rel);
    }

    @Transactional
    public void revokeCaretaker(User owner, Long caretakerUserId) {
        relationshipRepository.deleteByOwner_IdAndCaretaker_Id(owner.getId(), caretakerUserId);
    }

    @Transactional(readOnly = true)
    public List<CaretakerSummaryDto> listCaretakers(User owner) {
        return relationshipRepository.findByOwner_Id(owner.getId()).stream()
                .map(r -> new CaretakerSummaryDto(
                        r.getCaretaker().getId(),
                        r.getCaretaker().getEmail(),
                        r.getCaretaker().getName()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PatientSummaryDto> listPatientsForCaretaker(User caretaker) {
        LocalDate today = LocalDate.now(APP_ZONE);
        return relationshipRepository.findByCaretaker_Id(caretaker.getId()).stream()
                .map(r -> {
                    User o = r.getOwner();
                    List<DoseLog> todayLogs = doseLogRepository.findByUser_IdAndDate(o.getId(), today);
                    int taken = (int) todayLogs.stream().filter(l -> l.getStatus() == DoseStatus.TAKEN).count();
                    int missed = (int) todayLogs.stream().filter(l -> l.getStatus() == DoseStatus.MISSED).count();
                    int pending = (int) todayLogs.stream().filter(l -> l.getStatus() == DoseStatus.PENDING).count();
                    return new PatientSummaryDto(o.getId(), o.getName(), o.getEmail(), taken, missed, pending);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserMedicine> getMedicinesForPatient(User caretaker, Long ownerId) {
        assertCaretakerLinked(caretaker, ownerId);
        return userMedicineRepository.findByUser_Id(ownerId);
    }

    @Transactional(readOnly = true)
    public AdherenceReportDto adherenceForPatient(User caretaker, Long ownerId, int days) {
        assertCaretakerLinked(caretaker, ownerId);
        LocalDate end = LocalDate.now(APP_ZONE);
        LocalDate start = end.minusDays(Math.max(1, days) - 1L);
        return adherenceForPatientInternal(ownerId, start, end);
    }

    private void assertCaretakerLinked(User caretaker, Long ownerId) {
        relationshipRepository.findByOwner_IdAndCaretaker_Id(ownerId, caretaker.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Not linked to this patient."));
    }

    private AdherenceReportDto adherenceForPatientInternal(Long ownerId, LocalDate start, LocalDate end) {
        List<DoseLog> logs = doseLogRepository.findByUser_IdAndDateBetween(ownerId, start, end);
        List<AdherenceDayDto> out = new ArrayList<>();
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dtFmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            LocalDate cur = d;
            List<DoseLog> dayLogs = logs.stream().filter(l -> l.getDate().equals(cur)).collect(Collectors.toList());
            int total = dayLogs.size();
            int taken = (int) dayLogs.stream().filter(l -> l.getStatus() == DoseStatus.TAKEN).count();
            int missed = (int) dayLogs.stream().filter(l -> l.getStatus() == DoseStatus.MISSED).count();
            List<AdherenceMedicineRowDto> rows = new ArrayList<>();
            for (DoseLog log : dayLogs.stream().sorted(Comparator.comparing(DoseLog::getScheduledTime)).toList()) {
                UserMedicine um = userMedicineRepository.findById(log.getUserMedicineId()).orElse(null);
                Medicine m = um != null ? um.getMedicine() : null;
                String name = m != null ? m.getMedName() : "?";
                String takenAt = log.getTakenAt() != null ? log.getTakenAt().format(dtFmt) : null;
                rows.add(new AdherenceMedicineRowDto(
                        name,
                        log.getStatus().name(),
                        log.getScheduledTime() != null ? log.getScheduledTime().format(timeFmt) : "",
                        takenAt));
            }
            out.add(new AdherenceDayDto(cur.toString(), total, taken, missed, rows));
        }
        return new AdherenceReportDto(out);
    }
}
