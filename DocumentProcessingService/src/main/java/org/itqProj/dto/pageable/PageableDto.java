package org.itqProj.dto.pageable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO representing pagination information for a paginated response.")
public class PageableDto {
    @Schema(description = "The current page number (0-based index).",
            example = "0",
            type = "integer",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY,
            minimum = "0")
    private int pageNumber;

    @Schema(description = "The number of items per page.",
            example = "10",
            type = "integer",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY,
            minimum = "1")
    private int pageSize;

    @Schema(description = "The number of total pages.",
            example = "0",
            type = "integer",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY,
            minimum = "0")
    private int totalPages;

    @Schema(description = "The total number of items across all pages.",
            example = "100",
            type = "integer",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY,
            minimum = "0")
    private long totalElements;

    @Schema(description = "The number of elements in the current page.",
            example = "10",
            type = "integer",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private int numberOfElements;

    @Schema(description = "The number of the current page.",
            example = "2",
            type = "integer",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private int numberOfPages;

    @Schema(description = "Flag indicating whether the current page is the first page.",
            type = "boolean",
            example = "false",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private boolean first;

    @Schema(description = "Flag indicating whether the current page is the last page.",
            type = "boolean",
            example = "false",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private boolean last;

    @Schema(description = "Flag indicating whether the current page is empty.",
            type = "boolean",
            example = "false",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private boolean empty;

    @Schema(description = "Sorting parameters for the current page.",
            type = "object",
            implementation = PageableSortDto.class,
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private PageableSortDto sort;
}
