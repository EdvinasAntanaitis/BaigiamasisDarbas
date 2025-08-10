package lt.code.samples.maven.worklog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StartWorkRequestDto(
        @NotNull Long orderId,
        @NotBlank String operation
) {}
