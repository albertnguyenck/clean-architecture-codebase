package com.example.application.submission;

import com.example.application.audit.AuditService;
import com.example.application.submission.command.ApproveSubmissionCommand;
import com.example.application.submission.command.CreateSubmissionCommand;
import com.example.application.submission.command.RejectSubmissionCommand;
import com.example.application.submission.command.UpdateSubmissionCommand;
import com.example.application.submission.dto.PaginatedSubmissionsDto;
import com.example.application.submission.dto.SubmissionId;
import com.example.application.submission.exception.SubmissionApplicationException;
import com.example.application.submission.query.FindAllSubmissionsQuery;
import com.example.application.submission.query.FindSubmissionQuery;
import com.example.application.submission.usecase.FindSubmissionUseCase;
import com.example.application.submission.usecase.SubmissionUseCase;
import com.example.application.submission.usecase.UpdateSubmissionUseCase;
import com.example.domain.submission.event.SubmissionCreatedEvent;
import com.example.domain.submission.model.Submission;
import com.example.domain.submission.model.SubmissionEventPublisher;
import com.example.domain.submission.repository.SubmissionRepository;
import com.example.domain.user.model.User;
import com.example.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubmissionApplicationService implements SubmissionUseCase, FindSubmissionUseCase, UpdateSubmissionUseCase {
    
    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final SubmissionEventPublisher eventPublisher;
    private final AuditService auditService;
    
    public SubmissionApplicationService(@Qualifier("jdbcSubmissionRepository") SubmissionRepository submissionRepository, 
                                      @Qualifier("jdbcUserRepository") UserRepository userRepository,
                                      SubmissionEventPublisher eventPublisher,
                                      AuditService auditService) {
        this.submissionRepository = submissionRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
        this.auditService = auditService;
    }
    
    @Override
    public SubmissionId createSubmission(CreateSubmissionCommand command) {
        User creator = userRepository.findById(command.creatorId())
                .orElseThrow(() -> new SubmissionApplicationException("Creator not found: " + command.creatorId()));
        List<User> approvers = command.approverIds().stream()
                .map(id -> userRepository.findById(id)
                        .orElseThrow(() -> new SubmissionApplicationException("Approver not found: " + id)))
                .toList();
        Submission submission = Submission.create(
                command.title(),
                command.content(),
                creator,
                approvers
        );

        Submission savedSubmission = submissionRepository.save(submission);
        
        auditService.logSubmissionCreation(savedSubmission.getId(), creator.getId(),
            "Submission created with title: " + command.title());
        
        eventPublisher.publishSubmissionCreated(SubmissionCreatedEvent.of(savedSubmission));
        
        return new SubmissionId(savedSubmission.getId());
    }
    
    @Override
    public Optional<Submission> findSubmission(FindSubmissionQuery query) {
        return submissionRepository.findById(query.submissionId().value());
    }
    
    @Override
    public PaginatedSubmissionsDto findAllSubmissions(FindAllSubmissionsQuery query) {
        List<Submission> allSubmissions = submissionRepository.findAll();
        
        int totalElements = allSubmissions.size();
        int totalPages = (int) Math.ceil((double) totalElements / query.size());
        int startIndex = query.page() * query.size();
        int endIndex = Math.min(startIndex + query.size(), totalElements);
        
        List<Submission> paginatedSubmissions = allSubmissions.subList(startIndex, endIndex);
        
        return new PaginatedSubmissionsDto(
            paginatedSubmissions,
            query.page(),
            query.size(),
            totalElements,
            totalPages,
            query.page() < totalPages - 1,
            query.page() > 0
        );
    }
    
    @Override
    public void approveSubmission(ApproveSubmissionCommand command) {
        Submission submission = submissionRepository.findById(command.submissionId().value())
                .orElseThrow(() -> new SubmissionApplicationException("Submission not found: " + command.submissionId().value()));
        User approver = userRepository.findById(command.approverId())
                .orElseThrow(() -> new SubmissionApplicationException("Approver not found: " + command.approverId()));
        
        String oldStatus = submission.getStatus().name();
        submission.approve(approver);
        submissionRepository.save(submission);
        
        auditService.logSubmissionApproval(submission.getId(), approver.getId(),
            "Submission approved by " + approver.getName() + " (Status: " + oldStatus + " → " + submission.getStatus().name() + ")");
    }
    
    @Override
    public void rejectSubmission(RejectSubmissionCommand command) {
        Submission submission = submissionRepository.findById(command.submissionId().value())
                .orElseThrow(() -> new SubmissionApplicationException("Submission not found: " + command.submissionId().value()));
        User approver = userRepository.findById(command.approverId())
                .orElseThrow(() -> new SubmissionApplicationException("Approver not found: " + command.approverId()));
        
        submission.reject(approver, command.reason());
        submissionRepository.save(submission);
        
        auditService.logSubmissionRejection(submission.getId(), approver.getId(), command.reason());
    }
    
    @Override
    public SubmissionId updateSubmission(UpdateSubmissionCommand command) {
        Submission submission = submissionRepository.findById(command.submissionId())
                .orElseThrow(() -> new SubmissionApplicationException("Submission not found: " + command.submissionId()));
        
        String oldTitle = submission.getTitle();
        String oldContent = submission.getContent();
        
        if (command.title() != null && !command.title().isBlank()) {
            submission.setTitle(command.title());
        }
        if (command.content() != null && !command.content().isBlank()) {
            submission.setContent(command.content());
        }
        if (command.approverIds() != null && !command.approverIds().isEmpty()) {
            List<User> approvers = command.approverIds().stream()
                    .map(id -> userRepository.findById(id)
                            .orElseThrow(() -> new SubmissionApplicationException("Approver not found: " + id)))
                    .toList();
            submission.setApprovers(approvers);
        }
        
        Submission saved = submissionRepository.save(submission);
        
        if (command.title() != null && !command.title().isBlank() && !oldTitle.equals(command.title())) {
            auditService.logSubmissionUpdate(saved.getId(), "SYSTEM", oldTitle, command.title(), 
                "Title updated from '" + oldTitle + "' to '" + command.title() + "'");
        }
        if (command.content() != null && !command.content().isBlank() && !oldContent.equals(command.content())) {
            auditService.logSubmissionUpdate(saved.getId(), "SYSTEM", oldContent, command.content(), 
                "Content updated");
        }
        
        return new SubmissionId(saved.getId());
    }
} 