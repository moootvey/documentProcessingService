package org.itqProj.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(description = "Request DTO for creating a new document, containing the author's name and the document title.")
public class CreateDocumentRequestDto {
    @Schema(description = "The name of the document's author.",
            example = "Jane Doe",
            type = "string",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.WRITE_ONLY,
            minLength = 1,
            maxLength = 100)
    @Size(min = 1, max = 100, message = "The author's name must be between 1 and 100 characters long.")
    private String author;

    @Schema(description = "The title of the document.",
            example = "Project Proposal",
            type = "string",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.WRITE_ONLY,
            minLength = 1,
            maxLength = 200)
    @Size(min = 1, max = 200, message = "The title must be between 1 and 200 characters long.")
    private String title;
}
