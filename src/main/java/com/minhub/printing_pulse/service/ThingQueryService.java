package com.minhub.printing_pulse.service;

import com.minhub.printing_pulse.client.dto.ThingiverseDtos.CreatorDto;
import com.minhub.printing_pulse.client.dto.ThingiverseDtos.ThingDto;
import com.minhub.printing_pulse.domain.ThingEntity;
import com.minhub.printing_pulse.repository.ThingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThingQueryService {
    private final ThingRepository repo;

    public List<ThingDto> getAllThings() {
        List<ThingEntity> all = repo.findAll();
        return all.stream().map(this::toDto).toList();
    }

    public List<ThingEntity> getAllThingEntities() {
        return repo.findAll();
    }

    public List<ThingEntity> getTop5ThingEntities() {
        return repo.findTop5ByOrderByCreatedAtDesc();
    }

    public List<ThingEntity> getTop10ByLikes() {
        return repo.findTop10ByOrderByLikeCountDesc();
    }

    private ThingDto toDto(ThingEntity thingEntity) {
        CreatorDto creatorDto = null;
        if (thingEntity.getAuthorName() != null || thingEntity.getAuthorPublicUrl() != null) {
            creatorDto = new CreatorDto(
                null,
                thingEntity.getAuthorName(),
                thingEntity.getAuthorPublicUrl(),
                null
            );
        }

        return new ThingDto(
            thingEntity.getExternalId(),
            thingEntity.getName(),
            thingEntity.getThumbnailUrl(),
            thingEntity.getApiUrl(),
            thingEntity.getPublicUrl(),
            thingEntity.getLikeCount(),
            thingEntity.getAddedAt(),
            thingEntity.getModifiedAt(),
            creatorDto
        );
    }
}


