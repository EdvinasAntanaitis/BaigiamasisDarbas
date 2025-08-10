package lt.code.samples.maven.worklog.service;

import lt.code.samples.maven.worklog.model.WorkLogEntity;
import lt.code.samples.maven.order.model.OrderEntity;
import lt.code.samples.maven.order.repository.OrderRepository;
import lt.code.samples.maven.worklog.repository.WorkLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkLogService {

    private final WorkLogRepository workLogRepository;
    private final OrderRepository orderRepository;

    public Optional<OrderEntity> findOrderByName(String name) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getOrderName().equalsIgnoreCase(name))
                .findFirst();
    }

    public Optional<WorkLogEntity> findById(Long id) {
        return workLogRepository.findById(id);
    }

    public void updateWorkLog(Long id, String workerName, LocalDateTime startTime, LocalDateTime endTime) {
        WorkLogEntity log = workLogRepository.findById(id).orElseThrow();
        log.setWorkerName(workerName);
        log.setStartTime(startTime);
        log.setEndTime(endTime);
        workLogRepository.save(log);
    }

    public void deleteById(Long id) {
        workLogRepository.deleteById(id);
    }


    public Optional<OrderEntity> findOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public OrderEntity startWork(Long orderId, String workerName, String operationName) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        WorkLogEntity workLog = new WorkLogEntity();
        workLog.setOrder(order);

        workLog.setWorkerName(workerName);
        workLog.setOperationName(operationName);

        workLog.setStartTime(LocalDateTime.now());

        workLogRepository.save(workLog);
        return order;
    }


    public void endWorkByLogId(Long logId) {
        WorkLogEntity log = workLogRepository.findById(logId)
                .orElseThrow(() -> new IllegalArgumentException("Log not found"));

        log.setEndTime(LocalDateTime.now());
        workLogRepository.save(log);
    }



    public void markAsFaulty(Long workLogId, String faultDescription, String operationName) {
        WorkLogEntity workLogEntity = workLogRepository.findById(workLogId)
                .orElseThrow(() -> new IllegalArgumentException("Darbo įrašas nerastas"));

        workLogEntity.setFaulty(true);
        workLogEntity.setFaultDescription(faultDescription);
        workLogEntity.setOperationName(operationName);

        workLogRepository.save(workLogEntity);
    }

    public void save(WorkLogEntity log) {
        workLogRepository.save(log);
    }
}
