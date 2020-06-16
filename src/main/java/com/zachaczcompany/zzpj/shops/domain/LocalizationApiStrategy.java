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
import javassist.NotFoundException;

import static java.lang.Double.parseDouble;

class LocalizationApiStrategy implements LocalizationStrategy {
    private final LocationRestService locationRestService;

    LocalizationApiStrategy(LocationRestService locationRestService) {
        this.locationRestService = locationRestService;
    }

    @Override
    public Localization getLocalization(ShopCreateDto dto) throws NotFoundException {
        String query = new CityLocation(new ZipCodeLocation(new ApartmentLocation(new BuildingLocation(new StreetLocation(new NameLocation(dto
                .getName()), dto.getStreet()), String.valueOf(dto.getBuilding())), dto.getApartment()), dto
                .getZipCode()), dto
                .getCity()).getName();
        LocationResponse response = locationRestService.getLocations(query)
                                                       .stream().findFirst()
                                                       .orElseThrow(() -> new NotFoundException("Unable to geocode"));
        return new Localization(parseDouble(response.getLat()), parseDouble(response.getLon()));
    }
}