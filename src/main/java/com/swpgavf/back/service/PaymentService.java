package com.swpgavf.back.service;

import com.swpgavf.back.dto.PaymentResponseDTO;
import com.swpgavf.back.entity.Order;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.swpgavf.back.entity.Payment;
import com.swpgavf.back.repository.IPaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService implements IPaymentService {

    @Value("${stripe.key.secret}")
    String secretKey;

    private final IPaymentRepository paymentRepository;

    public PaymentService(IPaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentResponseDTO createPaymentIntent(Order order, String paymentMethodId) throws Exception {
        Stripe.apiKey = secretKey; // Set the Stripe secret key here
        System.out.println("Stripe API Key seteada: " + secretKey); // Debugging only
        System.out.println("Order details before payment: " + order);
//
//        Wasnt working
//        Order details before payment: Order(id=5, orderDate=2024-11-01, status=PENDING, products=[Product(id=1, sku=LED123, name=LED Bulb, description=A bright and energy-efficient LED bulb., category=Lighting, stock=100, price=5.99, brand=BrightLight), Product(id=2, sku=SOL456, name=Solar Panel, description=High-efficiency solar panel for residential use., category=Renewable Energy, stock=50, price=199.99, brand=EcoPower)], amount=20598, currency=usd)
//        Payment processing failed: A `return_url` must be specified because this Payment Intent is configured to automatically accept the payment methods enabled in the Dashboard, some of which may require a full page redirect to succeed. If you do not want to accept redirect-based payment methods, set `automatic_payment_methods[enabled]` to `true` and `automatic_payment_methods[allow_redirects]` to `never` when creating Setup Intents and Payment Intents.; request-id: req_mjlOiBWWeKkHEH
//
//        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
//                .setAmount(order.getAmount())
//                .setCurrency(order.getCurrency())
//                .setDescription("Payment for order #" + order.getId())
//                .setPaymentMethod(paymentMethodId)
//                .setConfirm(true)
//                .build();

//        Fixed 1. Specify a Return URL:
//        """
//        When creating the PaymentIntent, you can include a return_url. This URL is
//        where the user will be redirected after completing their payment. Here's how you can
//        modify your createPaymentIntent method in the PaymentService to include this:
//        """
//        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
//                .setAmount(order.getAmount())
//                .setCurrency(order.getCurrency())
//                .setDescription("Payment for order #" + order.getId())
//                .setPaymentMethod(paymentMethodId)
//                .setConfirm(true)
//                .setReturnUrl("http://your-website.com/return") // Update with your actual return URL
//                .build();
//
//        Fixed 2: Automatic Payment Methods Configuration:
//                """
//                If you want to avoid specifying a return_url, you can enable automatic payment methods
//                 and set their configuration to not require redirects.
//                  You can do this by modifying your PaymentIntentCreateParams
//                """

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(order.getAmount())
                .setCurrency(order.getCurrency())
                .setDescription("Payment for order #" + order.getId())
                .setPaymentMethod(paymentMethodId)
                .setConfirm(true)
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER) // This prevents requiring a redirect
                                .build())
                .build();

        PaymentIntent intent = PaymentIntent.create(params);

        // Create a Payment record if you are persisting it
        Payment payment = new Payment();
        payment.setOrderId(order.getId());
        payment.setAmount(order.getAmount());
        payment.setCurrency(order.getCurrency());
        payment.setStatus(intent.getStatus());
        payment.setPaymentIntentId(intent.getId());
        payment.setCreatedAt(LocalDateTime.now());

        // Save the payment record
        paymentRepository.save(payment);

        // Prepare response DTO
        PaymentResponseDTO responseDTO = new PaymentResponseDTO();
        responseDTO.setPaymentIntentId(intent.getId());
        responseDTO.setStatus(intent.getStatus());
        responseDTO.setClientSecret(intent.getClientSecret());

        return responseDTO;
    }
}
