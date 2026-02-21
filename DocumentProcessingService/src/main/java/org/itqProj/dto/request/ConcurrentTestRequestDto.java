package org.itqProj.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request DTO for testing concurrent approval of a single document.")
public class ConcurrentTestRequestDto {
    @Schema(description = "The ID of the document to be approved concurrently.",
            example = "f4qfuowrebfcqsd3",
            type = "string",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.WRITE_ONLY,
            minLength = 16,
            maxLength = 16)
    @Size(min = 16, max = 16, message = "The document ID must consist of 16 characters.")
    private String documentId;

    @Schema(description = "The name of the approver for the concurrent approval test.",
            example = "John Doe",
            type = "string",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.WRITE_ONLY,
            minLength =  1,
            maxLength = 50)
    @Size(min = 1, max = 50, message = "The approver's name must be between 1 and 50 characters.")
    private String approver;

    @Schema(description = "Additional details for the concurrent approval test.",
            example = "Testing concurrent approval with multiple threads.",
            type = "string",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            accessMode = Schema.AccessMode.WRITE_ONLY,
            maxLength = 255)
    @Size(max = 255, message = "Details must not exceed 255 characters.")
    private String details;

    @Schema(description = "The number of threads to use for the concurrent approval test.",
            example = "10",
            type = "integer",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.WRITE_ONLY,
            minimum = "0",
            maximum = "16")
    @Positive(message = "Thread count must not exceed 16.")
    private int threadCount;

    @Schema(description = "The number of approval attempts each thread should make in the concurrent approval test.",
            example = "100",
            type = "integer",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.WRITE_ONLY,
            minimum = "1",
            maximum = "1000")
    @Positive(message = "Attempt count must be between 1 and 1000.")
    private int attemptCount;
}
