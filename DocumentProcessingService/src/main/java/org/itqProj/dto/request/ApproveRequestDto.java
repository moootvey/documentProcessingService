package org.itqProj.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request DTO for approving/submitting documents, containing a list of document IDs, the approver's name, and additional details.")
public class ApproveRequestDto {
    @Schema(description = "A list of document IDs to be approved or submitted.",
            example = "[\"f4qfuowrebfcqsd3\", \"a1b2c3d4e5f6g7h8\"]",
            type = "array",
            implementation = String.class,
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.WRITE_ONLY)
    @Size.List({@Size(min = 1, max = 1000, message = "You must provide between 1 and 3 document IDs.")})
    private List<@Pattern(regexp = ".{16}", message = "Each document ID must consist of 16 characters.") String> documentIds;

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
}
