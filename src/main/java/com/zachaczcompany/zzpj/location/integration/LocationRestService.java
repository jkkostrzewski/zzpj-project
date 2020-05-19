package com.zachaczcompany.zzpj.location.integration;

import com.zachaczcompany.zzpj.location.model.LocationResponse;
import com.zachaczcompany.zzpj.location.model.RoutingResponse;
import com.zachaczcompany.zzpj.location.model.SupermarketLocationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@PropertySource("classpath:location.yml")
@Component
public class LocationRestService {
    private final String key;
    private final String format;
    private final String restApiUrl;
    private final RestTemplate restTemplate;

    @Autowired
    public LocationRestService(@Value("${locationiq.rest-api.key}") String key,
                               @Value("${locationiq.rest-api.format}") String format,
                               @Value("${locationiq.rest-api.url}") String restApiUrl) {
        this.key = key;
        this.format = format;
        this.restApiUrl = restApiUrl;
        this.restTemplate = new RestTemplate();
    }

    public List<LocationResponse> getLocations(String query) {
        String httpUrl = restApiUrl + "search.php?key=" + key;
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(httpUrl)
                                                .queryParam("q", query)
                                                .queryParam("format", format)
                                                .build();
        ParameterizedTypeReference<List<LocationResponse>> type = new ParameterizedTypeReference<>() {};
        return restTemplate.exchange(uri.encode().toUri(), HttpMethod.GET, null, type)
                           .getBody();
    }

    public LocationResponse getLocation(double lat, double lon) {
        String httpUrl = restApiUrl + "reverse.php?key=" + key;
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(httpUrl)
                                                .queryParam("lat", lat)
                                                .queryParam("lon", lon)
                                                .queryParam("format", format)
                                                .build();
        return restTemplate.getForEntity(uri.encode().toUri(), LocationResponse.class)
                           .getBody();
    }

    public List<SupermarketLocationResponse> getNearbySupermarkets(double lat, double lon, double radius) {
        String httpUrl = restApiUrl + "nearby.php?key=" + key;
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(httpUrl)
                                                .queryParam("lat", lat)
                                                .queryParam("lon", lon)
                                                .queryParam("tag", "supermarket")
                                                .queryParam("radius", radius)
                                                .queryParam("format", format)
                                                .build();
        ParameterizedTypeReference<List<SupermarketLocationResponse>> type = new ParameterizedTypeReference<>() {};
        return restTemplate.exchange(uri.encode().toUri(), HttpMethod.GET, null, type)
                           .getBody();
    }

    private String buildCoordsString(double latStart, double lonStart, double latEnd, double lonEnd) {
        return latStart + "," + lonStart + ";" + latEnd + "," + lonEnd;
    }

    public RoutingResponse getFastestRoute(double latStart, double lonStart, double latEnd, double lonEnd) {
        String httpUrl = restApiUrl + "directions/driving/" + buildCoordsString(latStart, lonStart, latEnd, lonEnd) + "?key=" + key;
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(httpUrl)
                                                .build();
        return restTemplate.getForEntity(uri.encode().toUri(), RoutingResponse.class)
                           .getBody();
    }
}
