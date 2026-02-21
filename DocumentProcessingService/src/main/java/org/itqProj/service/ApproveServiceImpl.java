package org.itqProj.service;

import lombok.RequiredArgsConstructor;
import org.itqProj.annotation.ConsoleLogAnnotation;
import org.itqProj.dto.model.ApproveResponseModelDto;
import org.itqProj.dto.response.ApproveResponseDto;
import org.itqProj.service.interfaces.ApproveService;
import org.itqProj.service.interfaces.ApproveTransactionService;
import org.itqProj.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApproveServiceImpl implements ApproveService {
    private final ApproveTransactionService approveTransactionalService;

    @ConsoleLogAnnotation
    @Override
    public ApproveResponseDto submitDocuments(List<String> documentIds, String approver, String details) {
        List<ApproveResponseModelDto> approveResponseModelDtos = new ArrayList<>();
        documentIds.forEach(documentId -> approveResponseModelDtos.add(approveTransactionalService.submitOneDocument(documentId, approver, details)));
        return MapperUtil.convert(ApproveResponseDto.class, approver, approveResponseModelDtos);
    }

    @ConsoleLogAnnotation
    @Override
    public ApproveResponseDto approveDocuments(List<String> documentIds, String approver, String details) {
        List<ApproveResponseModelDto> approveResponseModelDtos = new ArrayList<>();
        documentIds.forEach(documentId -> approveResponseModelDtos.add(approveTransactionalService.approveOneDocument(documentId, approver, details)));
        return MapperUtil.convert(ApproveResponseDto.class, approver, approveResponseModelDtos);
    }
}
