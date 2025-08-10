package lt.code.samples.maven.worklog.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lt.code.samples.maven.worklog.model.WorkLogEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkLogEndService {

    private final WorkLogService workLogService;

    public EndWorkResult endWork(Long workLogId) {
        Optional<WorkLogEntity> opt = workLogService.findById(workLogId);
        if (opt.isEmpty()) {
            return EndWorkResult.error("Work-log not found.", "/dashboard");
        }
        WorkLogEntity log = opt.get();

        if (log.isFaulty() && !log.isFaultFixed()) {
            return EndWorkResult.error("Cannot finish a faulty operation.", "/orders/worklog?orderName=" + log.getOrder().getOrderName());
        }
        if (log.getEndTime() != null) {
            return EndWorkResult.info("This operation is already finished.", "/orders/worklog?orderName=" + log.getOrder().getOrderName());
        }
        log.setEndTime(LocalDateTime.now());
        workLogService.save(log);

        return EndWorkResult.success("Work finished successfully.", "/orders/worklog?orderName=" + log.getOrder().getOrderName());
    }

    @Data
    @AllArgsConstructor(staticName = "of")
    public static class EndWorkResult {
        private boolean success;
        private String message;
        private String redirectUrl;

        public static EndWorkResult success(String msg, String url) { return EndWorkResult.of(true, msg, url); }
        public static EndWorkResult error(String msg, String url) { return EndWorkResult.of(false, msg, url); }
        public static EndWorkResult info(String msg, String url) { return EndWorkResult.of(true, msg, url); }
    }
}
