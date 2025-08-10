package lt.code.samples.maven.worklog.service;

import lt.code.samples.maven.worklog.model.WorkLogEntity;
import lt.code.samples.maven.order.model.OrderEntity;
import lt.code.samples.maven.order.repository.OrderRepository;
import lt.code.samples.maven.worklog.repository.WorkLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Transactional
    public OrderEntity startWork(Long orderId, Authentication authentication, String operation) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        String workerName = (authentication != null) ? authentication.getName() : "Unknown";

        WorkLogEntity wl = new WorkLogEntity();
        wl.setOrder(order);
        wl.setOperationName(operation);
        wl.setWorkerName(workerName);
        wl.setStartTime(LocalDateTime.now());
        wl.setFaulty(false);
        wl.setFaultFixed(false);

        workLogRepository.save(wl);

        if (order.getWorkLogs() == null) {
            order.setWorkLogs(new ArrayList<>());
        }
        order.getWorkLogs().add(wl);

        return order;
    }

    private String resolveWorkerName(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return "Unknown";
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof lt.code.samples.maven.user.model.UserEntity u) {
            String fn = u.getFirstName() != null ? u.getFirstName().trim() : "";
            String ln = u.getLastName() != null ? u.getLastName().trim() : "";
            return (fn + " " + ln).trim().isEmpty() ? u.getUsername() : (fn + " " + ln).trim();
        }
        if (principal instanceof org.springframework.security.core.userdetails.User u2) {
            return u2.getUsername();
        }
        return principal.toString();
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

    @Transactional
    public void startWork(Long orderId, String operation, String workerName) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        WorkLogEntity wl = new WorkLogEntity();
        wl.setOrder(order);
        wl.setOperationName(operation);
        wl.setStartTime(java.time.LocalDateTime.now());
        wl.setFaulty(false);
        wl.setFaultFixed(false);
        wl.setWorkerName(workerName);

        workLogRepository.save(wl);
    }
}
