package com.eshop.controller;

import com.eshop.config.AuthCodeConfig;
import com.eshop.dto.ProductRequest;
import com.eshop.dto.StripeResponse;
import com.eshop.service.StripeService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Objects;

@RestController
@RequestMapping("/product/v1")
public class ProductCheckoutController {

    private final StripeService stripeService;

    public ProductCheckoutController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<StripeResponse> checkoutProducts(@RequestBody ProductRequest productRequest,@RequestParam("orderId") String orderId,@RequestHeader HttpHeaders headers) {
        AuthCodeConfig.TOKEN = Objects.requireNonNull(headers.get("Authorization")).getFirst();
        StripeResponse stripeResponse = stripeService.checkoutProducts(productRequest,orderId,AuthCodeConfig.TOKEN);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(stripeResponse);
    }
}
