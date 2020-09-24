package com.bank.agencies.external.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.bank.agencies.domain.AgencyGatewayResponse;
import com.bank.agencies.external.gateway.AgenciesGateway;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AgenciesGatewayImpl implements AgenciesGateway {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Autowired
    CacheManager cacheManager;

    @Value( "${agencies.service.base.url}" )
    private String baseUrl;

    ObjectMapper mapper = new ObjectMapper();

    @Override
    @Cacheable(cacheNames = "AgencyGatewayResponse")
    public List<AgencyGatewayResponse> findAllAgencies() {

    	List<AgencyGatewayResponse> ret = new ArrayList<>();
		URI apiURI = UriComponentsBuilder
                .fromUriString(baseUrl)
                .queryParam("$format", "json")
                .build().toUri();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(apiURI)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == HttpStatus.OK.value()) {
                JsonNode parent = mapper.readTree(response.body());
                String content = parent.get("value").toString();
                ret = Arrays.asList(mapper.readValue(content, AgencyGatewayResponse[].class));
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error when trying get all Agencies from API");
        }

        return ret;
    }

    @Override
    @Cacheable(cacheNames = "AgencyGatewayResponseByUF")
    public List<AgencyGatewayResponse> findAsyncAgenciesByUf() {
    	return new ArrayList<AgencyGatewayResponse>();
    }

    @Async
    CompletableFuture<List<AgencyGatewayResponse>> findAgenciesByUF(String uf) {

    	List<AgencyGatewayResponse> ret = new ArrayList<>();
		URI apiURI = UriComponentsBuilder
                .fromUriString(baseUrl)
                .queryParam("$format", "json")
                .queryParam("$filter", "UF eq '" + uf + "'")
                .build().toUri();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(apiURI)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == HttpStatus.OK.value()) {
                JsonNode parent = mapper.readTree(response.body());
                String content = parent.get("value").toString();
                ret = Arrays.asList(mapper.readValue(content, AgencyGatewayResponse[].class));
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error when trying get all Agencies from API");
        }

        return CompletableFuture.completedFuture(ret);
    }
   
    @Async
    public CompletableFuture<List<AgencyGatewayResponse>> findAsyncAgenciesByUf(String uf) {
    	List<AgencyGatewayResponse> ret = new ArrayList<>();
		URI apiURI = UriComponentsBuilder
                .fromUriString(baseUrl)
                .queryParam("$format", "json")
                .queryParam("$filter", "UF eq '" + uf + "'")
                .build().toUri();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(apiURI)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == HttpStatus.OK.value()) {
                JsonNode parent = mapper.readTree(response.body());
                String content = parent.get("value").toString();
                ret = Arrays.asList(mapper.readValue(content, AgencyGatewayResponse[].class));
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error when trying get all Agencies from API");
        }

        return CompletableFuture.completedFuture(ret);
    }

    @Scheduled(fixedRate = 6000)
    public void evictCache() {
    	cacheManager.getCacheNames()
        .parallelStream()
        .forEach(cacheName -> cacheManager.getCache(cacheName).clear());
    }

}
