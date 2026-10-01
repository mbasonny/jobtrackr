package jobtrack.application;

import com.jobtrackr.application.*;
import com.jobtrackr.application.dto.ApplicationResponse;
import com.jobtrackr.application.dto.CreateApplicationRequest;
import com.jobtrackr.application.dto.StatsResponse;
import com.jobtrackr.application.dto.UpdateApplicationRequest;
import com.jobtrackr.common.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ApplicationServiceTest {
    private ApplicationRepository repository;
    private ApplicationService service;

    @BeforeEach
    void setUp(){
        repository = mock(ApplicationRepository.class);
        ApplicationMapper mapper = Mappers.getMapper(ApplicationMapper.class);
        service = new ApplicationService(repository, mapper);

    }

    @Test
    void devrait_creer_une_candidature_avec_statut_postule_par_defaut(){
        UUID userId = UUID.randomUUID();
        var request = new CreateApplicationRequest("Acme Inc.", "Développeur Java", null, null, LocalDate.now());

        when(repository.save(any(JobApplicationEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ApplicationResponse response = service.create(userId, request);

        assertThat(response.status()).isEqualTo(Status.POSTULE);
        assertThat(response.company()).isEqualTo("Acme Inc.");
        verify(repository, times(1)).save(any(JobApplicationEntity.class));
    }

    @Test
    void devrait_lever_une_exception_si_la_candidature_nappartient_pas_a_lutilisateur() {
        UUID userId = UUID.randomUUID();
        UUID applicationId = UUID.randomUUID();

        when(repository.findByIdAndUserId(applicationId, userId)).thenReturn(Optional.empty());

        var update = new UpdateApplicationRequest(null, null, null, null, Status.ENTRETIEN, null);

        assertThatThrownBy(() -> service.update(userId, applicationId, update))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void devrait_ne_modifier_que_les_champs_fournis_lors_dune_mise_a_jour_partielle(){
        UUID userId = UUID.randomUUID();
        UUID applicationId = UUID.randomUUID();

        JobApplicationEntity existing = JobApplicationEntity.builder()
                .id(applicationId)
                .userId(userId)
                .company("Acme Inc.")
                .position("Développeur Java")
                .status(Status.POSTULE)
                .appliedDate(LocalDate.now())
                .build();
        when(repository.findByIdAndUserId(applicationId, userId)).thenReturn(Optional.of(existing));
        when(repository.save(any(JobApplicationEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        var update = new UpdateApplicationRequest(null, null, null, null, Status.ENTRETIEN, null);
        ApplicationResponse response = service.update(userId, applicationId, update);

        assertThat(response.status()).isEqualTo(Status.ENTRETIEN);
        assertThat(response.company()).isEqualTo("Acme Inc.");
    }

    @Test
    void devrait_calculer_les_statistiques_par_statut(){
        UUID userId = UUID.randomUUID();

        when(repository.countByUserIdAndStatus(userId, Status.POSTULE)).thenReturn(5L);
        when(repository.countByUserIdAndStatus(userId, Status.ENTRETIEN)).thenReturn(2L);
        when(repository.countByUserIdAndStatus(userId, Status.REFUSE)).thenReturn(1L);
        when(repository.countByUserIdAndStatus(userId, Status.OFFRE)).thenReturn(1L);

        StatsResponse stats = service.stats(userId);

        assertThat(stats.total()).isEqualTo(9L);
        assertThat(stats.entretien()).isEqualTo(2L);
    }
}
