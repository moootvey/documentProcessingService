package org.itqProj.service;

import lombok.RequiredArgsConstructor;
import org.itqProj.annotation.ConsoleLogAnnotation;
import org.itqProj.dto.model.DocumentResponseModelDto;
import org.itqProj.dto.response.GetDocumentsResponseDto;
import org.itqProj.dto.response.GetSingleDocumentResponseDto;
import org.itqProj.entity.DocumentMetadataEntity;
import org.itqProj.enums.DocumentStatusEnum;
import org.itqProj.exception.DocumentNotFoundException;
import org.itqProj.repository.DocumentMetadataRepository;
import org.itqProj.repository.DocumentSpecifications;
import org.itqProj.service.interfaces.DocumentService;
import org.itqProj.util.GeneratorUUIDUtil;
import org.itqProj.util.MapperUtil;
import org.itqProj.util.PageableUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final DocumentMetadataRepository documentMetadataRepository;

    @ConsoleLogAnnotation
    @Transactional
    @Override
    public DocumentResponseModelDto createDocument(String author, String title) {
        String generatedUUID = GeneratorUUIDUtil.generateUUID(16);
        while (documentMetadataRepository.existsByDocumentId(generatedUUID))
            generatedUUID = GeneratorUUIDUtil.generateUUID(16);

        return MapperUtil.convert(DocumentResponseModelDto.class,
                documentMetadataRepository.save(DocumentMetadataEntity.builder()
                        .id(generatedUUID)
                        .uniqueNumber(GeneratorUUIDUtil.generateDocumentUniqueNum(generatedUUID))
                        .author(author)
                        .title(title)
                        .status(DocumentStatusEnum.DRAFT)
                        .createdAt(OffsetDateTime.now(ZoneOffset.UTC))
                        .updatedAt(OffsetDateTime.now(ZoneOffset.UTC))
                        .build()
                )
        );
    }

    @Transactional(readOnly = true)
    @Override
    public GetSingleDocumentResponseDto getDocumentWithHistory(String documentId, Boolean withHistory) {
        if (withHistory == null || withHistory) return MapperUtil.convert(GetSingleDocumentResponseDto.class,
                        documentMetadataRepository.findByIdWithHistory(documentId).orElseThrow(() -> new DocumentNotFoundException(documentId)));
        else return MapperUtil.convert(GetSingleDocumentResponseDto.class,
                documentMetadataRepository.findById(documentId).orElseThrow(() -> new DocumentNotFoundException(documentId)));
    }

    @Transactional(readOnly = true)
    @Override
    public GetDocumentsResponseDto getDocuments(List<String> documentIds, Integer page, Integer size, List<String> sort) {
        if (documentIds == null || documentIds.isEmpty()) return MapperUtil.convert(GetDocumentsResponseDto.class, new ArrayList<>(), Page.empty());
        Pageable pageable = PageableUtil.buildPageable(page, size, sort, documentIds.size());
        return MapperUtil.convert(GetDocumentsResponseDto.class, documentMetadataRepository.findAllByIdIn(documentIds, pageable));
    }

    @Transactional(readOnly = true)
    @Override
    public GetDocumentsResponseDto searchDocuments(Integer page, Integer size, List<String> sort, DocumentStatusEnum status, String author, OffsetDateTime fromDate, OffsetDateTime toDate) {
        Pageable pageable = PageableUtil.buildPageable(page, size, sort, documentMetadataRepository.count());
        String authorPattern = (author == null) ? null : author + "%";

        Specification<DocumentMetadataEntity> spec = Specification.where(DocumentSpecifications.hasStatus(status))
                .and(DocumentSpecifications.authorLike(authorPattern))
                .and(DocumentSpecifications.createdAfter(fromDate))
                .and(DocumentSpecifications.createdBefore(toDate));

        return MapperUtil.convert(GetDocumentsResponseDto.class, documentMetadataRepository.findAll(spec, pageable));
    }
}
