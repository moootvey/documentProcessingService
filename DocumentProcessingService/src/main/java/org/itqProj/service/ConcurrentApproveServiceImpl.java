package org.itqProj.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.itqProj.dto.response.ConcurrentApproveResponseDto;
import org.itqProj.entity.DocumentMetadataEntity;
import org.itqProj.exception.DocumentNotFoundException;
import org.itqProj.repository.DocumentMetadataRepository;
import org.itqProj.service.interfaces.ApproveTransactionService;
import org.itqProj.service.interfaces.ConcurrentApproveService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConcurrentApproveServiceImpl implements ConcurrentApproveService {
    private final ApproveTransactionService approveTransactionService;
    private final DocumentMetadataRepository documentMetadataRepository;

    @Override
    public ConcurrentApproveResponseDto approveOneDocumentConcurrentlyTest(String documentId, String approver, String details, int threadCount, int attemptCount) {
        if (!documentMetadataRepository.existsByDocumentId(documentId))
            throw new DocumentNotFoundException(documentId);

        int totalTaskCount = threadCount * attemptCount;
        LongAdder successCount = new LongAdder();
        LongAdder conflictCount = new LongAdder();
        LongAdder errorCount = new LongAdder();

        CountDownLatch startLatch = new CountDownLatch(1);
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        List<Future<?>> futures = new ArrayList<>(totalTaskCount);

        try {
            for (int i = 0; i < totalTaskCount; i++) {
                log.debug("scheduling task #{}", i + 1);
                futures.add(executorService.submit(() -> {
                    try {
                        startLatch.await();
                        var result = approveTransactionService.approveOneDocument(documentId, approver, details);
                        switch (result.getApproveStatus()) {
                            case SUCCESSFULLY -> successCount.increment();
                            case CONFLICT -> conflictCount.increment();
                            case ERROR_REGISTRATION_IN_REGISTRY -> errorCount.increment();
                            default -> {}
                        }
                    } catch (InterruptedException ie) {
                        // Preserve interrupt status and count as error
                        Thread.currentThread().interrupt();
                        errorCount.increment();
                        log.warn("Task interrupted", ie);
                    } catch (Exception e) {
                        errorCount.increment();
                        log.error("Task failed", e);
                    }
                }));
            }

            startLatch.countDown();

            for (Future<?> f : futures) {
                try {
                    f.get();
                } catch (ExecutionException ee) {
                    log.debug("Task execution failed", ee.getCause());
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    log.warn("Waiting for tasks was interrupted", ie);
                    break;
                }
            }

        } catch (Exception e) {
            Thread.currentThread().interrupt();
            log.error("Interrupted while waiting for tasks: {}", e.getMessage());
        } finally {
            executorService.shutdownNow();
            try {
                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    log.warn("Executor did not terminate cleanly");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        DocumentMetadataEntity documentMetadataEntity = documentMetadataRepository.findById(documentId).orElseThrow(() -> new DocumentNotFoundException(documentId));

        log.info("[CONCURRENT-TEST] Document ID = {}, total attempts count = {}, success count = {}, conflicts count = {}, fail count = {}, final status = {}", documentId, totalTaskCount, successCount, conflictCount, errorCount, documentMetadataEntity.getStatus());

        return new ConcurrentApproveResponseDto(
                approver,
                documentId,
                documentMetadataEntity.getStatus(),
                totalTaskCount,
                successCount.intValue(),
                conflictCount.intValue(),
                errorCount.intValue());
    }

}
