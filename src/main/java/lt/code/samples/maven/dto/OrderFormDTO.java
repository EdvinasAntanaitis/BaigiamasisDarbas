package lt.code.samples.maven.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderFormDTO {
    private String orderName;
    private String description;
    private List<PartGroupDTO> partGroups = new ArrayList<>();
}
