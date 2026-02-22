package org.itqProj.service;

import org.itqProj.dto.model.ApproveResponseModelDto;
import org.itqProj.dto.response.ApproveResponseDto;
import org.itqProj.enums.ApproveStatusEnum;
import org.itqProj.service.interfaces.ApproveTransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class ApproveServiceImplTest {

    @Mock
    private ApproveTransactionService approveTransactionService;

    @InjectMocks
    private ApproveServiceImpl approveService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void submitSingleDocument_HappyPath() {
        when(approveTransactionService
                .submitOneDocument(
                        anyString(),
                        anyString(),
                        anyString()))
                .thenReturn(new ApproveResponseModelDto(
                        "24dba5928c9b4f59",
                        ApproveStatusEnum.SUCCESSFULLY,
                        "ok"));

        ApproveResponseDto response = approveService.submitDocuments(
                List.of("24dba5928c9b4f59"),
                "John Doe",
                "Details test mess");

        assertEquals(1, response.getApproveRequestResult().size());
        assertEquals(ApproveStatusEnum.SUCCESSFULLY, response.getApproveRequestResult().get(0).getApproveStatus());
    }

    @Test
    void submitBatch_HappyPath() {
        when(approveTransactionService
                .submitOneDocument(
                        anyString(),
                        anyString(),
                        anyString()))
                .thenAnswer(
                        invocation ->
                                new ApproveResponseModelDto((String) invocation.getArgument(0), ApproveStatusEnum.SUCCESSFULLY, "ok"));

        ApproveResponseDto response = approveService.submitDocuments(
                List.of("24dba5928c9b4f59", "05cc11bfe69145fa", "c8c5cf480db84d70"),
                "John Doe",
                "Details test mess");

        assertEquals(3, response.getApproveRequestResult().size());
        assertEquals("24dba5928c9b4f59", response.getApproveRequestResult().get(0).getDocumentId());
        assertEquals("05cc11bfe69145fa", response.getApproveRequestResult().get(1).getDocumentId());
        assertEquals("c8c5cf480db84d70", response.getApproveRequestResult().get(2).getDocumentId());
    }

    @Test
    void approveBatch_PartialResults() {
        when(approveTransactionService
                .approveOneDocument(
                        "c8c5cf480db84d70",
                        "John Doe",
                        "Details test mess"))
                .thenReturn(
                        new ApproveResponseModelDto(
                                "c8c5cf480db84d70",
                                ApproveStatusEnum.SUCCESSFULLY,
                                "ok"));

        when(approveTransactionService
                .approveOneDocument(
                        "05cc11bfe69145fa",
                        "John Doe",
                        "Details test mess"))
                .thenReturn(
                        new ApproveResponseModelDto(
                                "05cc11bfe69145fa",
                                ApproveStatusEnum.CONFLICT,
                                "conflict"));

        ApproveResponseDto response = approveService.approveDocuments(
                List.of("c8c5cf480db84d70", "05cc11bfe69145fa"),
                "John Doe",
                "Details test mess");

        assertEquals(2, response.getApproveRequestResult().size());
        assertEquals(ApproveStatusEnum.SUCCESSFULLY, response.getApproveRequestResult().get(0).getApproveStatus());
        assertEquals(ApproveStatusEnum.CONFLICT, response.getApproveRequestResult().get(1).getApproveStatus());
    }
}

