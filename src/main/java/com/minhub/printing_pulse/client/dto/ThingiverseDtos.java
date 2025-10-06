package com.minhub.printing_pulse.client.dto;

import java.time.OffsetDateTime;

public final class ThingiverseDtos {
    public record CreatorDto(
            Long id, 
            String name, 
            String public_url, 
            String thumbnail
    ) {}
    
    public record ThingDto(
            Long id,
            String name,
            String thumbnail,
            String url,
            String public_url,
            Integer like_count,
            OffsetDateTime added,
            OffsetDateTime modified,
            CreatorDto creator
    ) {}
}
