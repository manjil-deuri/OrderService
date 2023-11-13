package com.java.novice.OrderService.controller;

import com.java.novice.OrderService.model.OrderRequest;
import com.java.novice.OrderService.model.OrderResponse;
import com.java.novice.OrderService.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Log4j2
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping()
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest orderRequest){
        OrderResponse orderResponse = orderService.placeOrder(orderRequest);
        log.info("Order Id: {}",orderResponse);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderDetail(@PathVariable("orderId") long orderId){
        OrderResponse orderResponse = orderService.getOrderDetail(orderId);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }
}


