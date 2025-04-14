package lt.code.samples.maven.repository;

import lt.code.samples.maven.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
