package org.itqProj.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.itqProj.dto.model.DocumentResponseModelDto;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response DTO for retrieving a single document along with its history.")
public class GetSingleDocumentResponseDto {
    @Schema(description = "The document details.",
            type = "object",
            implementation = DocumentResponseModelDto.class,
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private DocumentResponseModelDto document;

    @Schema(description = "A list of historical actions performed on the document.",
            type = "array",
            implementation = DocumentHistoryResponseDto.class,
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    private List<DocumentHistoryResponseDto> documentHistory;

}
