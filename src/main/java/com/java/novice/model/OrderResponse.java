package com.java.novice.model;

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
    private long id;
    @JsonInclude(value = NON_DEFAULT)
    private long productId;
    @JsonInclude(value = NON_DEFAULT)
    private long quantity;
    @JsonInclude(value = NON_DEFAULT)
    private Instant orderData;
    @JsonInclude(value = NON_DEFAULT)
    private String orderStatus;
    @JsonInclude(value = NON_DEFAULT)
    private long amount;

}
