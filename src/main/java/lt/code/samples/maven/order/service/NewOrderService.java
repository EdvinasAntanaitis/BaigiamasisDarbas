package lt.code.samples.maven.order.service;

import lt.code.samples.maven.order.dto.MaterialGroupDTO;
import lt.code.samples.maven.order.dto.OrderFormDTO;
import lt.code.samples.maven.order.dto.PartDTO;
import lt.code.samples.maven.order.model.OrderEntity;
import lt.code.samples.maven.order.model.PartGroupEntity;
import lt.code.samples.maven.order.model.PartEntity;

import lombok.RequiredArgsConstructor;
import lt.code.samples.maven.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewOrderService {

    private final OrderRepository orderRepository;

    public void createOrderFromDTO(OrderFormDTO dto) {
        OrderEntity order = convertDtoToEntity(dto);
        orderRepository.save(order);
    }

    private OrderEntity convertDtoToEntity(OrderFormDTO dto) {
        OrderEntity order = new OrderEntity();
        order.setOrderName(dto.getOrderName());

        List<PartGroupEntity> groups = new ArrayList<>();
        if (dto.getPartGroups() != null) {
            for (MaterialGroupDTO gDto : dto.getPartGroups()) {
                PartGroupEntity g = new PartGroupEntity();
                g.setMaterial(gDto.getMaterial());
                g.setPaintColor(gDto.getPaintColor());
                g.setOrder(order);

                List<PartEntity> parts = new ArrayList<>();
                if (gDto.getParts() != null) {
                    for (PartDTO pDto : gDto.getParts()) {
                        PartEntity p = new PartEntity();
                        p.setLength(pDto.getLength());
                        p.setWidth(pDto.getWidth());
                        p.setThickness(pDto.getThickness());
                        p.setAmount(pDto.getAmount());
                        p.setPaintedArea(pDto.getPaintedArea());
                        p.setPartGroup(g);
                        parts.add(p);
                    }
                }
                g.setParts(parts);
                groups.add(g);
            }
        }
        order.setPartGroups(groups);
        return order;
    }
}
