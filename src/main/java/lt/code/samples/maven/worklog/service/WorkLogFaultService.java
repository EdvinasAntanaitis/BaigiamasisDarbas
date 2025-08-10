package lt.code.samples.maven.worklog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkLogFaultService {

    private final WorkLogService workLogService;

    public String markAsFaultyAndGetOrderName(Long workLogId, String faultDescription, String operationName) {
        workLogService.markAsFaulty(workLogId, faultDescription, operationName);

        return workLogService.findById(workLogId)
                .map(l -> l.getOrder().getOrderName())
                .orElse("");
    }
}
