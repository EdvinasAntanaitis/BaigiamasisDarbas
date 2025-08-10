package lt.code.samples.maven.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MaterialGroupDTO {
    @NotBlank(message = "Material is required")
    private String material;

    @NotBlank(message = "Paint color is required")
    private String paintColor;

    @Valid
    private List<PartDTO> parts = new ArrayList<>();
}
