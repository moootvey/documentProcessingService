package org.itqProj.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.itqProj.enums.DocumentStatusEnum;

import java.time.OffsetDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "document_metadata")
public class DocumentMetadataEntity {
    @Id
    @Column(name = "id", length = 16, unique = true, nullable = false)
    private String id;
    @Column(name = "unique_number", length = 128, unique = true, nullable = false)
    private String uniqueNumber;
    @Column(name = "author", length = 100, nullable = false)
    private String author;
    @Column(name = "title", length = 200, nullable = false)
    private String title;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private DocumentStatusEnum status;
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "document_id", referencedColumnName = "id", insertable = false, updatable = false)
    private List<DocumentHistoryLogEntity> historyLogs;
}
