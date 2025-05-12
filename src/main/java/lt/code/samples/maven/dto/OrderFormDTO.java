package lt.code.samples.maven.dto;

import lombok.Data;

@Data
public class OrderFormDTO {
    private String orderName;
    private String paintColor;
    private String material;
    private String description;
}
