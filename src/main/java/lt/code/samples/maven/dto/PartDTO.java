package lt.code.samples.maven.dto;

import lombok.Data;
import lt.code.samples.maven.order.PaintedArea;

@Data
public class PartDTO {
    private double length;
    private double width;
    private double thickness;
    private int amount;
    private PaintedArea paintedArea;
}
