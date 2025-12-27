
package com.cfc.Payment_gateway.repository;

import com.cfc.Payment_gateway.entity.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentOrder, Long> {

    // Razorpay Order ID se order nikalne ke liye
    Optional<PaymentOrder> findByRazorpayOrderId(String razorpayOrderId);
}

