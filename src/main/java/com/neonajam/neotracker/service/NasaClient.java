package com.neonajam.neotracker.service;

import com.neonajam.neotracker.dto.nasa.NasaFeedResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class NasaClient {

    private final RestClient restClient;
    private final String apiKey;

    public NasaClient(@Value("${nasa.api.base-url}") String baseUrl,
                      @Value("${nasa.api.key}") String apiKey) {
        this.restClient = RestClient.create(baseUrl);
        this.apiKey = apiKey;
    }

    public NasaFeedResponse getFeed(String startDate, String endDate) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/feed")
                        .queryParam("start_date", startDate)
                        .queryParam("end_date", endDate)
                        .queryParam("api_key", apiKey)
                        .build())
                .retrieve()
                .body(NasaFeedResponse.class);
    }
}
