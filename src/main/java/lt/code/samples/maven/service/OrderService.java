package lt.code.samples.maven.service;

import lt.code.samples.maven.order.Order;
import lt.code.samples.maven.order.QRCodeGenerator;
import lt.code.samples.maven.repository.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.zxing.WriterException;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order createOrder(Order order) {
        // OrderNumber turi būti priskirtas (PrePersist arba čia)
        // Svarbu: jei orderNumber pildomas automatiškai – pirmiau issaugom, tada atnaujinam QR
        if (order.getOrderNumber() == null) {
            order.setOrderNumber(java.util.UUID.randomUUID().toString());
        }
        String qrContent = "http://localhost:8080/orders/" + order.getOrderNumber();
        try {
            byte[] qrImage = QRCodeGenerator.generateQRCodeImage(qrContent, 200, 200);
            String base64 = Base64.getEncoder().encodeToString(qrImage);
            order.setQrCodeImageBase64(base64);
        } catch (WriterException | IOException e) {
            order.setQrCodeImageBase64(null);
        }
        return orderRepository.save(order);
    }
}
