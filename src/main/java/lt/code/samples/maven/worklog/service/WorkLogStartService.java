package lt.code.samples.maven.worklog.service;

import lombok.RequiredArgsConstructor;
import lt.code.samples.maven.order.model.OrderEntity;
import lt.code.samples.maven.order.repository.OrderRepository;
import lt.code.samples.maven.worklog.model.WorkLogEntity;
import lt.code.samples.maven.worklog.repository.WorkLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WorkLogStartService {
    private final OrderRepository orderRepository;
    private final WorkLogRepository workLogRepository;

    public OrderEntity startWork(Long orderId, String workerName, String operation) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        WorkLogEntity log = new WorkLogEntity();
        log.setOrder(order);
        log.setWorkerName(workerName);
        log.setOperationName(operation);
        log.setStartTime(LocalDateTime.now());
        log.setFaulty(false);
        log.setFaultFixed(false);

        workLogRepository.save(log);

        return order;
    }
}

