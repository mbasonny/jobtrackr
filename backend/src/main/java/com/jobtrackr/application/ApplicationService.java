package com.jobtrackr.application;

import com.jobtrackr.application.dto.ApplicationResponse;
import com.jobtrackr.application.dto.CreateApplicationRequest;
import com.jobtrackr.application.dto.StatsResponse;
import com.jobtrackr.application.dto.UpdateApplicationRequest;
import com.jobtrackr.common.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository repository;
    private final ApplicationMapper mapper;

    public ApplicationResponse create(UUID userId, CreateApplicationRequest request) {

        JobApplicationEntity entity = mapper.toEntity(request, userId);
        return mapper.toResponse(repository.save(entity));
    }

    public List<ApplicationResponse> list(UUID userId, Status statusFilter){
        List<JobApplicationEntity> entities = statusFilter == null
                ? repository.findByUserIdOrderByAppliedDateDesc(userId)
                : repository.findByUserIdAndStatusOrderByAppliedDateDesc(userId, statusFilter);
        return entities.stream().map(mapper::toResponse).toList();

    }

    public ApplicationResponse update(UUID userId, UUID id, UpdateApplicationRequest request) {
        JobApplicationEntity entity = findOwnedOrThrow(userId, id);
        if (request.company() != null) entity.setCompany(request.company());
        if (request.position() != null) entity.setPosition(request.position());
        if (request.url() != null) entity.setUrl(request.url());
        if (request.notes() != null) entity.setNotes(request.notes());
        if (request.status() != null) entity.setStatus(request.status());
        if (request.appliedDate() != null) entity.setAppliedDate(request.appliedDate());

        return mapper.toResponse(repository.save(entity));
    }
     public void delete(UUID userId, UUID id) {
         JobApplicationEntity entity = findOwnedOrThrow(userId, id);
         repository.delete(entity);
     }

     private JobApplicationEntity findOwnedOrThrow(UUID userId, UUID id) {
        return repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidature introuvable : " + id));
     }

    public StatsResponse stats(UUID userId) {
        long postule = repository.countByUserIdAndStatus(userId, Status.POSTULE);
        long entretien = repository.countByUserIdAndStatus(userId, Status.ENTRETIEN);
        long refuse = repository.countByUserIdAndStatus(userId, Status.REFUSE);
        long offre = repository.countByUserIdAndStatus(userId, Status.OFFRE);

        return new StatsResponse(postule, entretien, refuse, offre, postule + entretien + refuse + offre);
    }
}
