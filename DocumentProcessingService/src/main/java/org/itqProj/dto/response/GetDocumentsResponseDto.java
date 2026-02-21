package org.itqProj.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.itqProj.dto.model.DocumentResponseModelDto;
import org.itqProj.dto.pageable.PageableDto;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response DTO for retrieving a list of documents.")
public class GetDocumentsResponseDto {
    @Schema(description = "A list of documents.",
            type = "array",
            implementation = DocumentResponseModelDto.class,
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private List<DocumentResponseModelDto> documents;

    @Schema(description = "Pagination information for the list of documents.",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private PageableDto pageable;
}
