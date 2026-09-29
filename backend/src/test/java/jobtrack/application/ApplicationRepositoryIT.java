package jobtrack.application;

import com.jobtrackr.application.ApplicationRepository;
import com.jobtrackr.application.JobApplicationEntity;
import com.jobtrackr.application.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class ApplicationRepositoryIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configureDataSource(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }


    @Autowired
    private ApplicationRepository repository;

    @Test
    void devrait_persister_et_retrouver_une_candidature_par_utilisateur_et_statut(){
        UUID userId = UUID.randomUUID();

        repository.save(JobApplicationEntity.builder()
                .userId(userId).company("Acme Inc.").position("Développeur Java").status(Status.POSTULE).appliedDate(LocalDate.now()).build());

        repository.save(JobApplicationEntity.builder()
                .userId(userId).company("Globex").position("Développeur Full Stack").status(Status.ENTRETIEN).appliedDate(LocalDate.now()).build());

        List<JobApplicationEntity> enEntretien = repository.findByUserIdAndStatusOrderByAppliedDateDesc(userId, Status.ENTRETIEN);

        assertThat(enEntretien).hasSize(1);
        assertThat(enEntretien.get(0).getCompany()).isEqualTo("Globex");
    }

    @Test
    void devrait_isoler_les_candidatures_par_utilisateur() {
        UUID userA = UUID.randomUUID();
        UUID userB = UUID.randomUUID();

        repository.save(JobApplicationEntity.builder()
                .userId(userA).company("Acme Inc.").position("Développeur Java")
                .status(Status.POSTULE).appliedDate(LocalDate.now()).build());

        List<JobApplicationEntity> resultatsUserB = repository.findByUserIdOrderByAppliedDateDesc(userB);

        assertThat(resultatsUserB).isEmpty();
    }
}
