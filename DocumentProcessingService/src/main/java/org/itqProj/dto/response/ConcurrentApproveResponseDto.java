package org.itqProj.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.itqProj.enums.DocumentStatusEnum;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response DTO for concurrent approval testing.")
public class ConcurrentApproveResponseDto {
    @Schema(description = "The name of the approver.",
            example = "Jane Doe",
            type = "string",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY,
            maxLength = 100)
    private String approver;

    @Schema(description = "The document ID for which the concurrent approval test was performed.",
            example = "1234567890abcdef",
            type = "string",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY,
            minLength = 16,
            maxLength = 16)
    private String documentId;

    @Schema(description = "The final status of the document after the concurrent approval test.",
            example = "APPROVED",
            type = "string",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private DocumentStatusEnum documentStatus;

    @Schema(description = "The total number of approval attempts made during the concurrent approval test.",
            example = "100",
            type = "integer",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private int totalAttemptsCount;

    @Schema(description = "The number of successful approvals during the concurrent approval test.",
            example = "1",
            type = "integer",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private int successfulApprovals;

    @Schema(description = "The number of approval attempts that resulted in a conflict during the concurrent approval test.",
            example = "99",
            type = "integer",
            accessMode = Schema.AccessMode.READ_ONLY)
    private int conflictApprovals;

    @Schema(description = "The number of approval attempts that failed due to reasons other than conflicts during the concurrent approval test.",
            example = "0",
            type = "integer",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private int failedApprovals;
}
