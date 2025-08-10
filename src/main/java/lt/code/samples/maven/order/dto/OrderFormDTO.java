package lt.code.samples.maven.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderFormDTO {
    private List<OrderItemDTO> items = new ArrayList<>();
    @NotBlank(message = "Order name is required")
    private String orderName;

    private String description;

    @Valid
    private List<MaterialGroupDTO> partGroups = new ArrayList<>();
}
