package com.java.novice.service;

import com.java.novice.model.OrderRequest;
import com.java.novice.model.OrderResponse;

public interface OrderService {
    OrderResponse placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetail(long id);
}
