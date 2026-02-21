package org.itqProj.dto.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.itqProj.enums.ApproveStatusEnum;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Model DTO representing the result of an approval request for a specific document.")
public class ApproveResponseModelDto {
    @Schema(description = "The ID of the document for which the approval request was processed.",
            example = "f4qfuowrebfcqsd3",
            type = "string",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY,
            minLength = 16,
            maxLength = 16)
    private String documentId;

    @Schema(description = "The status of the approval request, indicating whether it was approved, rejected, or if an error occurred.",
            example = "APPROVED",
            type = "string",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private ApproveStatusEnum approveStatus;

    @Schema(description = "A message providing additional information about the approval request result.",
            example = "Testing concurrent approval with multiple threads.",
            type = "string",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY,
            maxLength = 255)
    private String message;
}
