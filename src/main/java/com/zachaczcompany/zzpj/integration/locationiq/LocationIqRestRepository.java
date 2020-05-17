package com.zachaczcompany.zzpj.integration.locationiq;

import com.zachaczcompany.zzpj.location.model.LocationDataResponse;
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

    public LocationDataResponse[] getLocationDataInfoFromQuery(String query) {
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(restApiUrl + "search.php?key=" + key).queryParam("q", query).queryParam("format", format).build();
        ResponseEntity<LocationDataResponse[]> forEntity = restTemplate.getForEntity(uri.encode().toUri(), LocationDataResponse[].class);
        return forEntity.getBody();
    }

    public LocationDataResponse getLocationDataInfoFromCoordinates(String lat, String lon) {
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(restApiUrl + "reverse.php?key=" + key).queryParam("lat", lat).queryParam("lon", lon).queryParam("format", format).build();
        ResponseEntity<LocationDataResponse> forEntity = restTemplate.getForEntity(uri.encode().toUri(), LocationDataResponse.class);
        return forEntity.getBody();
    }
}
