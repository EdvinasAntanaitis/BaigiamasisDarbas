package lt.code.samples.maven.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@Data
public class OrderFormDTO {
    private String orderName;
    private String paintColor;
    private String material;
    private String description;

    private List<OrderItemDTO> parts = new ArrayList<>();
}
