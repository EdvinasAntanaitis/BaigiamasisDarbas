package lt.code.samples.maven.model;

import jakarta.persistence.*;
import lombok.Data;
import lt.code.samples.maven.order.Order;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "work_log_entity")
public class WorkLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String workerName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String operationName;
    private String paintType;
    private boolean faulty;

    @Column(columnDefinition = "TEXT")
    private String faultDescription;

    @Column(nullable = false)
    private boolean faultFixed = false;


    @ManyToOne
    private Order order;
}
