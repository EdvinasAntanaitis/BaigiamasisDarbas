package lt.code.samples.maven.order;

import jakarta.persistence.*;
import lombok.Data;
import lt.code.samples.maven.model.WorkLogEntity;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "qr_code_image")
    private String qrCodeImageBase64;

    // Pridėtas orderNumber laukas, nes duomenų bazė reikalauja
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
    private List<PartGroup> partGroups;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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
        // Jei orderNumber nepriskirtas – priskiriam UUID automatiškai
        if (orderNumber == null) {
            orderNumber = java.util.UUID.randomUUID().toString();
        }
    }
}
