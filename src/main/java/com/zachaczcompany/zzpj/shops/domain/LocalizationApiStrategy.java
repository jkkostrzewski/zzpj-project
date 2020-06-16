package com.zachaczcompany.zzpj.shops.domain;

import com.zachaczcompany.zzpj.location.integration.LocationRestService;
import com.zachaczcompany.zzpj.location.model.LocationResponse;
import com.zachaczcompany.zzpj.shops.ShopCreateDto;
import com.zachaczcompany.zzpj.shops.domain.location.ApartmentLocation;
import com.zachaczcompany.zzpj.shops.domain.location.BuildingLocation;
import com.zachaczcompany.zzpj.shops.domain.location.CityLocation;
import com.zachaczcompany.zzpj.shops.domain.location.NameLocation;
import com.zachaczcompany.zzpj.shops.domain.location.StreetLocation;
import com.zachaczcompany.zzpj.shops.domain.location.ZipCodeLocation;
import com.zachaczcompany.zzpj.shops.exceptions.LocationNotFoundException;

import static java.lang.Double.parseDouble;

class LocalizationApiStrategy implements LocalizationStrategy {
    private final LocationRestService locationRestService;

    LocalizationApiStrategy(LocationRestService locationRestService) {
        this.locationRestService = locationRestService;
    }

    @Override
    public Localization getLocalization(ShopCreateDto dto) throws LocationNotFoundException {
        NameLocation nameLocation = new NameLocation(dto.getName());
        StreetLocation streetLocation = new StreetLocation(nameLocation, dto.getStreet());
        BuildingLocation buildingLocation = new BuildingLocation(streetLocation, String.valueOf(dto.getBuilding()));
        ApartmentLocation apartmentLocation = new ApartmentLocation(buildingLocation, dto.getApartment());
        ZipCodeLocation zipCodeLocation = new ZipCodeLocation(apartmentLocation, dto.getZipCode());
        String query = new CityLocation(zipCodeLocation, dto.getCity()).getName();

        LocationResponse response = locationRestService.getLocations(query)
                                                       .stream().findFirst()
                                                       .orElseThrow(() -> new LocationNotFoundException("Unable to geocode"));
        return new Localization(parseDouble(response.getLat()), parseDouble(response.getLon()));
    }
}