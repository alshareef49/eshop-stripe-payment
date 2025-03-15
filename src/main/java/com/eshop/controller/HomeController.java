package com.eshop.controller;

import com.eshop.config.AuthCodeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class HomeController {

    private static Integer ORDER_ID;

    @Autowired
    private AuthCodeConfig authCodeConfig;

    @Autowired
    private RestTemplate template;

    @GetMapping("/success")
    public ResponseEntity<String> success(){
        template.exchange("http://localhost:3333/EShop/order-api/order/" + ORDER_ID + "/update/order-status",
                HttpMethod.PUT,
                authCodeConfig.getHeaderEntityWithBody("TRANSACTION_SUCCESS"),
                String.class);
        String success = "payment successfully done!";
        return new ResponseEntity<>(success, HttpStatus.OK);
    }
    public static void setOrderId(Integer orderId) {
        ORDER_ID = orderId;
    }
    @GetMapping("/failure")
    public ResponseEntity<String> failure() {
        template.exchange("http://localhost:3333/EShop/order-api/order/" + ORDER_ID + "/update/order-status",
                HttpMethod.PUT,
                authCodeConfig.getHeaderEntityWithBody("CANCELLED"),
                String.class);
        String failureMessage = "Payment failed! Please try again.";
        return new ResponseEntity<>(failureMessage, HttpStatus.BAD_REQUEST);
    }
}
