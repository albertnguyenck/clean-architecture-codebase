package com.example.presentation;

import com.example.application.submission.command.ApproveSubmissionCommand;
import com.example.application.submission.command.CreateSubmissionCommand;
import com.example.application.submission.command.RejectSubmissionCommand;
import com.example.application.submission.command.UpdateSubmissionCommand;
import com.example.application.submission.dto.SubmissionId;
import com.example.application.submission.query.FindAllSubmissionsQuery;
import com.example.application.submission.query.FindSubmissionQuery;
import com.example.application.submission.usecase.FindSubmissionUseCase;
import com.example.application.submission.usecase.SubmissionUseCase;
import com.example.application.submission.usecase.UpdateSubmissionUseCase;
import com.example.presentation.dto.request.ApproveSubmissionRequest;
import com.example.presentation.dto.request.CreateSubmissionRequest;
import com.example.presentation.dto.request.RejectSubmissionRequest;
import com.example.presentation.dto.request.UpdateSubmissionRequest;
import com.example.presentation.dto.response.SubmissionResponse;
import com.example.presentation.dto.response.PaginatedSubmissionsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/submissions")
public class SubmissionController {
    
    private final SubmissionUseCase submissionUseCase;
    private final FindSubmissionUseCase findSubmissionUseCase;
    private final UpdateSubmissionUseCase updateSubmissionUseCase;
    
    public SubmissionController(SubmissionUseCase submissionUseCase, 
                              FindSubmissionUseCase findSubmissionUseCase,
                              UpdateSubmissionUseCase updateSubmissionUseCase) {
        this.submissionUseCase = submissionUseCase;
        this.findSubmissionUseCase = findSubmissionUseCase;
        this.updateSubmissionUseCase = updateSubmissionUseCase;
    }
    
    @PostMapping
    public ResponseEntity<SubmissionResponse> createSubmission(@RequestBody CreateSubmissionRequest request) {
        var command = new CreateSubmissionCommand(
                request.title(),
                request.content(),
                request.creatorId(),
                request.approverIds()
        );
        var submissionId = submissionUseCase.createSubmission(command);
        var findQuery = new FindSubmissionQuery(submissionId);
        Optional<SubmissionResponse> response = findSubmissionUseCase
                .findSubmission(findQuery)
                .map(SubmissionResponse::from);
        return response.map(ResponseEntity::ok)
                .orElse(ResponseEntity.internalServerError().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<SubmissionResponse> updateSubmission(
            @PathVariable String id,
            @RequestBody UpdateSubmissionRequest request) {
        var command = new UpdateSubmissionCommand(
                id,
                request.title(),
                request.content(),
                request.approverIds()
        );
        var submissionId = updateSubmissionUseCase.updateSubmission(command);
        var findQuery = new FindSubmissionQuery(submissionId);
        Optional<SubmissionResponse> response = findSubmissionUseCase
                .findSubmission(findQuery)
                .map(SubmissionResponse::from);
        return response.map(ResponseEntity::ok)
                .orElse(ResponseEntity.internalServerError().build());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SubmissionResponse> getSubmission(@PathVariable String id) {
        var submissionId = new SubmissionId(id);
        var query = new FindSubmissionQuery(submissionId);
        
        Optional<SubmissionResponse> response = findSubmissionUseCase
                .findSubmission(query)
                .map(SubmissionResponse::from);
        
        return response.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<PaginatedSubmissionsResponse> getAllSubmissions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        var query = new FindAllSubmissionsQuery(page, size, sortBy, sortDirection);
        var paginatedResult = findSubmissionUseCase.findAllSubmissions(query);
        
        List<SubmissionResponse> responses = paginatedResult.submissions()
                .stream()
                .map(SubmissionResponse::from)
                .toList();
        
        var response = new PaginatedSubmissionsResponse(
            responses,
            paginatedResult.page(),
            paginatedResult.size(),
            paginatedResult.totalElements(),
            paginatedResult.totalPages(),
            paginatedResult.hasNext(),
            paginatedResult.hasPrevious()
        );
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approveSubmission(
            @PathVariable String id,
            @RequestBody ApproveSubmissionRequest request) {
        var command = new ApproveSubmissionCommand(
                new SubmissionId(id),
                request.approverId()
        );
        submissionUseCase.approveSubmission(command);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/reject")
    public ResponseEntity<Void> rejectSubmission(
            @PathVariable String id,
            @RequestBody RejectSubmissionRequest request) {
        var command = new RejectSubmissionCommand(
                new SubmissionId(id),
                request.approverId(),
                request.reason()
        );
        submissionUseCase.rejectSubmission(command);
        return ResponseEntity.ok().build();
    }
} 