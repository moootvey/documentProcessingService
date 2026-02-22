package org.itqProj.repository;

import org.itqProj.entity.DocumentMetadataEntity;
import org.itqProj.enums.DocumentStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface DocumentMetadataRepository extends JpaRepository<DocumentMetadataEntity, String>, JpaSpecificationExecutor<DocumentMetadataEntity> {
    @Query("SELECT d FROM DocumentMetadataEntity d LEFT JOIN FETCH d.historyLogs WHERE d.id = :documentId")
    Optional<DocumentMetadataEntity> findByIdWithHistory(String documentId);

    @Query("SELECT d FROM DocumentMetadataEntity d WHERE d.id IN :ids")
    Page<DocumentMetadataEntity> findAllByIdIn(List<String> ids, Pageable pageable);

    @Modifying
    @Query("UPDATE DocumentMetadataEntity d SET d.status = :status, d.updatedAt = :updatedAt WHERE d.id = :documentId")
    void updateStatusByDocumentIds(String documentId, DocumentStatusEnum status, OffsetDateTime updatedAt);

    @Modifying
    @Query("UPDATE DocumentMetadataEntity d SET d.status = :newStatus, d.updatedAt = :updatedAt WHERE d.id = :documentId AND d.status = :expectedStatus")
    int updateStatusIfCurrentStatus(@Param("documentId") String documentId, @Param("expectedStatus") DocumentStatusEnum expectedStatus, @Param("newStatus") DocumentStatusEnum newStatus, @Param("updatedAt") OffsetDateTime updatedAt);

    @Query("SELECT EXISTS (SELECT d FROM DocumentMetadataEntity d WHERE d.id = :documentId) AS boolean_value")
    boolean existsByDocumentId(String documentId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT d FROM DocumentMetadataEntity d WHERE d.id = :documentId")
    Optional<DocumentMetadataEntity> findByIdForUpdate(String documentId);

    @Query("SELECT d FROM DocumentMetadataEntity d WHERE d.id = :documentId")
    Optional<DocumentMetadataEntity> findById(String documentId);

    @Query("SELECT d.id FROM DocumentMetadataEntity d WHERE d.status = :status")
    List<String> findIdsByStatus(DocumentStatusEnum status, Pageable pageable);
}