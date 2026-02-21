package org.itqProj.dto.pageable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "DTO representing sorting information for a paginated response.")
public class PageableSortDto {
    @Schema(description = "The flag for applying sorting to the result.",
            example = "true",
            type = "boolean",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private boolean sorted;

    @Schema(description = "Flag for the presence of a sorting criterion.",
            example = "false",
            type = "boolean",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private boolean empty;
}
