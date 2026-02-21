package org.itqProj.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.itqProj.enums.ActionsEnum;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response DTO representing a single entry in the document history.")
public class DocumentHistoryResponseDto {
    @Schema(description = "The unique identifier of the document history entry.",
            example = "1234567890abcdef",
            type = "string",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY,
            minLength = 16,
            maxLength = 16)
    private String id;

    @Schema(description = "The name of the user who performed the action.",
            example = "Jane Doe",
            type = "string",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY,
            maxLength = 100)
    private String performedBy;

    @Schema(description = "The timestamp when the action was performed.",
            example = "2024-06-01T12:00:00Z",
            type = "string",
            format = "date-time",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private OffsetDateTime timestamp;

    @Schema(description = "The action performed on the document.",
            example = "APPROVE",
            type = "string",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private ActionsEnum action;

    @Schema(description = "A message providing additional information about the approval request result.",
            example = "Testing concurrent approval with multiple threads",
            type = "string",
            accessMode = Schema.AccessMode.READ_ONLY,
            maxLength = 255)
    private String details;
}
