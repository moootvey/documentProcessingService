package org.itqProj.dto.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@Schema(description = "A model representing a generic exception response.")
public class ExceptionDto {
    @Schema(description = "HTTP status code.",
            example = "400",
            type = "string",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private String httpStatusCode;

    @Schema(description = "Detailed message about the error.",
            example = "Invalid input provided",
            type = "string",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private String message;

    @Schema(description = "Detailed information about the error.",
            example = "The input provided does not meet the required format",
            type = "string",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private String errDetails;

    @Schema(description = "The time when the error occurred.",
            example = "2025-03-06T12:38:36.974+00:00",
            type = "date-time",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private OffsetDateTime timestamp;
}
