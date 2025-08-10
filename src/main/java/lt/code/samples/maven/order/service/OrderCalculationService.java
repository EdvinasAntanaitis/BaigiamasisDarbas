package lt.code.samples.maven.order.service;

import lt.code.samples.maven.order.model.OrderEntity;
import lt.code.samples.maven.order.model.PartEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderCalculationService {

    public double calculateTotalArea(OrderEntity order) {
        return order.getPartGroups().stream()
                .flatMap(g -> g.getParts().stream())
                .mapToDouble(this::calculatePaintedArea)
                .sum();
    }

    public Map<String, Double> calculateAreaPerGroup(OrderEntity order) {
        return order.getPartGroups().stream()
                .collect(Collectors.toMap(
                        g -> g.getMaterial() + "-" + g.getPaintColor(),
                        g -> g.getParts().stream()
                                .mapToDouble(this::calculatePaintedArea)
                                .sum()
                ));
    }

    private double calculatePaintedArea(PartEntity part) {
        double L = part.getLength()    / 1000d;
        double W = part.getWidth()     / 1000d;
        double T = part.getThickness() / 1000d;
        return switch (part.getPaintedArea()) {
            case ALL_SIDES           -> 2 * (L*W + L*T + W*T);
            case ONE_EDGE            -> T * W;
            case TWO_EDGES           -> 2 * T * W;
            case THREE_EDGES         -> 2 * (L*T + W*T) + T*W;
            case ONE_SIDE_NO_EDGES   -> L * W;
            case ONE_SIDE_WITH_EDGES -> L*W + 2*(L*T + W*T);
        };
    }
}
