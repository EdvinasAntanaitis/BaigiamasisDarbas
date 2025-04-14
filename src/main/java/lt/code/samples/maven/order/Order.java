package lt.code.samples.maven.order;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_name", nullable = false)
    private String orderName;

    private String material;
    private String color;
    private String barcode;
    private double length;
    private double width;
    private double thickness;
    private int amount;
    private String description;

    @Column(name = "edge_type")
    private String edgeType;

    private String status;

    @Column(name = "edited_by")
    private String editedBy;

    @Column(name = "created_at")
    private String createdAt;
}