package lt.code.samples.maven.worklog.repository;

import lt.code.samples.maven.worklog.model.WorkLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WorkLogRepository extends JpaRepository<WorkLogEntity, Long> {

    Optional<WorkLogEntity> findTopByOrderUuidAndWorkerNameAndOperationNameAndEndTimeIsNullOrderByStartTimeDesc(
            UUID orderUuid,
            String workerName,
            String operationName
    );
}