package org.itqProj.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.itqProj.annotation.DataBaseLogAnnotation;
import org.itqProj.dto.model.ApproveResponseModelDto;
import org.itqProj.entity.ApproveRegisterEntity;
import org.itqProj.entity.DocumentMetadataEntity;
import org.itqProj.enums.ApproveStatusEnum;
import org.itqProj.enums.DocumentStatusEnum;
import org.itqProj.exception.DocumentNotFoundException;
import org.itqProj.repository.ApproveRegisterRepository;
import org.itqProj.repository.DocumentMetadataRepository;
import org.itqProj.service.interfaces.ApproveTransactionService;
import org.itqProj.util.GeneratorUUIDUtil;
import org.itqProj.util.MapperUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.OffsetDateTime;

import static java.time.ZoneOffset.UTC;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApproveTransactionServiceImpl implements ApproveTransactionService {
    private final DocumentMetadataRepository documentMetadataRepository;
    private final ApproveRegisterRepository approveRegisterRepository;

    @DataBaseLogAnnotation
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public ApproveResponseModelDto submitOneDocument(String documentId, String approver, String details) {
        try {
            if (!documentMetadataRepository.findById(documentId).orElseThrow(() ->
                    new DocumentNotFoundException(documentId))
                    .getStatus().equals(DocumentStatusEnum.DRAFT)) {
                return MapperUtil.convert(ApproveResponseModelDto.class, documentId, ApproveStatusEnum.CONFLICT, MapperUtil.DOC_ALREADY_SUBMITTED);
            }
            documentMetadataRepository.updateStatusByDocumentIds(documentId, DocumentStatusEnum.SUBMITTED, OffsetDateTime.now(UTC));

            return MapperUtil.convert(ApproveResponseModelDto.class, documentId, ApproveStatusEnum.SUCCESSFULLY, MapperUtil.DOC_SUCCESSFULLY_SUBMITTED);
        } catch (DocumentNotFoundException ex) {
            return MapperUtil.convert(ApproveResponseModelDto.class, documentId, ApproveStatusEnum.NOT_FOUND, MapperUtil.DOC_NOT_FOUND);
        }
    }

    @DataBaseLogAnnotation
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public ApproveResponseModelDto approveOneDocument(String documentId, String approver, String details) {
        try {
            DocumentMetadataEntity documentMetadataEntity = documentMetadataRepository.findByIdForUpdate(documentId).orElseThrow(() -> new DocumentNotFoundException(documentId));
            if (!documentMetadataEntity.getStatus().equals(DocumentStatusEnum.SUBMITTED)) {
                return MapperUtil.convert(ApproveResponseModelDto.class, documentId, ApproveStatusEnum.CONFLICT, MapperUtil.DOC_NOT_SUBMITTED);
            }
            documentMetadataRepository.updateStatusByDocumentIds(documentId, DocumentStatusEnum.APPROVED, OffsetDateTime.now(UTC));
            approveRegisterRepository.save(new ApproveRegisterEntity(GeneratorUUIDUtil.generateUUID(20), documentId, approver, OffsetDateTime.now(UTC)));

            return MapperUtil.convert(ApproveResponseModelDto.class, documentId, ApproveStatusEnum.SUCCESSFULLY, MapperUtil.DOC_SUCCESSFULLY_APPROVED);
        } catch (DocumentNotFoundException ex) {
            return MapperUtil.convert(ApproveResponseModelDto.class, documentId, ApproveStatusEnum.NOT_FOUND, MapperUtil.DOC_NOT_FOUND);
        } catch (DataAccessException ex) {
            try {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            } catch (Exception txEx) {
                log.warn("Unable to mark transaction as rollback-only: {}", txEx.getMessage());
            }
            log.error("Data access error while approving document {}: {}", documentId, ex.getMessage(), ex);
            return MapperUtil.convert(ApproveResponseModelDto.class, documentId, ApproveStatusEnum.ERROR_REGISTRATION_IN_REGISTRY, MapperUtil.DOC_ERROR_REGISTER_APPROVAL);
        }
    }
}