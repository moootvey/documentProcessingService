package org.itqProj.dto.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "A model representing details of an exception.")
public class ExceptionDetailsDto {
    @Schema(description = "Problem type.",
            example = "value_error",
            type = "string",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private String type;

    @Schema(description = "The location of the error.",
            example = "password",
            type = "string",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private String location;

    @Schema(description = "Detailed message about the error.",
            example = "Invalid input provided",
            type = "string",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private String message;

    @Schema(description = "The input that caused the error.",
            example = "password123",
            type = "string",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private String input;
}