package com.java.novice.OrderService.external.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private long productId;
    @JsonInclude(Include.NON_DEFAULT)
    private String productName;
    @JsonInclude(Include.NON_DEFAULT)
    private long price;
    @JsonInclude(Include.NON_DEFAULT)
    private long quantity;
}
