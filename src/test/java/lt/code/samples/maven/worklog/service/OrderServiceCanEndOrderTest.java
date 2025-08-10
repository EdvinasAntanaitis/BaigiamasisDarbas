package lt.code.samples.maven.order.service;

import lt.code.samples.maven.order.model.OrderEntity;
import lt.code.samples.maven.worklog.model.WorkLogEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderServiceCanEndOrderTest {

    private final OrderService service = new OrderService(null);

    private WorkLogEntity wl(boolean faulty, boolean fixed, boolean hasEnd) {
        WorkLogEntity w = new WorkLogEntity();
        w.setFaulty(faulty);
        w.setFaultFixed(fixed);
        w.setStartTime(LocalDateTime.now());
        if (hasEnd) w.setEndTime(LocalDateTime.now());
        return w;
    }

    @Test
    void canEnd_true_whenNoLogs() {
        OrderEntity o = new OrderEntity();
        assertThat(service.canEndOrder(o)).isTrue();
    }

    @Test
    void canEnd_true_whenAllFinishedAndNoFaults() {
        OrderEntity o = new OrderEntity();
        o.setWorkLogs(List.of(
                wl(false, false, true),
                wl(false, false, true)
        ));
        assertThat(service.canEndOrder(o)).isTrue();
    }

    @Test
    void canEnd_false_whenAnyLogIsInProgress() {
        OrderEntity o = new OrderEntity();
        o.setWorkLogs(List.of(
                wl(false, false, true),
                wl(false, false, false) // nebaigtas
        ));
        assertThat(service.canEndOrder(o)).isFalse();
    }

    @Test
    void canEnd_false_whenAnyFaultNotFixed() {
        OrderEntity o = new OrderEntity();
        o.setWorkLogs(List.of(
                wl(true, false, true), // brokas, nesutvarkyta
                wl(false, false, true)
        ));
        assertThat(service.canEndOrder(o)).isFalse();
    }

    @Test
    void canEnd_true_whenAllFaultsFixed() {
        OrderEntity o = new OrderEntity();
        o.setWorkLogs(List.of(
                wl(true, true, true), // brokas sutvarkytas
                wl(false, false, true)
        ));
        assertThat(service.canEndOrder(o)).isTrue();
    }
}
