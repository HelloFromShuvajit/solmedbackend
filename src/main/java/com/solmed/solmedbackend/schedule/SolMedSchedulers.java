package com.solmed.solmedbackend.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.solmed.solmedbackend.dose.DoseLogService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SolMedSchedulers {

    private final DoseLogService doseLogService;

    /** Every 15 minutes: mark overdue doses missed. */
    @Scheduled(fixedRateString = "${solmed.missed-dose-interval-ms:900000}")
    public void processMissedDoses() {
        doseLogService.processMissedDoses();
    }

    /** Extend rolling dose window nightly (idempotent). */
    @Scheduled(cron = "${solmed.dose-extend-cron:0 15 0 * * ?}", zone = "${solmed.app-zone:Asia/Kolkata}")
    public void extendDoseHorizon() {
        doseLogService.extendHorizonForAllUserMedicines();
    }
}
