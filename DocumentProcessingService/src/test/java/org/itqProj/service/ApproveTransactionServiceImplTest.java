package org.itqProj.service;

import org.itqProj.dto.model.ApproveResponseModelDto;
import org.itqProj.entity.ApproveRegisterEntity;
import org.itqProj.entity.DocumentMetadataEntity;
import org.itqProj.enums.ApproveStatusEnum;
import org.itqProj.enums.DocumentStatusEnum;
import org.itqProj.repository.ApproveRegisterRepository;
import org.itqProj.repository.DocumentMetadataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;

import java.time.OffsetDateTime;
import java.util.Optional;

import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ApproveTransactionServiceImplTest {

    @Mock
    private DocumentMetadataRepository documentMetadataRepository;

    @Mock
    private ApproveRegisterRepository approveRegisterRepository;

    @InjectMocks
    private ApproveTransactionServiceImpl approveTransactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void approveOneDocument_HappyPath() {
        when(documentMetadataRepository
                .findByIdForUpdate("406b41bcc7ba4fce"))
                .thenReturn(Optional.of(new DocumentMetadataEntity(
                        "406b41bcc7ba4fce",
                        "doc_406b41bcc7ba4fce_2026-02-21T09:38:37.459963Z",
                        "John Doe",
                        "Test Document",
                        DocumentStatusEnum.SUBMITTED,
                        OffsetDateTime.now(UTC),
                        OffsetDateTime.now(UTC),
                        null)));

        doNothing()
                .when(documentMetadataRepository)
                .updateStatusByDocumentIds(
                        eq("406b41bcc7ba4fce"),
                        eq(DocumentStatusEnum.APPROVED),
                        any(OffsetDateTime.class));

        when(approveRegisterRepository
                .save(any(ApproveRegisterEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ApproveResponseModelDto result = approveTransactionService
                .approveOneDocument(
                        "406b41bcc7ba4fce",
                        "John Doe",
                        "Test Document");

        assertNotNull(result);
        assertEquals(ApproveStatusEnum.SUCCESSFULLY, result.getApproveStatus());

        verify(documentMetadataRepository, times(1))
                .updateStatusByDocumentIds(
                        eq("406b41bcc7ba4fce"),
                        eq(DocumentStatusEnum.APPROVED),
                        any(OffsetDateTime.class));
        verify(approveRegisterRepository, times(1))
                .save(any(ApproveRegisterEntity.class));
    }

    @Test
    void approveOneDocument_RollbackOnRegisterSaveError() {
        when(documentMetadataRepository
                .findByIdForUpdate("406b41bcc7ba4fce"))
                .thenReturn(Optional.of(new DocumentMetadataEntity(
                        "406b41bcc7ba4fce",
                        "doc_406b41bcc7ba4fce_2026-02-21T09:38:37.459963Z",
                        "John Doe",
                        "Test Document",
                        DocumentStatusEnum.SUBMITTED,
                        OffsetDateTime.now(UTC),
                        OffsetDateTime.now(UTC),
                        null)));

        doNothing()
                .when(documentMetadataRepository)
                .updateStatusByDocumentIds(
                        eq("406b41bcc7ba4fce"),
                        eq(DocumentStatusEnum.APPROVED),
                        any(OffsetDateTime.class));

        when(approveRegisterRepository
                .save(any(ApproveRegisterEntity.class)))
                .thenThrow(new DataAccessException("DB error"){});

        ApproveResponseModelDto result = approveTransactionService
                .approveOneDocument(
                        "406b41bcc7ba4fce",
                        "John Doe",
                        "Test Document");

        assertNotNull(result);
        assertEquals(ApproveStatusEnum.ERROR_REGISTRATION_IN_REGISTRY, result.getApproveStatus());

        verify(documentMetadataRepository, times(1))
                .updateStatusByDocumentIds(
                        eq("406b41bcc7ba4fce"),
                        eq(DocumentStatusEnum.APPROVED),
                        any(OffsetDateTime.class));
        verify(approveRegisterRepository, times(1))
                .save(any(ApproveRegisterEntity.class));
    }

    @Test
    void approveOneDocument_ConflictWhenNotSubmitted() {
        when(documentMetadataRepository
                .findByIdForUpdate("406b41bcc7ba4fce"))
                .thenReturn(Optional.of(new DocumentMetadataEntity(
                        "406b41bcc7ba4fce",
                        "doc_406b41bcc7ba4fce_2026-02-21T09:38:37.459963Z",
                        "John Doe",
                        "Test Document",
                        DocumentStatusEnum.DRAFT,
                        OffsetDateTime.now(UTC),
                        OffsetDateTime.now(UTC),
                        null)));

        ApproveResponseModelDto result = approveTransactionService.approveOneDocument("406b41bcc7ba4fce", "John Doe", "Test Document");

        assertNotNull(result);
        assertEquals(ApproveStatusEnum.CONFLICT, result.getApproveStatus());

        verify(documentMetadataRepository, never())
                .updateStatusByDocumentIds(
                        anyString(),
                        any(),
                        any(OffsetDateTime.class));
        verify(approveRegisterRepository, never())
                .save(any(ApproveRegisterEntity.class));
    }
}

