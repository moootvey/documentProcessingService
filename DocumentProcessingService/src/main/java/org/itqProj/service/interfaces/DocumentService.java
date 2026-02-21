package org.itqProj.service.interfaces;

import jakarta.validation.constraints.NotNull;
import org.itqProj.dto.model.DocumentResponseModelDto;
import org.itqProj.dto.response.GetDocumentsResponseDto;
import org.itqProj.dto.response.GetSingleDocumentResponseDto;
import org.itqProj.enums.DocumentStatusEnum;

import java.time.OffsetDateTime;
import java.util.List;

public interface DocumentService {
    DocumentResponseModelDto createDocument(@NotNull String author, @NotNull String title);
    GetSingleDocumentResponseDto getDocumentWithHistory(@NotNull String documentId, Boolean withHistory);
    GetDocumentsResponseDto getDocuments(@NotNull List<String> documentIds, Integer page, Integer size, List<String> sort);
    GetDocumentsResponseDto searchDocuments(Integer page, Integer size, List<String> sort, DocumentStatusEnum status, String author, OffsetDateTime fromDate, OffsetDateTime toDate);
}
