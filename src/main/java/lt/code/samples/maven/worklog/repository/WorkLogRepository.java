package lt.code.samples.maven.worklog.repository;

import lt.code.samples.maven.worklog.model.WorkLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkLogRepository extends JpaRepository<WorkLogEntity, Long> {
    Optional<WorkLogEntity> findTopByOrderIdAndWorkerNameAndOperationNameAndEndTimeIsNullOrderByStartTimeDesc(
            Long orderId,
            String workerName,
            String operationName);

}
