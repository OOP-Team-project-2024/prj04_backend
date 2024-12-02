package com.oop.puangJumJum.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.oop.puangJumJum.exception.OpenAIServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class OpenAIService {
    @Value("${openai.api.key}")
    private String OPENAI_API_KEY;
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private final Gson gson = new Gson();
    public String generateFortuneDescription(String type, int score, String userName) {
        String prompt = String.format(
                "'%s'에 대한 운세를 지어내 알려주세요. 점수는 %d점, 사용자 이름은 %s입니다." ,
                type,
                score,
                userName);
        String systemMessage = "당신은 한국어로 운세를 알려주는 점술가입니다. " +
                "주어진 운세 분류, 점수, 사용자 이름에 대한 운세를 지어내주세요. " +
                "운세 분류는 '재물운', '학업운', '애정운', '건강운', '총평' 중 하나입니다 ." +
                "점수는 최저 50점부터 최고 100점으로 주어지며, 사용자 이름은 한글로 주어집니다. " +
                "점수에 대한 평가, 주의해야할 사항을 언급하세요." +
                "최소 2줄에서 최대 4줄의 문장을 제공하세요. " +
                "예시 : 오늘 길동님의 재물운은 매우 좋습니다. 돈 걱정은 하지 않아도 되는 하루입니다. 기본적으로 안정적인 재물 운이 유지되는 시기이니 돈과 관련된 스트레스는 적을 것입니다. 다만 큰 돈을 벌고싶은 마음이 가득하다면 스트레스를 받을 수 있습니다.";
        try {
            // JSON 요청 바디 생성
            String requestBody = "{"
                    + "\"model\": \"gpt-3.5-turbo\","
                    + "\"messages\": ["
                    + "{\"role\": \"system\", \"content\": \"" + escapeJson(systemMessage) + "\"},"
                    + "{\"role\": \"user\", \"content\": \"" + escapeJson(prompt) + "\"}"
                    + "],"
                    + "\"max_tokens\": 500"
                    + "}";

            // HTTP 연결 설정
            URL url = new URL(OPENAI_API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 요청 메서드와 헤더 설정
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + OPENAI_API_KEY);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);

            // 요청 바디 전송
            try (OutputStream os = connection.getOutputStream()) {
                os.write(requestBody.getBytes("UTF-8"));
                os.flush();
            }

            // 응답 코드 확인
            int responseCode = connection.getResponseCode();
            String responseMessage = connection.getResponseMessage();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 응답 읽기
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    // 응답에서 운세 설명 추출
                    String responseBody = response.toString();
                    return parseResponseContent(responseBody);
                }
            } else {
                System.out.println(responseMessage);
                throw new OpenAIServiceException(
                        "OpenAI API 호출 실패. HTTP 응답 코드: " + responseCode
                );
            }

        } catch (OpenAIServiceException e) {
            e.printStackTrace();
            throw e; // 이미 발생한 사용자 정의 예외 전달
        } catch (Exception e) {
            // 기타 예외 처리
            throw new OpenAIServiceException("OpenAI API 호출 중 알 수 없는 오류가 발생했습니다.", e);
        }
    }

    // JSON 특수 문자를 이스케이프하는 헬퍼 메서드
    private String escapeJson(String str) {
        return str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private String parseResponseContent(String jsonResponse) {
        try {
            JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
            JsonArray choicesArray = jsonObject.getAsJsonArray("choices");
            if (choicesArray != null && !choicesArray.isEmpty()) {
                JsonObject firstChoice = choicesArray.get(0).getAsJsonObject();
                JsonObject messageObject = firstChoice.getAsJsonObject("message");
                if (messageObject != null) {
                    String content = messageObject.get("content").getAsString();
                    return content.trim();
                }
            }
            throw new OpenAIServiceException("OpenAI API 응답에서 'content' 필드를 추출할 수 없습니다.");
        } catch (Exception e) {
            throw new OpenAIServiceException("OpenAI API 응답 파싱 중 오류가 발생했습니다.", e);
        }
    }
}
