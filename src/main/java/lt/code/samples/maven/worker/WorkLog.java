package lt.code.samples.maven.worker;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lt.code.samples.maven.order.Order;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Entity
@Data
public class WorkLog {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String workerName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @ManyToOne
    private Order order;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
