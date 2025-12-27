package com.cfc.Payment_gateway.Service;

import com.cfc.Payment_gateway.entity.PaymentOrder;
import com.cfc.Payment_gateway.repository.PaymentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private TelegramService telegramService; // 🔔 ADMIN TELEGRAM

    @Autowired
    private EmailService emailService;       // 📧 USER EMAIL

    @Autowired
    private PaymentRepository paymentRepository;

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    // ================= CREATE ORDER =================
    public PaymentOrder createOrder(PaymentOrder order) throws RazorpayException {

        RazorpayClient client = new RazorpayClient(keyId, keySecret);

        JSONObject options = new JSONObject();
        options.put("amount", order.getAmount() * 100);
        options.put("currency", "INR");
        options.put("receipt", "txn_" + UUID.randomUUID());

        Order razorpayOrder = client.orders.create(options);

        order.setRazorpayOrderId(razorpayOrder.get("id"));
        order.setStatus("CREATED");
        order.setCreatedAt(LocalDateTime.now());

        return paymentRepository.save(order);
    }

    // ================= PAYMENT SUCCESS =================
    public PaymentOrder updateOrder(String razorpayOrderId, String razorpayPaymentId) {

        PaymentOrder order = paymentRepository
                .findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setRazorpayPaymentId(razorpayPaymentId);
        order.setStatus("PAID");
        order.setPaidAt(LocalDateTime.now());

        PaymentOrder savedOrder = paymentRepository.save(order);

        // ================= 📧 EMAIL to USER =================
        emailService.sendPaymentSuccessEmail(
                savedOrder.getEmail(),
                savedOrder.getName(),
                savedOrder.getCourse(),
                savedOrder.getAmount()
        );

        // ================= 🔔 TELEGRAM to ADMIN =================
        telegramService.sendMessage(
                "💰 New Payment Received\n\n" +
                        "Name: " + savedOrder.getName() + "\n" +
                        "Course: " + savedOrder.getCourse() + "\n" +
                        "Amount: ₹" + savedOrder.getAmount() + "\n\n" +
                        "Order ID: " + savedOrder.getRazorpayOrderId() + "\n" +
                        "Payment ID: " + savedOrder.getRazorpayPaymentId()
        );

        return savedOrder;
    }
}
