package org.itqProj.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.itqProj.dto.model.ApproveResponseModelDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Response DTO for the approval process, containing the approver's ID and a list of results for each approval request.")
public class ApproveResponseDto {
    @Schema(description = "The name of the approver who processed the approval requests.",
            example = "Jane Doe",
            type = "string",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY,
            minLength = 1,
            maxLength = 100)
    private String approver;

    @Schema(description = "A list of results for each approval request, including the document ID, approval status, and any relevant messages.",
            type = "array",
            implementation = ApproveResponseModelDto.class,
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private List<ApproveResponseModelDto> approveRequestResult;
}
