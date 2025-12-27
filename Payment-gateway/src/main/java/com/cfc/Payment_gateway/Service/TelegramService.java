
package com.cfc.Payment_gateway.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TelegramService {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.chat.id}")
    private String chatId;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendMessage(String message) {

        String url = "https://api.telegram.org/bot" + botToken +
                "/sendMessage?chat_id=" + chatId +
                "&text=" + message;

        restTemplate.getForObject(url, String.class);
    }
}

