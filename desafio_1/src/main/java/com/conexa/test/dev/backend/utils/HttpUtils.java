package com.conexa.test.dev.backend.utils;

import com.conexa.test.dev.backend.exception.SWApiException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class HttpUtils {

    @Autowired
    private HttpClient httpClient;

    @Autowired
    private ObjectMapper objectMapper;

    public <T> T performRequest(HttpRequest httpRequest, TypeReference<T> typeReference) throws SWApiException {
        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(), typeReference);
        } catch (IOException e) {
            throw new SWApiException(e.getMessage(), e, HttpStatus.BAD_GATEWAY);
        } catch (InterruptedException e) {
            throw new SWApiException(e.getMessage(), e, HttpStatus.GATEWAY_TIMEOUT);
        }
    }
}
