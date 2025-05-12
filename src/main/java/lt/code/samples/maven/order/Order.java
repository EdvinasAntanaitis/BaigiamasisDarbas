package lt.code.samples.maven.order;

import jakarta.persistence.*;
import lombok.Data;
import lt.code.samples.maven.worker.WorkLog;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderName;
    private String paintColor;
    private String material;
    private LocalDateTime creationDate;

    @Lob
    private String description;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<WorkLog> workLogs;
}