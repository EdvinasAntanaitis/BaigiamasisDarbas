package lt.code.samples.maven.worklog.repository;

import lt.code.samples.maven.order.model.OrderEntity;
import lt.code.samples.maven.order.repository.OrderRepository;
import lt.code.samples.maven.worklog.model.WorkLogEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class WorkLogRepositoryDataJpaTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    WorkLogRepository workLogRepository;

    @Test
    void savesWorkLogForOrder() {
        OrderEntity order = new OrderEntity();
        order.setOrderNumber("ORD-TEST-1");
        OrderEntity savedOrder = orderRepository.save(order);

        WorkLogEntity wl = new WorkLogEntity();
        wl.setOrder(savedOrder);
        wl.setWorkerName("Tester");
        wl.setOperationName("Sanding");
        wl.setStartTime(LocalDateTime.now());
        wl.setFaulty(false);
        wl.setFaultFixed(false);

        WorkLogEntity saved = workLogRepository.save(wl);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getOrder().getId()).isEqualTo(savedOrder.getId());
    }
}
