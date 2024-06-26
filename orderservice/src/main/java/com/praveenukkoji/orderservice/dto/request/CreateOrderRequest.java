package com.praveenukkoji.orderservice.dto.request;

import com.praveenukkoji.orderservice.dto.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    private List<Product> products;
    private UUID createdBy;
}
