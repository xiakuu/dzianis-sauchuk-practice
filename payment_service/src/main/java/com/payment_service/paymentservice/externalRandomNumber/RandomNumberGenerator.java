package com.payment_service.paymentservice.externalRandomNumber;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomNumberGenerator {
    Random rand = new Random();

    public int getRandomNumber(int max) {
        return rand.nextInt(max);
    }
}
