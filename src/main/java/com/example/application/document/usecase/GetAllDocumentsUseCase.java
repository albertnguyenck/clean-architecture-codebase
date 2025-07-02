package com.example.application.document.usecase;

import com.example.application.document.dto.DocumentDto;

import java.util.List;

public interface GetAllDocumentsUseCase {
    List<DocumentDto> execute();
} 