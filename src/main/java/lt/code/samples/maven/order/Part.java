package lt.code.samples.maven.order;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double length;
    private double width;
    private double thickness;
    private int amount;

    @Enumerated(EnumType.STRING)
    private PaintedArea paintedArea;

    @ManyToOne
    @JoinColumn(name = "part_group_id")
    private PartGroup partGroup;
}