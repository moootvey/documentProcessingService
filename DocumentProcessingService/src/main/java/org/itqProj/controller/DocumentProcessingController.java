package org.itqProj.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.itqProj.dto.exception.ExceptionDto;
import org.itqProj.dto.exception.ValidationExceptionDto;
import org.itqProj.dto.model.DocumentResponseModelDto;
import org.itqProj.dto.request.CreateDocumentRequestDto;
import org.itqProj.dto.response.GetDocumentsResponseDto;
import org.itqProj.dto.response.GetSingleDocumentResponseDto;
import org.itqProj.enums.DocumentStatusEnum;
import org.itqProj.service.interfaces.DocumentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Tag(name = "Docs", description = "Document processing API for creating, getting, and searching documents.")
@RestController
@RequestMapping("/api/v1/document-processing")
@RequiredArgsConstructor
@Validated
@Slf4j
public class DocumentProcessingController {
    private final DocumentService documentService;

    @Operation(summary = "Create a new document with the provided author and title.",
            tags = "Docs",
            description = "This endpoint allows you to create a new document by providing the author's name and the document's title. The created document will be returned in the response.",
            responses = {
            @ApiResponse(responseCode = "201", description = "Document successfully created.", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DocumentResponseModelDto.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid input data. The request body must contain valid author and title fields.", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ValidationExceptionDto.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal server error. An unexpected error occurred while processing the request.", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ExceptionDto.class)))})
            })
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public DocumentResponseModelDto createDocument(@RequestBody @Valid CreateDocumentRequestDto createDocumentRequestDto) {
        return documentService.createDocument(createDocumentRequestDto.getAuthor(), createDocumentRequestDto.getTitle());
    }

    @Operation(summary = "Get a document by its ID, with an option to include its history.",
            tags = "Docs",
            description = "This endpoint retrieves a document based on its unique ID. You can also specify whether to include the document's history in the response by using the 'withHistory' query parameter. If 'withHistory' is set to true, the response will include the document's history logs; otherwise, only the document details will be returned.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The document was received successfully.", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GetSingleDocumentResponseDto.class)))}),
                    @ApiResponse(responseCode = "400", description = "Invalid input data. The request body must contain valid author and title fields.", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ValidationExceptionDto.class)))}),
                    @ApiResponse(responseCode = "500", description = "Internal server error. An unexpected error occurred while processing the request.", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ExceptionDto.class)))})
            })
    @GetMapping("/{documentId}")
    public GetSingleDocumentResponseDto getDocument(@PathVariable @Size(min = 16, max = 16, message = "The document ID must consist of 16 characters.") String documentId,
                                                    @RequestParam(name = "withHistory", required = false, defaultValue = "true") Boolean withHistory) {
        return documentService.getDocumentWithHistory(documentId, withHistory);
    }

    @Operation(summary = "Get multiple documents by their IDs, with pagination and sorting options.",
            tags = "Docs",
            description = "This endpoint allows you to retrieve multiple documents by providing a list of document IDs. You can also specify pagination parameters (page and size) and sorting options (sort) to control the output. The response will include the details of the requested documents, along with pagination information if applicable.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The documents was received successfully.", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GetDocumentsResponseDto.class)))}),
                    @ApiResponse(responseCode = "400", description = "Invalid input data. The request body must contain valid author and title fields.", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ValidationExceptionDto.class)))}),
                    @ApiResponse(responseCode = "500", description = "Internal server error. An unexpected error occurred while processing the request.", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ExceptionDto.class)))})
            })
    @Parameter(name = "documentIds", example = "69efa3a347b7442e,", description = "A list of document IDs to retrieve. Each ID must consist of 16 characters, and you must provide between 1 and 1000 IDs.", required = true)
    @Parameter(name = "page", example = "1", description = "The page number for pagination. Must be a positive integer or zero.")
    @Parameter(name = "size", example = "10", description = "The number of documents to return per page. Must be a positive integer or zero.")
    @Parameter(name = "sort", example = "createdAt:desc", description = "Sorting criteria in the format 'field:direction'. Acceptable fields include 'uniqueNumber', 'author', 'title', 'status', 'createdAt', and 'updatedAt'. Direction can be 'asc' for ascending or 'desc' for descending.")
    @GetMapping("/documents")
    public GetDocumentsResponseDto getDocuments(@RequestParam(name = "documentIds") @Size.List({@Size(min = 1, max = 1000, message = "You must provide between 1 and 3 document IDs.")})
                                                    List<@Pattern(regexp = ".{16}", message = "The document ID must consist of 16 characters.") String> documentIds,
                                                @RequestParam(name = "page", required = false) @PositiveOrZero Integer page,
                                                @RequestParam(name = "size", required = false) @PositiveOrZero Integer size,
                                                @RequestParam(name = "sort", required = false) List<String> sort){
        return documentService.getDocuments(documentIds, page, size, sort);
    }

    @Operation(summary = "Search for documents based on various criteria, with pagination and sorting options.",
            tags = "Docs",
            description = "This endpoint allows you to search for documents based on multiple criteria, including document status, author name, and creation date range. You can also specify pagination parameters (page and size) and sorting options (sort) to control the output. The response will include the details of the documents that match the search criteria, along with pagination information if applicable.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The documents was received successfully.", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GetDocumentsResponseDto.class)))}),
                    @ApiResponse(responseCode = "400", description = "Invalid input data. The request body must contain valid author and title fields.", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ValidationExceptionDto.class)))}),
                    @ApiResponse(responseCode = "500", description = "Internal server error. An unexpected error occurred while processing the request.", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ExceptionDto.class)))})
            })
    @Parameter(name = "status", example = "SUBMITTED", description = "The status of the documents to search for. Acceptable values include 'DRAFT', 'SUBMITTED', and 'APPROVED'.")
    @Parameter(name = "author", example = "John Doe", description = "The name of the author to search for. The search will be case-insensitive and will match any document where the author's name contains the provided value.")
    @Parameter(name = "fromDate", example = "2026-01-01", description = "The start date for the document creation date range. Documents created on or after this date will be included in the search results. The date must be in ISO format (YYYY-MM-DD).")
    @Parameter(name = "toDate", example = "2026-12-31", description = "The end date for the document creation date range. Documents created on or before this date will be included in the search results. The date must be in ISO format (YYYY-MM-DD).")
    @Parameter(name = "page", example = "1", description = "The page number for pagination. Must be a positive integer or zero.")
    @Parameter(name = "size", example = "10", description = "The number of documents to return per page. Must be a positive integer or zero.")
    @Parameter(name = "sort", example = "createdAt:desc", description = "Sorting criteria in the format 'field:direction'. Acceptable fields include 'uniqueNumber', 'author', 'title', 'status', 'createdAt', and 'updatedAt'. Direction can be 'asc' for ascending or 'desc' for descending.")
    @GetMapping("/search")
    public GetDocumentsResponseDto searchDocuments(@RequestParam(required = false) DocumentStatusEnum status,
                                                   @RequestParam(required = false) @Size(max = 100, message = "The author's name cannot exceed 100 characters.") String author,
                                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                                                   @RequestParam(required = false) @PositiveOrZero Integer page,
                                                   @RequestParam(required = false) @PositiveOrZero Integer size,
                                                   @RequestParam(required = false) List<String> sort) {
        return documentService.searchDocuments(page, size, sort, status, author, fromDate == null ? null : fromDate.atStartOfDay().atOffset(ZoneOffset.UTC), toDate == null ? null : toDate.atTime(LocalTime.MAX).atOffset(ZoneOffset.UTC));
    }
}
