package lt.code.samples.maven.worklog.service;

import lombok.RequiredArgsConstructor;
import lt.code.samples.maven.order.model.OrderEntity;
import lt.code.samples.maven.order.repository.OrderRepository;
import lt.code.samples.maven.user.dto.UserDto;
import lt.code.samples.maven.user.service.UserService;
import lt.code.samples.maven.worklog.model.WorkLogEntity;
import lt.code.samples.maven.worklog.repository.WorkLogRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
public class WorkLogStartService {

    private final OrderRepository orderRepository;
    private final WorkLogRepository workLogRepository;
    private final UserService userService;

    public WorkLogStartService(OrderRepository orderRepository,
                               WorkLogRepository workLogRepository,
                               UserService userService) {
        this.orderRepository = orderRepository;
        this.workLogRepository = workLogRepository;
        this.userService = userService;
    }

    @Transactional
    public OrderEntity startWork(Long orderId, Authentication auth, String operation) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        String workerName = null;
        if (auth != null) {
            UserDto user = userService.getUserDtoByUsername(auth.getName());
            if (user != null) {
                workerName = ((user.getFirstName() != null) ? user.getFirstName() : "") +
                        " " +
                        ((user.getLastName()  != null) ? user.getLastName()  : "");
                workerName = workerName.trim();
            }
        }
        if (workerName == null || workerName.isBlank()) {
            workerName = (auth != null) ? auth.getName() : "N/A";
        }

        WorkLogEntity wl = new WorkLogEntity();
        wl.setOrder(order);
        wl.setOperationName(operation);
        wl.setStartTime(java.time.LocalDateTime.now());
        wl.setWorkerName(workerName);
        wl.setFaulty(false);
        wl.setFaultFixed(false);

        workLogRepository.save(wl);
        return order;
    }
}
