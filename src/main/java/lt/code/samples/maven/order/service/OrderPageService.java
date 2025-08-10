package lt.code.samples.maven.order.service;

import lombok.RequiredArgsConstructor;
import lt.code.samples.maven.common.exception.ConflictException;
import lt.code.samples.maven.common.exception.NotFoundException;
import lt.code.samples.maven.order.model.OrderEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderPageService {

    private final OrderService orderService;
    private final OrderCalculationService calc;

    public ViewData getViewData(Long id) {
        OrderEntity o = orderService.getOrderById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        double total = calc.calculateTotalArea(o);
        Map<String, Double> perGroup = calc.calculateAreaPerGroup(o);
        return new ViewData(o, total, perGroup);
    }

    public void endOrder(Long orderId) {
        OrderEntity o = orderService.getOrderById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        if (!orderService.canEndOrder(o)) {
            throw new ConflictException("Cannot end order – some tasks are unfinished or faulty.");
        }
        orderService.endOrder(o);
    }

    public void deleteOrder(Long id) {
        orderService.getOrderById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        orderService.deleteOrder(id);
    }

    public OrderEntity getForEdit(Long id) {
        return orderService.getOrderById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
    }

    public record ViewData(OrderEntity order, double totalArea, Map<String, Double> areaPerGroup) {}
}
