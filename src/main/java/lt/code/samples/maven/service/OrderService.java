package lt.code.samples.maven.service;

import lt.code.samples.maven.order.Order;
import lt.code.samples.maven.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    // Čia galėsi lengvai pridėti kitus metodus ateityje, pvz.:
    // public List<Order> getAllOrders() { ... }
}
