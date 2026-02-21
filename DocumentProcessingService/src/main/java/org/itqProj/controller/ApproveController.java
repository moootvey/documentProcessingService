package org.itqProj.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.itqProj.dto.exception.ExceptionDto;
import org.itqProj.dto.exception.ValidationExceptionDto;
import org.itqProj.dto.request.ApproveRequestDto;
import org.itqProj.dto.request.ConcurrentTestRequestDto;
import org.itqProj.dto.response.ApproveResponseDto;
import org.itqProj.dto.response.ConcurrentApproveResponseDto;
import org.itqProj.dto.response.GetDocumentsResponseDto;
import org.itqProj.service.interfaces.ApproveService;
import org.itqProj.service.interfaces.ConcurrentApproveService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/document-processing/operations")
@RequiredArgsConstructor
@Tag(name = "Approve", description = "Endpoints for submitting and approving documents, as well as testing concurrent approvals.")
public class ApproveController {
    private final ApproveService approveService;
    private final ConcurrentApproveService concurrentApproveService;

    @Operation(summary = "Submit documents for approval with the provided document IDs, approver's name, and additional details.",
            tags = "Approve",
            description = "This endpoint allows you to submit a list of documents for approval by providing their IDs, the name of the approver, and any additional details. The response will contain the approval status for each submitted document.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The document has been successfully submitted.", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ApproveResponseDto.class)))}),
                    @ApiResponse(responseCode = "400", description = "Invalid input data. The request body must contain valid author and title fields.", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ValidationExceptionDto.class)))}),
                    @ApiResponse(responseCode = "500", description = "Internal server error. An unexpected error occurred while processing the request.", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ExceptionDto.class)))})
            })
    @PostMapping("/submit")
    public ApproveResponseDto submitDocuments(@RequestBody @Valid ApproveRequestDto submitRequestDto) {
        return approveService.submitDocuments(submitRequestDto.getDocumentIds(), submitRequestDto.getApprover(), submitRequestDto.getDetails());
    }

    @Operation(summary = "Approve documents with the provided document IDs, approver's name, and additional details.",
            tags = "Approve",
            description = "This endpoint allows you to approve a list of documents by providing their IDs, the name of the approver, and any additional details. The response will contain the approval status for each approved document.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The document has been successfully approved.", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ApproveResponseDto.class)))}),
                    @ApiResponse(responseCode = "400", description = "Invalid input data. The request body must contain valid author and title fields.", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ValidationExceptionDto.class)))}),
                    @ApiResponse(responseCode = "500", description = "Internal server error. An unexpected error occurred while processing the request.", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ExceptionDto.class)))})
            })
    @PostMapping("/approve")
    public ApproveResponseDto approveDocuments(@RequestBody @Valid ApproveRequestDto approveRequestDto) {
        return approveService.approveDocuments(approveRequestDto.getDocumentIds(), approveRequestDto.getApprover(), approveRequestDto.getDetails());
    }

    @Operation(summary = "Concurrently approve a single document multiple times to test the system's handling of concurrent approvals.",
            tags = "Approve",
            description = "This endpoint allows you to test the concurrent approval of a single document by providing its ID, the approver's name, additional details, the number of concurrent threads to use, and the number of approval attempts per thread. The response will contain the results of the concurrent approval test, including counts of successful approvals, conflicts, and errors.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The document has been successfully approved.", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ConcurrentApproveResponseDto.class)))}),
                    @ApiResponse(responseCode = "400", description = "Invalid input data. The request body must contain valid author and title fields.", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ValidationExceptionDto.class)))}),
                    @ApiResponse(responseCode = "500", description = "Internal server error. An unexpected error occurred while processing the request.", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ExceptionDto.class)))})
            })
    @PostMapping("/test-concurrent-approve")
    public ConcurrentApproveResponseDto testConcurrentApproval(@RequestBody @Valid ConcurrentTestRequestDto concurrentTestRequestDto) {
        return concurrentApproveService.approveOneDocumentConcurrentlyTest(
                concurrentTestRequestDto.getDocumentId(),
                concurrentTestRequestDto.getApprover(),
                concurrentTestRequestDto.getDetails(),
                concurrentTestRequestDto.getThreadCount(),
                concurrentTestRequestDto.getAttemptCount());
    }
}
