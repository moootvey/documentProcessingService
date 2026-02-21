package org.itqProj.service.interfaces;

import org.itqProj.dto.model.ApproveResponseModelDto;

public interface ApproveTransactionService {
    ApproveResponseModelDto submitOneDocument(String documentId, String approver, String details);
    ApproveResponseModelDto approveOneDocument(String documentId, String approver, String details);
}
