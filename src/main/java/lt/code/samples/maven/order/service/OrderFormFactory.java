package lt.code.samples.maven.order.service;

import lt.code.samples.maven.order.dto.MaterialGroupDTO;
import lt.code.samples.maven.order.dto.OrderFormDTO;
import lt.code.samples.maven.order.dto.PartDTO;
import org.springframework.stereotype.Service;

@Service
public class OrderFormFactory {

    public OrderFormDTO emptyFormWithOneGroupAndPart() {
        OrderFormDTO form = new OrderFormDTO();
        MaterialGroupDTO group = new MaterialGroupDTO();
        group.getParts().add(new PartDTO());
        form.getPartGroups().add(group);
        return form;
    }
}
