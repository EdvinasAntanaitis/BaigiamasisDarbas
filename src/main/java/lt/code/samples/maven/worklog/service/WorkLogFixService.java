package lt.code.samples.maven.worklog.service;

import lombok.RequiredArgsConstructor;
import lt.code.samples.maven.worklog.model.WorkLogEntity;
import lt.code.samples.maven.worklog.repository.WorkLogRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkLogFixService {
    private final WorkLogRepository workLogRepository;

    public boolean fixFault(Long id) {
        WorkLogEntity log = workLogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("WorkLog entry not found: " + id));

        if (!log.isFaulty()) {
            return false;
        }

        log.setFaultFixed(true);
        workLogRepository.save(log);
        return true;
    }

    public Optional<WorkLogEntity> findById(Long id) {
        return workLogRepository.findById(id);
    }
}
