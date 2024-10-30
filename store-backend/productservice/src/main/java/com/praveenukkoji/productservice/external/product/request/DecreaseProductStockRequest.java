package com.praveenukkoji.productservice.external.product.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DecreaseProductStockRequest {
    private UUID id;
    private Integer quantity;
}
