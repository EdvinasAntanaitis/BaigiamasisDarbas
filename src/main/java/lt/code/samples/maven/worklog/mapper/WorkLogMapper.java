package lt.code.samples.maven.worklog.mapper;

import lt.code.samples.maven.worklog.dto.WorkLogDto;
import lt.code.samples.maven.worklog.model.WorkLogEntity;

public final class WorkLogMapper {
    private WorkLogMapper() {}

    public static WorkLogDto toDto(WorkLogEntity e) {
        return new WorkLogDto(
                e.getId(),
                e.getOrder() != null ? e.getOrder().getId() : null,
                e.getWorkerName(),
                e.getOperationName(),
                e.getPaintType(),
                e.getStartTime(),
                e.getEndTime(),
                e.isFaulty(),
                e.isFaultFixed(),
                e.getFaultDescription()
        );
    }
}
