package com.minhub.printing_pulse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsLetterScheduler {
    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private final ThingQueryService queryService;
    private final NewsletterRenderService renderService;
    private final EmailService emailService;

    private final AtomicReference<LocalDate> lastDailySentDate = new AtomicReference<>();
    private final AtomicReference<LocalDate> lastWeeklySentDate = new AtomicReference<>();

    // 매일 18:00 (Asia/Seoul) 정각에 최신 5개로 뉴스레터 전송
    @Scheduled(cron = "0 0 18 * * *", zone = "Asia/Seoul")
    public void sendDailyAt6PM() {
        ZonedDateTime now = ZonedDateTime.now(KST);
        // 오작동 방지: 정확히 18:00 시각이 아니면 무시 (호스트 슬립/타임드리프트 보호)
        if (!(now.getHour() == 18 && now.getMinute() == 0)) {
            log.warn("[Daily] Skipped unexpected trigger at {}", now);
            return;
        }

        LocalDate today = now.toLocalDate();
        if (today.equals(lastDailySentDate.get())) {
            log.info("[Daily] Already sent for {}. Skipping.", today);
            return;
        }

        var items = queryService.getTop5ThingEntities();
        var html = renderService.render(items);
        var subject = "3D Models Newsletter " + today;
        emailService.sendHtml(subject, html, "Your email client does not support HTML.");
        lastDailySentDate.set(today);
        log.info("[Daily] Newsletter sent at {}", now);
    }

    // 매주 금요일 18:00 (Asia/Seoul) 좋아요 순위 상위 10개 전송
    @Scheduled(cron = "0 0 18 * * FRI", zone = "Asia/Seoul")
    public void sendWeeklyAtFriday6PM() {
        ZonedDateTime now = ZonedDateTime.now(KST);
        if (!(now.getHour() == 18 && now.getMinute() == 0)) {
            log.warn("[Weekly] Skipped unexpected trigger at {}", now);
            return;
        }

        LocalDate today = now.toLocalDate();
        if (today.equals(lastWeeklySentDate.get())) {
            log.info("[Weekly] Already sent for {}. Skipping.", today);
            return;
        }

        var items = queryService.getTop10ByLikes();
        var html = renderService.render(items);
        var subject = "Top 10 by Likes Newsletter " + today;
        emailService.sendHtml(subject, html, "Your email client does not support HTML.");
        lastWeeklySentDate.set(today);
        log.info("[Weekly] Newsletter sent at {}", now);
    }
}


