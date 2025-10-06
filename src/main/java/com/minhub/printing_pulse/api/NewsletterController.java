package com.minhub.printing_pulse.api;

import com.minhub.printing_pulse.domain.ThingEntity;
import com.minhub.printing_pulse.service.EmailService;
import com.minhub.printing_pulse.service.NewsletterRenderService;
import com.minhub.printing_pulse.service.ThingQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/newsletters")
@RequiredArgsConstructor
public class NewsletterController {
    private final ThingQueryService queryService;
    private final NewsletterRenderService renderService;
    private final EmailService emailService;

    @PostMapping("/send")
    public Map<String, Object> send(
            @RequestParam(defaultValue = "3D Models Newsletter")
            String subjectPrefix
    ) {
        // 최신 5개만 조회
        var items = queryService.getTop5ThingEntities();
        var html = renderService.render(items);
        var subject = subjectPrefix + " " + LocalDate.now();
        emailService.sendHtml(subject, html, "Your email client does not support HTML.");
        return Map.of("sent", true, "count", items.size(), "subject", subject);
    }

    @GetMapping("/preview")
    public ResponseEntity<String> preview() {
        // 최신 5개만 조회
        var items = queryService.getTop5ThingEntities();
        var html = renderService.render(items);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }

    @GetMapping("/weekly/preview")
    public ResponseEntity<String> weeklyPreview() {
        // 좋아요 순위 상위 10개 조회
        var items = queryService.getTop10ByLikes();
        var html = renderService.render(items);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }
}
