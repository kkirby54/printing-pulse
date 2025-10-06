package com.minhub.printing_pulse.service;

import com.minhub.printing_pulse.client.ThingiverseClient;
import com.minhub.printing_pulse.client.dto.ThingiverseDtos.ThingDto;
import com.minhub.printing_pulse.domain.ThingEntity;
import com.minhub.printing_pulse.repository.ThingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ThingImportService {
    private final ThingiverseClient client;
    private final ThingRepository repo;

    public List<ThingEntity> importRandomFive() {
        try {
            var dtos = client.getRandomThings();
            var entities = dtos.stream().limit(5).map(this::toEntity).toList();

            // 이미 존재하는 externalId는 제외하여 중복 저장을 방지한다
            Set<Long> ids = entities.stream().map(ThingEntity::getExternalId).collect(Collectors.toSet());
            var existing = repo.findAllById(ids).stream()
                .map(ThingEntity::getExternalId)
                .collect(Collectors.toSet());

            var newEntities = entities.stream()
                .filter(e -> !existing.contains(e.getExternalId()))
                .toList();

            if (!newEntities.isEmpty()) {
                repo.saveAll(newEntities);
            }
            return newEntities;
        } catch (Exception e) {
            log.warn("Thingiverse API 호출 실패 {}", e.getMessage());
            throw new RuntimeException();
        }
    }

    private ThingEntity toEntity(ThingDto dto) {
        var e = new ThingEntity();
        e.setExternalId(dto.id());
        e.setName(dto.name());
        e.setThumbnailUrl(dto.thumbnail());
        e.setPublicUrl(dto.public_url());
        e.setApiUrl(dto.url());
        e.setAuthorName(dto.creator() != null ? dto.creator().name() : null);
        e.setAuthorPublicUrl(dto.creator() != null ? dto.creator().public_url() : null);
        e.setLikeCount(dto.like_count());
        e.setAddedAt(dto.added());
        e.setModifiedAt(dto.modified());
        return e;
    }

}
