package com.minhub.printing_pulse.repository;

import com.minhub.printing_pulse.domain.ThingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThingRepository extends JpaRepository<ThingEntity, Long> {
    // createdAt 기준 상위 5개
    List<ThingEntity> findTop5ByOrderByCreatedAtDesc();

    // 좋아요 순위 상위 10개
    List<ThingEntity> findTop10ByOrderByLikeCountDesc();
}
