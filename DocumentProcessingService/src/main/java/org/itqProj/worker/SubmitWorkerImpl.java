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
public class SubmitWorkerImpl {
    @Value("${workers.batch.size:50}")
    private int batchSize;
    @Value("${workers.submit.name:SUBMIT-WORKER}")
    private String workerName;

    private final DocumentMetadataRepository documentMetadataRepository;
    private final ApproveTransactionService approveTransactionService;

    private static final DocumentStatusEnum DOCUMENT_DRAFT_STATUS = DocumentStatusEnum.DRAFT;
    private static final String DETAILS_MESSAGE = "Automated submission by SubmitWorker.";

    @Scheduled(initialDelayString = "${workers.submit.initialDelay:60000}", fixedDelayString = "${workers.submit.fixedDelay:300000}")
    public void processSubmittingDocuments() {
        List<String> documentIds = documentMetadataRepository.findIdsByStatus(DOCUMENT_DRAFT_STATUS, PageRequest.of(0, batchSize));

        if (documentIds.isEmpty()) {
            log.info("[SUBMIT-WORKER] No documents found with status DRAFT for processing.");
            return;
        }

        int totalDocuments = documentIds.size();
        int successfullyProcessed = 0;
        int conflictDocuments = 0;
        int notFoundDocuments = 0;

        log.info("[SUBMIT-WORKER] Found {} documents with status DRAFT for processing.", documentIds.size());
        try (ProgressBar pb = new ProgressBar("[SUBMIT-WORKER]", documentIds.size())) {
            for (String documentId : documentIds) {
                switch (approveTransactionService.submitOneDocument(documentId, workerName, DETAILS_MESSAGE).getApproveStatus()) {
                    case SUCCESSFULLY -> successfullyProcessed++;
                    case CONFLICT -> conflictDocuments++;
                    case NOT_FOUND -> notFoundDocuments++;
                }

                pb.step();
            }

            log.info("[SUBMIT-WORKER] Processing completed. Total: {}, Successfully Processed: {}, Conflicts: {}, Not Found: {}.",
                    totalDocuments, successfullyProcessed, conflictDocuments, notFoundDocuments);
        }
    }
}
