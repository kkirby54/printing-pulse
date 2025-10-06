package com.minhub.printing_pulse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImportScheduler {
    private final ThingImportService importService;

    // 매일 05:00 (Asia/Seoul)
    @Scheduled(cron = "0 0 5 * * *", zone = "Asia/Seoul")
    public void runAt5AM() {
        runImportWithRetry();
    }

    // 매일 17:00 (Asia/Seoul)
    @Scheduled(cron = "0 0 17 * * *", zone = "Asia/Seoul")
    public void runAt5PM() {
        runImportWithRetry();
    }

    private void runImportWithRetry() {
        int maxAttempts = 2;
        long backoffMillis = 1500L;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                var result = importService.importRandomFive();
                log.info("Thing import success on attempt {}/{} ({} items)", attempt, maxAttempts, result.size());
                return;
            } catch (RuntimeException ex) {
                log.warn("Thing import failed on attempt {}/{}: {}", attempt, maxAttempts, ex.getMessage());
                if (attempt == maxAttempts) {
                    log.error("Thing import ultimately failed after {} attempts", maxAttempts, ex);
                    return;
                }
                try {
                    Thread.sleep(backoffMillis);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }
}


