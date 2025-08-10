package lt.code.samples.maven.worklog.dto;

import java.time.LocalDateTime;

public record WorkLogDto(
        Long id,
        Long orderId,
        String workerName,
        String operationName,
        String paintType,
        LocalDateTime startTime,
        LocalDateTime endTime,
        boolean faulty,
        boolean faultFixed,
        String faultDescription
) {}
