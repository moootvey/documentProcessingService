package org.itqProj.repository;

import org.itqProj.entity.DocumentMetadataEntity;
import org.itqProj.enums.DocumentStatusEnum;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;

public class DocumentSpecifications {

    public static Specification<DocumentMetadataEntity> hasStatus(DocumentStatusEnum status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<DocumentMetadataEntity> authorLike(String authorPattern) {
        return (root, query, cb) -> {
            if (authorPattern == null) return null;
            return cb.like(cb.lower(root.get("author")), authorPattern.toLowerCase());
        };
    }

    public static Specification<DocumentMetadataEntity> createdAfter(OffsetDateTime fromDate) {
        return (root, query, cb) -> fromDate == null ? null : cb.greaterThanOrEqualTo(root.get("createdAt"), fromDate);
    }

    public static Specification<DocumentMetadataEntity> createdBefore(OffsetDateTime toDate) {
        return (root, query, cb) -> toDate == null ? null : cb.lessThanOrEqualTo(root.get("createdAt"), toDate);
    }
}
