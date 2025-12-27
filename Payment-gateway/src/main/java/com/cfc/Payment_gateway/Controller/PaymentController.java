
package com.cfc.Payment_gateway.Controller;

import com.cfc.Payment_gateway.Service.PaymentService;
import com.cfc.Payment_gateway.entity.PaymentOrder;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    private PaymentService service;

    @PostMapping("/create-order")
    public PaymentOrder createOrder(@RequestBody PaymentOrder order) throws Exception {
        return service.createOrder(order);
    }

    @PutMapping("/update-order")
    public PaymentOrder updateOrder(@RequestBody PaymentOrder order) {
        return service.updateOrder(
                order.getRazorpayOrderId(),
                order.getRazorpayPaymentId()
        );
    }
}

