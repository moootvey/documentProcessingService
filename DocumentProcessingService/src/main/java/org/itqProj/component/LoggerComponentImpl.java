package org.itqProj.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.itqProj.component.interfaces.LoggerComponent;
import org.itqProj.dto.model.ApproveResponseModelDto;
import org.itqProj.dto.model.DocumentResponseModelDto;
import org.itqProj.dto.response.ApproveResponseDto;
import org.itqProj.entity.DocumentHistoryLogEntity;
import org.itqProj.enums.ActionsEnum;
import org.itqProj.enums.ApproveStatusEnum;
import org.itqProj.repository.DocumentHistoryLogRepository;
import org.itqProj.util.GeneratorUUIDUtil;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.time.OffsetDateTime;
import java.util.List;

import static java.time.ZoneOffset.UTC;

@Component
@RequiredArgsConstructor
@Aspect
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class LoggerComponentImpl implements LoggerComponent {
    private final DocumentHistoryLogRepository documentHistoryLogRepository;

    @Override
    @Around("@annotation(org.itqProj.annotation.ConsoleLogAnnotation)")
    public Object printLog(ProceedingJoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        Object result = null;
        StopWatch stopWatch = new StopWatch();

        switch (methodName) {
            case "createDocument":
                try {
                    stopWatch.start();
                    log.info("[CREATE_DOC] Creating document with Author: {} and Title: {}.", args[0], args[1]);
                    result = joinPoint.proceed();
                    stopWatch.stop();
                    DocumentResponseModelDto documentResponseModelDto = (DocumentResponseModelDto) result;
                    log.info("[CREATE_DOC] Document created successfully: ID: {}, Number: {}", documentResponseModelDto.getDocumentId(), documentResponseModelDto.getUniqueNumber());
                    log.info("[CREATE_DOC] Document was created in {} seconds.", stopWatch.getTotalTimeSeconds());
                } catch (Throwable throwable) {
                    log.error("[CREATE_DOC] Error creating document: {}", throwable.getMessage());
                    throw new RuntimeException(throwable);
                }
                break;
            case "submitDocuments":
                try {
                    stopWatch.start();
                    log.info("[SUBMIT_DOCS] Submitting batch of {} IDs by {}.", args[0] instanceof List ? ((List<?>) args[0]).size() : 0, args[1]);
                    result = joinPoint.proceed();
                    stopWatch.stop();
                    ApproveResponseDto approveResponseDto = (ApproveResponseDto) result;

                    long submittedDocs = getDocsCount(approveResponseDto, ApproveStatusEnum.SUCCESSFULLY);
                    long conflictedDocs = getDocsCount(approveResponseDto, ApproveStatusEnum.CONFLICT);
                    long notFoundDocs = getDocsCount(approveResponseDto, ApproveStatusEnum.NOT_FOUND);

                    log.info("[SUBMIT_DOCS] Submitted documents: {}, Submit conflicts: {}, Documents not found: {}", submittedDocs, conflictedDocs, notFoundDocs);
                    log.info("[SUBMIT_DOCS] Batch of {} IDs submitted in {} seconds.", approveResponseDto.getApproveRequestResult().size(), stopWatch.getTotalTimeSeconds());
                } catch (Throwable throwable) {
                    log.error("[SUBMIT_DOCS] Error submitting documents: {}", throwable.getMessage());
                    throw new RuntimeException(throwable);
                }
                break;
            case "approveDocuments":
                try {
                    stopWatch.start();
                    log.info("[APPROVE_DOCS] Approving batch of {} IDs by {}.", args[0] instanceof List ? ((List<?>) args[0]).size() : 0, args[1]);
                    result = joinPoint.proceed();
                    stopWatch.stop();

                    ApproveResponseDto approveResponseDto = (ApproveResponseDto) result;

                    long approvedDocs = getDocsCount(approveResponseDto, ApproveStatusEnum.SUCCESSFULLY);
                    long conflictedDocs = getDocsCount(approveResponseDto, ApproveStatusEnum.CONFLICT);
                    long notFoundDocs = getDocsCount(approveResponseDto, ApproveStatusEnum.NOT_FOUND);
                    long registryError = getDocsCount(approveResponseDto, ApproveStatusEnum.ERROR_REGISTRATION_IN_REGISTRY);

                    log.info("[APPROVE_DOCS] Approved documents: {}, Approve conflicts: {}, Documents not found: {}, Registration in registry error: {}", approvedDocs, conflictedDocs, notFoundDocs, registryError);
                    log.info("[APPROVE_DOCS] Batch of {} IDs approved in {} seconds.", approveResponseDto.getApproveRequestResult().size(), stopWatch.getTotalTimeSeconds());
                } catch (Throwable throwable) {
                    log.error("[APPROVE_DOCS] Error approving documents: {}", throwable.getMessage());
                    throw new RuntimeException(throwable);
                }
                break;
        }

        return result;
    }

    @Override
    @Around("@annotation(org.itqProj.annotation.DataBaseLogAnnotation)")
    public Object saveDocumentHistoryLog(ProceedingJoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        Object result = null;

        switch (methodName) {
            case "submitOneDocument":
                try {
                    result = joinPoint.proceed();
                    ApproveResponseModelDto approveResponseModelDto = (ApproveResponseModelDto) result;
                    if (approveResponseModelDto.getApproveStatus().equals(ApproveStatusEnum.SUCCESSFULLY))
                        saveHistoryLog((String) args[0], (String) args[1], ActionsEnum.SUBMIT, (String) args[2]);
                } catch (Throwable throwable) {
                    log.error("[SUBMIT_DOCS] Error saving document history log for document with ID {} : {}", args[0], throwable.getMessage());
                    throw new RuntimeException(throwable);
                }
                break;
            case "approveOneDocument":
                try {
                    result = joinPoint.proceed();
                    ApproveResponseModelDto approveResponseModelDto = (ApproveResponseModelDto) result;
                    if (approveResponseModelDto.getApproveStatus().equals(ApproveStatusEnum.SUCCESSFULLY))
                        saveHistoryLog((String) args[0], (String) args[1], ActionsEnum.APPROVE, (String) args[2]);
                } catch (Throwable throwable) {
                    log.error("[APPROVE_DOCS] Error saving document history log for document with ID {} : {}", args[0], throwable.getMessage());
                    throw new RuntimeException(throwable);
                }
        }
        return result;
    }

    private long getDocsCount(ApproveResponseDto approveResponseDto, ApproveStatusEnum approvedStatus) {
        return approveResponseDto.getApproveRequestResult().stream()
                .filter(approve ->
                        approve.getApproveStatus().equals(approvedStatus))
                .count();
    }

    private void saveHistoryLog(String documentId, String performedBy, ActionsEnum action, String details) {
        DocumentHistoryLogEntity documentHistoryLogEntity = new DocumentHistoryLogEntity(
                GeneratorUUIDUtil.generateUUID(20),
                documentId,
                performedBy,
                OffsetDateTime.now(UTC),
                action,
                details);

        documentHistoryLogRepository.save(documentHistoryLogEntity);
    }
}

