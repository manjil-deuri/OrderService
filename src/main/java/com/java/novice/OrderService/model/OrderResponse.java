package com.java.novice.OrderService.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private long orderId;
    @JsonInclude(value = NON_DEFAULT)
    private Instant orderDate;
    @JsonInclude(value = NON_DEFAULT)
    private String orderStatus;
    @JsonInclude(value = NON_DEFAULT)
    private long amount;
    @JsonInclude(value = NON_DEFAULT)
    private ProductDetails productDetails;
    @JsonInclude(value = NON_DEFAULT)
    private PaymentDetails paymentDetails;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductDetails {
        private long productId;
        private String productName;
        private long price;
        private long quantity;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PaymentDetails {
        private long paymentId;
        private long orderId;
        private PaymentMode paymentMode;
        private String referenceNumber;
        private Instant paymentDate;
        private String paymentStatus;
        private long amount;
    }

}
