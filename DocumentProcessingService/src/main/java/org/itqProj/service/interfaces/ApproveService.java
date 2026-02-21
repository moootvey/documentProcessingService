package org.itqProj.service.interfaces;


import org.itqProj.dto.response.ApproveResponseDto;

import java.util.List;

public interface ApproveService {
    ApproveResponseDto submitDocuments(List<String> documentIds, String approver, String details);
    ApproveResponseDto approveDocuments(List<String> documentIds, String approver, String details);
}
