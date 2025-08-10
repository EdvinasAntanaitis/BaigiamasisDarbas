package lt.code.samples.maven.order.model;

import jakarta.persistence.*;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class PartGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String material;
    private String paintColor;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false) // FK į orders.id
    private OrderEntity order;

    @OneToMany(mappedBy = "partGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PartEntity> parts = new ArrayList<>();
}
