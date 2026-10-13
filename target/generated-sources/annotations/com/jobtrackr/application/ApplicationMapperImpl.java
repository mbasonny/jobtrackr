package com.jobtrackr.application;

import com.jobtrackr.application.dto.ApplicationResponse;
import com.jobtrackr.application.dto.CreateApplicationRequest;
import java.time.LocalDate;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-29T03:33:19-0400",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.12.1 (Ubuntu)"
)
@Component
public class ApplicationMapperImpl implements ApplicationMapper {

    @Override
    public JobApplicationEntity toEntity(CreateApplicationRequest request, UUID userId) {
        if ( request == null && userId == null ) {
            return null;
        }

        JobApplicationEntity.JobApplicationEntityBuilder jobApplicationEntity = JobApplicationEntity.builder();

        if ( request != null ) {
            jobApplicationEntity.company( request.company() );
            jobApplicationEntity.position( request.position() );
            jobApplicationEntity.url( request.url() );
            jobApplicationEntity.notes( request.notes() );
            jobApplicationEntity.appliedDate( request.appliedDate() );
        }
        jobApplicationEntity.userId( userId );
        jobApplicationEntity.status( Status.POSTULE );

        return jobApplicationEntity.build();
    }

    @Override
    public ApplicationResponse toResponse(JobApplicationEntity entity) {
        if ( entity == null ) {
            return null;
        }

        UUID id = null;
        String company = null;
        String position = null;
        String url = null;
        String notes = null;
        Status status = null;
        LocalDate appliedDate = null;

        id = entity.getId();
        company = entity.getCompany();
        position = entity.getPosition();
        url = entity.getUrl();
        notes = entity.getNotes();
        status = entity.getStatus();
        appliedDate = entity.getAppliedDate();

        ApplicationResponse applicationResponse = new ApplicationResponse( id, company, position, url, notes, status, appliedDate );

        return applicationResponse;
    }
}
