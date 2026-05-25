package lt.code.samples.maven.worklog.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record WorkLogDto(
        Long id,
        UUID orderId,
        String workerName,
        String operationName,
        String paintType,
        LocalDateTime startTime,
        LocalDateTime endTime,
        boolean faulty,
        boolean faultFixed,
        String faultDescription
) {}
