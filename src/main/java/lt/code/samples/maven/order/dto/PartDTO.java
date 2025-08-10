package lt.code.samples.maven.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lt.code.samples.maven.order.model.PaintedArea;

@Data
public class PartDTO {
    @Positive(message = "Length must be > 0")
    private double length;

    @Positive(message = "Width must be > 0")
    private double width;

    @Positive(message = "Thickness must be > 0")
    private double thickness;

    @Min(value = 1, message = "Amount must be >= 1")
    private int amount = 1;

    @NotNull(message = "Painted area is required")
    private PaintedArea paintedArea = PaintedArea.ALL_SIDES;
}
