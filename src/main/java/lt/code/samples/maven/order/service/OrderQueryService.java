package lt.code.samples.maven.order.service;

import lombok.RequiredArgsConstructor;
import lt.code.samples.maven.order.model.OrderEntity;
import lt.code.samples.maven.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderQueryService {

    private final OrderRepository orderRepository;

    public List<OrderEntity> findAll() {
        return orderRepository.findAll();
    }
}
