package org.itqProj.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.itqProj.enums.ActionsEnum;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "document_history_log")
public class DocumentHistoryLogEntity {
    @Id
    @Column(name = "id", length = 20, unique = true, nullable = false)
    private String id;
    @Column(name = "document_id", length = 16, nullable = false)
    private String documentId;
    @Column(name = "performed_by", length = 100, nullable = false)
    private String performedBy;
    @Column(name = "timestamp", nullable = false)
    private OffsetDateTime timestamp;
    @Column(name = "action", nullable = false)
    @Enumerated(EnumType.STRING)
    private ActionsEnum action;
    @Column(name = "details", length = 255)
    private String details;
}
