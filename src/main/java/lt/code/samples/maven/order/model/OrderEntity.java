package lt.code.samples.maven.order.model;

import jakarta.persistence.*;
import lombok.Data;
import lt.code.samples.maven.worklog.model.WorkLogEntity;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "qr_code_image", columnDefinition = "TEXT") // paliekam TEXT
    private String qrCodeImageBase64;

    @Column(name = "order_number", nullable = false, unique = true)
    private String orderNumber;

    private String orderName;
    private String material;
    private String paintColor;

    @Column(updatable = false, name = "creation_date")
    private LocalDateTime creationDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Boolean completed = false;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PartGroupEntity> partGroups;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<WorkLogEntity> workLogs;


    public boolean hasUnfixedFaults() {
        return workLogs != null && workLogs.stream()
                .anyMatch(log -> log.isFaulty() && !log.isFaultFixed());
    }

    @PrePersist
    public void prePersist() {
        if (creationDate == null) {
            creationDate = LocalDateTime.now();
        }
        if (orderNumber == null || orderNumber.isBlank()) {
            orderNumber = java.util.UUID.randomUUID().toString();
        }
    }

}
