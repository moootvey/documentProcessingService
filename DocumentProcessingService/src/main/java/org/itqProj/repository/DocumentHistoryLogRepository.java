package org.itqProj.repository;

import org.itqProj.entity.DocumentHistoryLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentHistoryLogRepository extends JpaRepository<DocumentHistoryLogEntity, String> {
}
