package org.itqProj.util;

import org.itqProj.dto.model.ApproveResponseModelDto;
import org.itqProj.dto.model.DocumentResponseModelDto;
import org.itqProj.dto.response.ApproveResponseDto;
import org.itqProj.dto.response.DocumentHistoryResponseDto;
import org.itqProj.dto.response.GetDocumentsResponseDto;
import org.itqProj.dto.response.GetSingleDocumentResponseDto;
import org.itqProj.entity.DocumentHistoryLogEntity;
import org.itqProj.entity.DocumentMetadataEntity;
import org.itqProj.enums.ApproveStatusEnum;
import org.springframework.data.domain.Page;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class MapperUtil {
    public static final String DOC_ALREADY_SUBMITTED = "Document has already been submitted or approved.";
    public static final String DOC_SUCCESSFULLY_SUBMITTED = "Document has been successfully submitted.";
    public static final String DOC_SUCCESSFULLY_APPROVED = "Document has been successfully approved.";
    public static final String DOC_NOT_SUBMITTED = "Document is not in a state that can be approved.";
    public static final String DOC_NOT_FOUND = "Document does not exist.";
    public static final String DOC_ERROR_REGISTER_APPROVAL = "An error occurred while registering the approval action.";

    public static <T> T convert(Class<T> clazz, Object ... objects) {
        if (clazz.equals(DocumentResponseModelDto.class)) {
            return (T) mapToDocumentResponseDto((DocumentMetadataEntity) objects[0]);
        } else if (clazz.equals(GetSingleDocumentResponseDto.class)) {
            return (T) mapToGetSingleDocumentResponseDto((DocumentMetadataEntity) objects[0]);
        } else if (clazz.equals(GetDocumentsResponseDto.class)) {
            return (T) mapToGetDocumentsResponseDto((Page<DocumentMetadataEntity>) objects[0]);
        } else if (clazz.equals(ApproveResponseModelDto.class)) {
            return (T) mapToApproveResponseModelDto((String) objects[0], (ApproveStatusEnum) objects[1], (String) objects[2]);
        } else if (clazz.equals(ApproveResponseDto.class)) {
            return (T) mapToApproveResponseDto((String) objects[0], (List<ApproveResponseModelDto>) objects[1]);
        }
        throw new IllegalArgumentException("Unsupported class type for mapping: " + clazz.getName());
    }

    private static DocumentResponseModelDto mapToDocumentResponseDto(DocumentMetadataEntity documentMetadataEntity) {
        return new DocumentResponseModelDto(
                documentMetadataEntity.getId(),
                documentMetadataEntity.getUniqueNumber(),
                documentMetadataEntity.getStatus(),
                documentMetadataEntity.getAuthor(),
                documentMetadataEntity.getTitle(),
                documentMetadataEntity.getCreatedAt() != null ? OffsetDateTime.now(ZoneOffset.UTC) : null,
                documentMetadataEntity.getUpdatedAt() != null ? OffsetDateTime.now(ZoneOffset.UTC) : null
        );
    }

    private static GetSingleDocumentResponseDto mapToGetSingleDocumentResponseDto(DocumentMetadataEntity documentMetadataEntity) {
        return new GetSingleDocumentResponseDto(
                mapToDocumentResponseDto(documentMetadataEntity),
                documentMetadataEntity.getHistoryLogs()
                        .stream()
                        .map(MapperUtil::mapToDocumentHistoryResponseDto)
                        .toList());
    }

    private static DocumentHistoryResponseDto mapToDocumentHistoryResponseDto(DocumentHistoryLogEntity documentHistoryLogEntity) {
        return new DocumentHistoryResponseDto(
                documentHistoryLogEntity.getId(),
                documentHistoryLogEntity.getPerformedBy(),
                documentHistoryLogEntity.getTimestamp(),
                documentHistoryLogEntity.getAction(),
                documentHistoryLogEntity.getDetails()
        );
    }

    private static GetDocumentsResponseDto mapToGetDocumentsResponseDto(Page<DocumentMetadataEntity> pageable) {
        return new GetDocumentsResponseDto(
                pageable.getContent()
                        .stream()
                        .map(MapperUtil::mapToDocumentResponseDto)
                        .toList(),
                PageableUtil.fromPage(pageable));
    }

    private static ApproveResponseModelDto mapToApproveResponseModelDto(String documentId, ApproveStatusEnum status, String message) {
        return new ApproveResponseModelDto(documentId, status, message);
    }

    private static ApproveResponseDto mapToApproveResponseDto(String approverId, List<ApproveResponseModelDto> responses) {
        return new ApproveResponseDto(approverId, responses);
    }
}
