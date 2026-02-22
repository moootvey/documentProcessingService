package org.itqProj.component;

import lombok.extern.slf4j.Slf4j;
import me.tongfei.progressbar.ProgressBar;
import org.itqProj.dto.CreateDocumentRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class DocumentGeneratorComponent implements CommandLineRunner {
    @Value("${generator.n}")
    private int countToCreate;
    @Value("${generator.url}")
    private String apiUrl;
    @Value("${generator.author-name}")
    private String author;
    @Value("${generator.title-prefix}")
    private String titlePrefix;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void run(String... args) throws Exception {
        log.info(">>> [DOCUMENT-GENERATOR] Starting creation of N={} documents", countToCreate);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        int successCount = 0;

        try (ProgressBar pb = new ProgressBar("[DOCUMENT-GENERATOR]", countToCreate)) {
            for (int i = 1; i <= countToCreate; i++) {
                try {
                    CreateDocumentRequestDto request = new CreateDocumentRequestDto(
                            author,
                            titlePrefix + "-" + i
                    );

                    restTemplate.postForEntity(apiUrl, request, Void.class);
                    pb.step();
                    successCount++;

                } catch (Exception e) {
                    log.error("[DOCUMENT-GENERATOR] Failed to create document #{}: {}", i, e.getMessage());
                }
            }
        }

        stopWatch.stop();
        log.info(">>> [DOCUMENT-GENERATOR] Completed! Successfully created: {}. Time taken: {}s",
                successCount, stopWatch.getTotalTimeSeconds());
    }
}
