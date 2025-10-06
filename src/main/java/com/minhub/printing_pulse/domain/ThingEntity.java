package com.minhub.printing_pulse.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "things")
@Getter
@Setter
public class ThingEntity {
    @Id
    @Column(name = "external_id")
    private Long externalId; // Thingiverse id

    private String name;
    
    @Column(name = "thumbnail_url")
    private String thumbnailUrl;
    
    @Column(name = "public_url")
    private String publicUrl;
    
    @Column(name = "api_url")
    private String apiUrl;

    @Column(name = "author_name")
    private String authorName;
    
    @Column(name = "author_public_url")
    private String authorPublicUrl;

    @Column(name = "like_count")
    private Integer likeCount;

    @Column(name = "added_at")
    private OffsetDateTime addedAt;
    
    @Column(name = "modified_at")
    private OffsetDateTime modifiedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    public void onPrePersist() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }
}
