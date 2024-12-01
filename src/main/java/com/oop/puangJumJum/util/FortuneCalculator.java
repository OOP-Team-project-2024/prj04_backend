package com.oop.puangJumJum.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FortuneCalculator {
    public static int calculateFortuneScore(String dateString, String studentNumber, int emojiUnicode) {
        try {
            // 1. 입력 데이터 결합
            String combinedString = dateString + studentNumber + emojiUnicode;

            // 2. 해시 함수 적용
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(combinedString.getBytes(StandardCharsets.UTF_8));

            // 3. 해시 값을 숫자로 변환
            int hashInt = 0;
            for (int i = 0; i < 4; i++) {
                hashInt = (hashInt << 8) | (hashBytes[i] & 0xFF);
            }
            hashInt = Math.abs(hashInt);

            // 4. 점수 범위 조정
            return (hashInt % 51) + 50; // 50~100 사이의 값
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hash Algorithm not found", e);
        }
    }
}
