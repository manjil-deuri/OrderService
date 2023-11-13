package com.java.novice.OrderService.service;

import com.java.novice.OrderService.model.OrderRequest;
import com.java.novice.OrderService.model.OrderResponse;

public interface OrderService {
    OrderResponse placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetail(long id);
}
