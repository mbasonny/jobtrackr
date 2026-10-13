package com.jobtrackr.application;

import com.jobtrackr.application.dto.ApplicationResponse;
import com.jobtrackr.application.dto.CreateApplicationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "company", source = "request.company")
    @Mapping(target = "position", source = "request.position")
    @Mapping(target = "url", source = "request.url" )
    @Mapping(target = "notes", source = "request.notes")
    @Mapping(target = "appliedDate", source = "request.appliedDate")
    @Mapping(target = "status", constant = "POSTULE")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    JobApplicationEntity toEntity(CreateApplicationRequest request, UUID userId);

    ApplicationResponse toResponse(JobApplicationEntity entity);
}
