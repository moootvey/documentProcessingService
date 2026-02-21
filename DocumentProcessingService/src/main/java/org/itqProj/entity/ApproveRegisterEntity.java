package org.itqProj.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "approve_register")
public class ApproveRegisterEntity {
    @Id
    @Column(name = "approve_id", length = 20, unique = true, nullable = false)
    private String approveId;
    @Column(name = "document_id", length = 16, nullable = false)
    private String documentId;
    @Column(name = "approved_by", length = 100, nullable = false)
    private String approvedBy;
    @Column(name = "approved_at", nullable = false)
    private OffsetDateTime approvedAt;
}
