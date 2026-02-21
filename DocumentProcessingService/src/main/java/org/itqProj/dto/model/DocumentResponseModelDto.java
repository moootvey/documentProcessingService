package org.itqProj.dto.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.itqProj.enums.DocumentStatusEnum;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Model DTO representing the details of a document.")
public class DocumentResponseModelDto {
    @Schema(description = "The unique identifier of the document.",
            example = "f4qfuowrebfcqsd3",
            type = "string",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY,
            minLength = 16,
            maxLength = 16)
    private String documentId;

    @Schema(description = "A unique number associated with the document, which can be used for tracking and reference purposes.",
            example = "doc_f4qfuowrebfcqsd3_2024-06-01T12:00:00Z",
            type = "string",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private String uniqueNumber;

    @Schema(description = "The current status of the document, indicating whether it is draft, submitted, approved.",
            example = "DRAFT",
            type = "string",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private DocumentStatusEnum status;

    @Schema(description = "The name of the author who created the document.",
            example = "Jane Doe",
            type = "string",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY,
            maxLength = 100)
    private String author;

    @Schema(description = "The title of the document.",
            example = "Project Proposal",
            type = "string",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY,
            maxLength = 200)
    private String title;

    @Schema(description = "The timestamp when the document was created.",
            example = "2024-06-01T12:00:00Z",
            type = "string",
            format = "date-time",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private OffsetDateTime createAt;

    @Schema(description = "The timestamp when the document was last updated.",
            example = "2024-06-01T12:00:00Z",
            type = "string",
            format = "date-time",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private OffsetDateTime updateAt;
}
