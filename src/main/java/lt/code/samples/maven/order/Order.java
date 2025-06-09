package lt.code.samples.maven.order;

import jakarta.persistence.*;
import lombok.Data;
import lt.code.samples.maven.worker.WorkLog;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderName;
    private String material;
    private String paintColor;
    private LocalDateTime creationDate;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PartGroup> partGroups;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkLog> workLogs;
}
