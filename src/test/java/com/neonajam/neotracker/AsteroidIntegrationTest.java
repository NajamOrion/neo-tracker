package com.neonajam.neotracker;

import com.neonajam.neotracker.dto.nasa.NasaFeedResponse;
import com.neonajam.neotracker.exception.NasaApiException;
import com.neonajam.neotracker.service.NasaClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AsteroidIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NasaClient nasaClient;

    @Test
    void fetch_validRequest_returns200() throws Exception {
        NasaFeedResponse emptyFeed = new NasaFeedResponse();
        emptyFeed.setElementCount(0);
        emptyFeed.setNearEarthObjects(new HashMap<>());

        when(nasaClient.getFeed(anyString(), anyString())).thenReturn(emptyFeed);

        mockMvc.perform(post("/asteroids/fetch")
                .param("startDate", "2026-08-06")
                .param("endDate", "2026-08-08"))
                .andExpect(status().isOk());
    }

    @Test
    void fetch_rangeOver7Days_returns400() throws Exception {
        mockMvc.perform(post("/asteroids/fetch")
                        .param("startDate", "2026-08-01")
                        .param("endDate", "2026-08-20"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void fetch_whenNasaUnavailable_return503() throws Exception {
        when(nasaClient.getFeed(anyString(), anyString()))
                .thenThrow(new NasaApiException("Nasa API is down", new RuntimeException()));

        mockMvc.perform(post("/asteroids/fetch")
                        .param("startDate", "2026-08-06")
                        .param("endDate", "2026-08-08"))
                .andExpect(status().isServiceUnavailable());
    }
}
