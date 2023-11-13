package com.java.novice.service;

import com.java.novice.entity.Order;
import com.java.novice.exception.CustomException;
import com.java.novice.external.client.PaymentService;
import com.java.novice.external.client.ProductService;
import com.java.novice.external.request.PaymentRequest;
import com.java.novice.model.OrderRequest;
import com.java.novice.model.OrderResponse;
import com.java.novice.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.time.Instant.now;
import static org.springframework.beans.BeanUtils.copyProperties;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductService productService;

    @Autowired
    PaymentService paymentService;

    @Override
    public OrderResponse placeOrder(OrderRequest orderRequest) {
        log.info("Placing Order Request: {}", orderRequest);
        productService.reduceQuantity(orderRequest.getProductId(),orderRequest.getQuantity());

        log.info("Creating order with order status CREATED");
        Order order = Order
                .builder()
                .productId(orderRequest.getProductId())
                .quantity(orderRequest.getQuantity())
                .orderStatus("CREATED")
                .amount(orderRequest.getTotalAmount())
                .orderDate(now())
                .build();
        orderRepository.save(order);

        String orderStatus = null;
        log.info("Calling Payment Service to complete payment");
        try {
            paymentService.processPayment(
                    PaymentRequest.builder()
                            .orderId(order.getId())
                            .amount(orderRequest.getTotalAmount())
                            .paymentMode(orderRequest.getPaymentMode())
                            .build()
            );
            log.info("Payment done successfully, changing Order Status to PLACED");
            orderStatus = "PLACED";
        }catch (Exception e){
            log.error("Error occurred in payment, changing Order Status to PAYMENT_FAILED ");
            orderStatus = "PAYMENT_FAILED";
        }

        order.setOrderStatus(orderStatus);
        orderRepository.save(order);

        return OrderResponse.builder().id(order.getId()).build();
    }

    @Override
    public OrderResponse getOrderDetail(long id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new CustomException("Order not found with Id: " + id, "PRODUCT_NOT_FOUND", 404));
        OrderResponse orderResponse = new OrderResponse();
        copyProperties(order, orderResponse);
        return orderResponse;
    }
}
