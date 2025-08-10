package lt.code.samples.maven.worklog.dto;

import jakarta.validation.constraints.NotBlank;

public record FaultRequestDto(
        @NotBlank String operationName,
        String faultDescription
) {}
