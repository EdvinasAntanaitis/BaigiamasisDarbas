package lt.code.samples.maven.worklog.service;

import lombok.RequiredArgsConstructor;
import lt.code.samples.maven.order.model.OrderEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkLogViewService {

    private final WorkLogService workLogService;

    public Optional<OrderEntity> findOrderForView(UUID orderId, String orderName) {

        if (orderId != null) {
            return workLogService.findOrderById(orderId);
        }

        if (orderName != null && !orderName.isBlank()) {
            return workLogService.findOrderByName(orderName);
        }

        return Optional.empty();
    }
}