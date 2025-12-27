
package com.cfc.Payment_gateway.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPaymentSuccessEmail(
            String toEmail,
            String name,
            String course,
            double amount
    ) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Payment Successful - " + course);

        message.setText(
                "Hello " + name + ",\n\n" +
                        "✅ Your payment has been successfully completed.\n\n" +
                        "Course : " + course + "\n" +
                        "Amount Paid : ₹" + amount + "\n\n" +
                        "Thank you for choosing us.\n\n" +
                        "Team CFC"
        );

        mailSender.send(message);
    }
}
