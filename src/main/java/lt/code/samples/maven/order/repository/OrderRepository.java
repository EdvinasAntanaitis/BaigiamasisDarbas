package lt.code.samples.maven.order.repository;

import lt.code.samples.maven.order.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

}
