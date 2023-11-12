package com.java.novice.controller;

import com.java.novice.model.OrderRequest;
import com.java.novice.model.OrderResponse;
import com.java.novice.service.OrderService;
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

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderDetail(@PathVariable("id") long id){
        OrderResponse orderResponse = orderService.getOrderDetail(id);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }
}


