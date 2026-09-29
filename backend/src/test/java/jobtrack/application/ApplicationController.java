package jobtrack.application;

import com.jobtrackr.application.ApplicationService;
import com.jobtrackr.application.Status;
import com.jobtrackr.application.dto.ApplicationResponse;
import com.jobtrackr.application.dto.CreateApplicationRequest;
import com.jobtrackr.application.dto.StatsResponse;
import com.jobtrackr.application.dto.UpdateApplicationRequest;
import com.jobtrackr.auth.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
@Tag(name="Candidatures")
public class ApplicationController {

    private final ApplicationService service;

    @PostMapping
    public ResponseEntity<ApplicationResponse> create(
            @AuthenticationPrincipal CustomUserDetails principal,
            @Valid @RequestBody CreateApplicationRequest request
            ){
        var response = service.create(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ApplicationResponse>> list(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestParam(required = false) Status status
    ){
        return ResponseEntity.ok(service.list(principal.getId(), status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationResponse> update(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable UUID id,
            @RequestBody UpdateApplicationRequest request
            ){
            return ResponseEntity.ok(service.update(principal.getId(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable UUID id
    ){
        service.delete(principal.getId(), id);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<StatsResponse> stats(@AuthenticationPrincipal CustomUserDetails principal){
        return ResponseEntity.ok(service.stats(principal.getId()));
    }
}
