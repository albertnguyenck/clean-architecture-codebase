package com.example.application.metadata.usecase;

import com.example.application.metadata.dto.MetadataDto;

import java.util.List;

public interface GetAllMetadataUseCase {
    List<MetadataDto> execute();
} 