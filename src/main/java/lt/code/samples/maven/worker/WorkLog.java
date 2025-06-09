package lt.code.samples.maven.worker;

import jakarta.persistence.*;
import lombok.Data;
import lt.code.samples.maven.order.Order;

import java.time.LocalDateTime;

@Entity
@Data
public class WorkLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String workerName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
