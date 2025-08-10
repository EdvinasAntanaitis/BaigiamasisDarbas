package lt.code.samples.maven.order.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class PartGroupDTO {
    private String material;
    private String paintColor;

    private List<PartDTO> parts = new ArrayList<>();
}
