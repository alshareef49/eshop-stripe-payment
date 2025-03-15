package com.eshop.service;

import com.eshop.dto.ProductRequest;
import com.eshop.dto.StripeResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    @Value("${stripe.secretKey}")
    private String secretKey;


        public StripeResponse checkoutProducts(ProductRequest productRequest,String orderId,String token) {
            Stripe.apiKey = secretKey;

            SessionCreateParams.LineItem.PriceData.ProductData productData =
                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName(productRequest.getName())
                            .build();

            SessionCreateParams.LineItem.PriceData priceData =
                    SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency(productRequest.getCurrency() != null ? productRequest.getCurrency() : "USD")
                            .setUnitAmount(productRequest.getAmount())
                            .setProductData(productData)
                            .build();

            SessionCreateParams.LineItem lineItem =
                    SessionCreateParams
                            .LineItem.builder()
                            .setQuantity(productRequest.getQuantity())
                            .setPriceData(priceData)
                            .build();

            SessionCreateParams params =
                    SessionCreateParams.builder()
                            .setMode(SessionCreateParams.Mode.PAYMENT)
                            .setSuccessUrl("http://localhost:3333/EShop/success?orderId="+orderId+"&token="+token)
                            .setCancelUrl("http://localhost:3333/EShop/cancel?orderId="+orderId+"&token="+token)
                            .addLineItem(lineItem)
                            .build();

            Session session = null;
            try {
                session = Session.create(params);
            } catch (StripeException e) {
                throw new RuntimeException(e);
            }

            assert session != null;
            return StripeResponse
                    .builder()
                    .status("SUCCESS")
                    .message("Payment session created ")
                    .sessionId(session.getId())
                    .sessionUrl(session.getUrl())
                    .build();
        }

}
