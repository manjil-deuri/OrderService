package com.java.novice.OrderService.service;

import com.java.novice.OrderService.exception.CustomException;
import com.java.novice.OrderService.external.client.PaymentService;
import com.java.novice.OrderService.external.client.ProductService;
import com.java.novice.OrderService.external.response.PaymentResponse;
import com.java.novice.OrderService.external.response.ProductResponse;
import com.java.novice.OrderService.model.PaymentMode;
import com.java.novice.OrderService.repository.OrderRepository;
import com.java.novice.OrderService.entity.Order;
import com.java.novice.OrderService.external.request.PaymentRequest;
import com.java.novice.OrderService.model.OrderRequest;
import com.java.novice.OrderService.model.OrderResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import static java.time.Instant.now;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestTemplate restTemplate;

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

        return OrderResponse.builder().orderId(order.getId()).build();
    }

    @Override
    public OrderResponse getOrderDetail(long id) {
        log.info("Fetching order details for order id: {}", id);
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new CustomException("Order not found with Id: " + id, "PRODUCT_NOT_FOUND", 404));

        log.info("Invoking Product Service to fetch product for  id: {}", order.getProductId());
        ProductResponse productResponse = restTemplate.getForObject("http://PRODUCT-SERVICE/product/" + order.getProductId(), ProductResponse.class);

        log.info("Getting payment details for order Id: {}", order.getId());
        PaymentResponse paymentResponse = restTemplate.getForObject("http://PAYMENT-SERVICE/payment/order/" + order.getId(), PaymentResponse.class);

        OrderResponse.ProductDetails productDetails = OrderResponse.ProductDetails.builder()
                .productId(productResponse.getProductId())
                .productName(productResponse.getProductName())
                .quantity(productResponse.getQuantity())
                .price(productResponse.getPrice())
                .build();

        OrderResponse.PaymentDetails paymentDetails = OrderResponse.PaymentDetails.builder()
                .paymentId(paymentResponse.getPaymentId())
                .paymentDate(paymentResponse.getPaymentDate())
                .referenceNumber(paymentResponse.getReferenceNumber())
                .paymentMode(paymentResponse.getPaymentMode())
                .paymentStatus(paymentResponse.getPaymentStatus())
                .build();
        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(order.getId())
                .amount(order.getAmount())
                .orderStatus(order.getOrderStatus())
                .orderDate(order.getOrderDate())
                .productDetails(productDetails)
                .paymentDetails(paymentDetails)
                .build();
        return orderResponse;
    }
}
