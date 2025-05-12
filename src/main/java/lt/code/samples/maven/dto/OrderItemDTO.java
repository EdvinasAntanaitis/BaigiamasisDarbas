package lt.code.samples.maven.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Double length;
    private Double width;
    private Double thickness;
    private Integer amount;
    private String sides;
}