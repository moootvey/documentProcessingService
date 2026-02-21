package org.itqProj.dto.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "A model representing a validation exception response.")
public class ValidationExceptionDto {
    @Schema(description = "HTTP status code.",
            example = "400",
            type = "string",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private String httpStatusCode;

    @Schema(description = "List of data validation errors.",
            type = "array",
            implementation = ExceptionDetailsDto.class,
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private List<ExceptionDetailsDto> details;

    @Schema(description = "The time when the error occurred.",
            example = "2025-03-06T12:38:36.974+00:00",
            type = "date-time",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private OffsetDateTime timestamp;
}