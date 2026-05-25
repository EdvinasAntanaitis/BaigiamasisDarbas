package lt.code.samples.maven.order.service;

import lombok.RequiredArgsConstructor;
import lt.code.samples.maven.common.exception.NotFoundException;
import lt.code.samples.maven.order.model.OrderEntity;
import lt.code.samples.maven.order.repository.OrderRepository;
import lt.code.samples.maven.worklog.model.WorkLogEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Optional<OrderEntity> getOrderById(UUID id) {
        return orderRepository.findById(id);
    }

    public boolean canEndOrder(OrderEntity order) {
        if (order.getWorkLogs() == null || order.getWorkLogs().isEmpty()) return true;
        return order.getWorkLogs().stream().allMatch(this::isLogResolved);
    }

    private boolean isLogResolved(WorkLogEntity log) {
        if (log.isFaulty()) return log.isFaultFixed();
        return log.getEndTime() != null;
    }

    @Transactional
    public void endOrder(OrderEntity order) {
        order.setCompleted(true);
        if (order.getWorkLogs() != null) {
            for (WorkLogEntity wl : order.getWorkLogs()) {
                if (wl.getEndTime() == null && !wl.isFaulty()) {
                    wl.setEndTime(LocalDateTime.now());
                }
            }
        }
        orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(UUID id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found: " + id));
        orderRepository.delete(order);
    }
}
