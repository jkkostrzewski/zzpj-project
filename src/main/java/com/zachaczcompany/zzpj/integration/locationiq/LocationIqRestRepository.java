package com.zachaczcompany.zzpj.integration.locationiq;

import com.zachaczcompany.zzpj.location.model.LocationDataResponse;
import com.zachaczcompany.zzpj.location.model.RoutingLocationDataResponse;
import com.zachaczcompany.zzpj.location.model.SupermarketLocationDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class LocationIqRestRepository {
    private final String key;
    private final String format;
    private final String restApiUrl;
    private final RestTemplate restTemplate;

    @Autowired
    public LocationIqRestRepository(@Value("${locationiq.rest-api.key}") String key,
                                    @Value("${locationiq.rest-api.format}") String format,
                                    @Value("${locationiq.rest-api.url}") String restApiUrl) {
        this.key = key;
        this.format = format;
        this.restApiUrl = restApiUrl;
        this.restTemplate = new RestTemplate();
    }

    public LocationDataResponse[] getLocationDataFromQuery(String query) {
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(restApiUrl + "search.php?key=" + key)
                .queryParam("q", query).queryParam("format", format).build();
        ResponseEntity<LocationDataResponse[]> responseEntity = restTemplate
                .getForEntity(uri.encode().toUri(), LocationDataResponse[].class);
        return responseEntity.getBody();
    }

    public LocationDataResponse getLocationDataFromCoordinates(double lat, double lon) {
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(restApiUrl + "reverse.php?key=" + key)
                .queryParam("lat", lat).queryParam("lon", lon).queryParam("format", format).build();
        ResponseEntity<LocationDataResponse> responseEntity = restTemplate
                .getForEntity(uri.encode().toUri(), LocationDataResponse.class);
        return responseEntity.getBody();
    }

    public SupermarketLocationDataResponse[] getLocationDataOfNearbySupermarkets(double lat, double lon, double radius) {
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(restApiUrl + "nearby.php?key=" + key)
                .queryParam("lat", lat).queryParam("lon", lon)
                .queryParam("tag", "supermarket").queryParam("radius", radius)
                .queryParam("format", format).build();
        ResponseEntity<SupermarketLocationDataResponse[]> responseEntity = restTemplate
                .getForEntity(uri.encode().toUri(), SupermarketLocationDataResponse[].class);
        return responseEntity.getBody();
    }

    private String buildCoordsString(double latStart, double lonStart, double latEnd, double lonEnd) {
        return latStart + "," + lonStart + ";" + latEnd + "," + lonEnd;
    }

    public RoutingLocationDataResponse getLocationDataOfFastestRouteBetweenCoordinates(double latStart, double lonStart,
                                                                                       double latEnd, double lonEnd) {
        UriComponents uri = UriComponentsBuilder
                .fromHttpUrl(restApiUrl + "directions/driving/" + buildCoordsString(latStart, lonStart, latEnd, lonEnd)
                        + "?key=" + key)
                .build();
        ResponseEntity<RoutingLocationDataResponse> responseEntity = restTemplate
                .getForEntity(uri.encode().toUri(), RoutingLocationDataResponse.class);
        return responseEntity.getBody();
    }
}
