package com.minhub.printing_pulse.service;

import com.minhub.printing_pulse.domain.ThingEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class NewsletterRenderService {
    private final TemplateEngine templateEngine;

    public String render(List<ThingEntity> things) {
        var ctx = new Context(Locale.getDefault());
        ctx.setVariable("things", things);
        // TODO: add date/branding variables if needed
        return templateEngine.process("newsletter", ctx);
    }
}
