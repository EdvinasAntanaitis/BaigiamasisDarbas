package lt.code.samples.maven.repository;

import lt.code.samples.maven.model.WorkLogEntity;
import lt.code.samples.maven.worker.WorkLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkLogRepository extends JpaRepository<WorkLogEntity, Long> {
    Optional<WorkLogEntity> findTopByOrderIdAndWorkerNameAndOperationNameAndEndTimeIsNullOrderByStartTimeDesc(
            Long orderId, String workerName, String operationName);

}
