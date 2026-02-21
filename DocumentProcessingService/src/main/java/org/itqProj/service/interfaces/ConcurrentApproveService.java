package org.itqProj.service.interfaces;

import org.itqProj.dto.response.ConcurrentApproveResponseDto;

public interface ConcurrentApproveService {
    ConcurrentApproveResponseDto approveOneDocumentConcurrentlyTest(String documentId, String approver, String details,  int threadCount, int attemptCount);
}
