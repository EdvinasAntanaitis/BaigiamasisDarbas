package lt.code.samples.maven.worklog.service;

import lt.code.samples.maven.order.model.OrderEntity;
import lt.code.samples.maven.order.repository.OrderRepository;
import lt.code.samples.maven.user.dto.UserDto;
import lt.code.samples.maven.user.service.UserService;
import lt.code.samples.maven.worklog.model.WorkLogEntity;
import lt.code.samples.maven.worklog.repository.WorkLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class WorkLogStartServiceTest {

    private OrderRepository orderRepository;
    private WorkLogRepository workLogRepository;
    private UserService userService;
    private WorkLogStartService service;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        workLogRepository = mock(WorkLogRepository.class);
        userService = mock(UserService.class);
        service = new WorkLogStartService(orderRepository, workLogRepository, userService);
    }

    @Test
    void startWork_buildsWorkerNameFromUserDto_andSavesWorklog() {
        OrderEntity order = new OrderEntity();
        order.setId(15L);
        order.setOrderNumber("ORD-15");
        when(orderRepository.findById(15L)).thenReturn(Optional.of(order));

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("worker1");

        UserDto dto = UserDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@acme.test")
                .role("ROLE_USER")
                .id(99L)
                .build();
        when(userService.getUserDtoByUsername("worker1")).thenReturn(dto);

        service.startWork(15L, auth, "Sanding");

        ArgumentCaptor<WorkLogEntity> cap = ArgumentCaptor.forClass(WorkLogEntity.class);
        verify(workLogRepository).save(cap.capture());

        WorkLogEntity saved = cap.getValue();
        assertThat(saved.getOrder()).isEqualTo(order);
        assertThat(saved.getOperationName()).isEqualTo("Sanding");
        assertThat(saved.getWorkerName()).isEqualTo("John Doe");
        assertThat(saved.isFaulty()).isFalse();
        assertThat(saved.isFaultFixed()).isFalse();
    }

    @Test
    void startWork_fallsBackToAuthName_whenDtoEmpty() {
        OrderEntity order = new OrderEntity();
        order.setId(7L);
        order.setOrderNumber("ORD-7");
        when(orderRepository.findById(7L)).thenReturn(Optional.of(order));

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("workerX");

        // Simuliuojame, kad UserService grąžina DTO be vardo/pavardės
        UserDto dto = UserDto.builder().firstName(null).lastName(null).id(1L).role("ROLE_USER").build();
        when(userService.getUserDtoByUsername("workerX")).thenReturn(dto);

        service.startWork(7L, auth, "Painting");

        ArgumentCaptor<WorkLogEntity> cap = ArgumentCaptor.forClass(WorkLogEntity.class);
        verify(workLogRepository).save(cap.capture());

        assertThat(cap.getValue().getWorkerName()).isEqualTo("workerX");
    }
}
