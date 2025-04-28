package lt.code.samples.maven.repository;

import lt.code.samples.maven.worker.WorkLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkLogRepository extends JpaRepository<WorkLog, Long> {
}
