package org.itqProj.worker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.tongfei.progressbar.ProgressBar;
import org.itqProj.enums.DocumentStatusEnum;
import org.itqProj.repository.DocumentMetadataRepository;
import org.itqProj.service.interfaces.ApproveTransactionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApproveWorkerImpl {
    @Value("${workers.batch.size:50}")
    private int batchSize;
    @Value("${workers.approve.name:APPROVE-WORKER}")
    private String workerName;

    private final DocumentMetadataRepository documentMetadataRepository;
    private final ApproveTransactionService approveTransactionService;

    private static final DocumentStatusEnum DOCUMENT_SUBMIT_STATUS = DocumentStatusEnum.SUBMITTED;
    private static final String DETAILS_MESSAGE = "Automated approve by ApproveWorker.";

    @Scheduled(initialDelayString = "${workers.approve.initialDelay:60000}", fixedDelayString = "${workers.approve.fixedDelay:300000}")
    public void processApprovingDocuments() {
        List<String> documentIds = documentMetadataRepository.findIdsByStatus(DOCUMENT_SUBMIT_STATUS, PageRequest.of(0, batchSize));

        if (documentIds.isEmpty()) {
            log.info("[APPROVE-WORKER] No documents found with status SUBMITTED for processing.");
            return;
        }

        int totalDocuments = documentIds.size();
        int successfullyProcessed = 0;
        int conflictDocuments = 0;
        int notFoundDocuments = 0;
        int failedDocuments = 0;

        log.info("[APPROVE-WORKER] Found {} documents with status DRAFT for processing.", documentIds.size());
        try (ProgressBar pb = new ProgressBar("[APPROVE-WORKER]", documentIds.size())) {
            for (String documentId : documentIds) {
                switch (approveTransactionService.approveOneDocument(documentId, workerName, DETAILS_MESSAGE).getApproveStatus()) {
                    case SUCCESSFULLY -> successfullyProcessed++;
                    case CONFLICT -> conflictDocuments++;
                    case NOT_FOUND -> notFoundDocuments++;
                    case ERROR_REGISTRATION_IN_REGISTRY ->  failedDocuments++;
                }

                pb.step();
            }

            log.info("[APPROVE-WORKER] Processing completed. Total: {}, Successfully Processed: {}, Conflicts: {}, Not Found: {}, Failed: {}.",
                    totalDocuments, successfullyProcessed, conflictDocuments, notFoundDocuments, failedDocuments);
        }
    }
}
