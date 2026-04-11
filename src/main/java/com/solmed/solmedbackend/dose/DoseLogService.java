package com.solmed.solmedbackend.dose;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;

import com.solmed.solmedbackend.UserMedicine.UserMedicine;
import com.solmed.solmedbackend.UserMedicine.UserMedicineRepository;
import com.solmed.solmedbackend.medicine.Medicine;
import com.solmed.solmedbackend.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoseLogService {

    private static final int DEFAULT_SEED_DAYS = 21;

    private final DoseLogRepository doseLogRepository;
    private final UserMedicineRepository userMedicineRepository;
    @Value("${solmed.app-zone:Asia/Kolkata}")
    private String appZone;

    private ZoneId zone() {
        return ZoneId.of(appZone);
    }

    /**
     * Creates one PENDING dose log per calendar day for the given schedule (rolling window).
     */
    @Transactional
    public void seedDoseLogsForUserMedicine(UserMedicine userMedicine, int daysAhead) {
        if (userMedicine == null || userMedicine.getId() == null) {
            return;
        }
        User user = userMedicine.getUser();
        Medicine medicine = userMedicine.getMedicine();
        LocalTime time = userMedicine.getMedTiming();
        if (user == null || medicine == null || time == null) {
            return;
        }
        LocalDate today = LocalDate.now(zone());
        for (int i = 0; i < daysAhead; i++) {
            LocalDate d = today.plusDays(i);
            List<DoseLog> existing = doseLogRepository.findByUserMedicineIdAndDate(userMedicine.getId(), d);
            if (!existing.isEmpty()) {
                continue;
            }
            DoseLog log = new DoseLog();
            log.setUser(user);
            log.setMedicineId(medicine.getMedId());
            log.setUserMedicineId(userMedicine.getId());
            log.setScheduledTime(time);
            log.setDate(d);
            log.setStatus(DoseStatus.PENDING);
            log.setAlertSent(false);
            doseLogRepository.save(log);
        }
    }

    /** Backfill window when new medicines are added to existing users (idempotent per day). */
    @Transactional
    public void ensureRollingWindowForUserMedicine(Long userMedicineId) {
        userMedicineRepository.findById(userMedicineId).ifPresent(um -> seedDoseLogsForUserMedicine(um, DEFAULT_SEED_DAYS));
    }

    @Transactional
    public void markTakenForUserMedicine(Long userMedicineId) {
        if (userMedicineId == null) {
            return;
        }
        LocalDateTime now = LocalDateTime.now(zone());
        LocalDate today = now.toLocalDate();

        // First choice: today's dose (PENDING preferred, else MISSED) so caretaker sees updates on the current day.
        List<DoseLog> todayLogs = doseLogRepository.findByUserMedicineIdAndDate(userMedicineId, today);
        Optional<DoseLog> todayPending = todayLogs.stream()
                .filter(l -> l.getStatus() == DoseStatus.PENDING)
                .min(Comparator.comparing(DoseLog::getScheduledTime));
        if (todayPending.isPresent()) {
            markTaken(todayPending.get(), now);
            return;
        }
        Optional<DoseLog> todayMissed = todayLogs.stream()
                .filter(l -> l.getStatus() == DoseStatus.MISSED)
                .max(Comparator.comparing(DoseLog::getScheduledTime));
        if (todayMissed.isPresent()) {
            markTaken(todayMissed.get(), now);
            return;
        }

        // Fallback: look back a few days if today's row is missing.
        for (int i = 1; i <= 3; i++) {
            LocalDate d = today.minusDays(i);
            List<DoseLog> logs = doseLogRepository.findByUserMedicineIdAndDate(userMedicineId, d);
            Optional<DoseLog> pendingOrMissed = logs.stream()
                    .filter(l -> l.getStatus() == DoseStatus.PENDING || l.getStatus() == DoseStatus.MISSED)
                    .max(Comparator.comparing(DoseLog::getScheduledTime));
            if (pendingOrMissed.isPresent()) {
                markTaken(pendingOrMissed.get(), now);
                return;
            }
        }
    }

    private void markTaken(DoseLog log, LocalDateTime takenAt) {
        log.setStatus(DoseStatus.TAKEN);
        log.setTakenAt(takenAt);
        doseLogRepository.save(log);
    }

    /** True if this user-medicine has at least one TAKEN dose log for the app's current calendar day. */
    @Transactional(readOnly = true)
    public boolean isTodaysDoseTakenForUserMedicine(Long userMedicineId) {
        if (userMedicineId == null) {
            return false;
        }
        LocalDate today = LocalDate.now(zone());
        return doseLogRepository.findByUserMedicineIdAndDate(userMedicineId, today).stream()
                .anyMatch(l -> l.getStatus() == DoseStatus.TAKEN);
    }

    @Transactional(readOnly = true)
    public List<DoseLog> findDosesForUserInRange(Long userId, LocalDate from, LocalDate to) {
        return doseLogRepository.findByUser_IdAndDateBetween(userId, from, to);
    }

    /**
     * Marks overdue PENDING doses as MISSED (scheduled time + 30 minutes passed).
     */
    @Transactional
    public void processMissedDoses() {
        LocalDateTime now = LocalDateTime.now(zone());
        List<DoseLog> pending = doseLogRepository.findByStatus(DoseStatus.PENDING);
        for (DoseLog d : pending) {
            LocalDateTime scheduled = LocalDateTime.of(d.getDate(), d.getScheduledTime());
            if (now.isAfter(scheduled.plusMinutes(30))) {
                d.setAlertSent(true);
                d.setStatus(DoseStatus.MISSED);
                doseLogRepository.save(d);
            }
        }
    }

    /** Nightly-style backfill: extend horizon for all active user medicines (optional cron). */
    @Transactional
    public void extendHorizonForAllUserMedicines() {
        List<UserMedicine> all = userMedicineRepository.findAll();
        for (UserMedicine um : all) {
            seedDoseLogsForUserMedicine(um, DEFAULT_SEED_DAYS);
        }
    }
}
