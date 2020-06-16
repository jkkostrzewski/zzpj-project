package com.zachaczcompany.zzpj.shops.domain;

import com.zachaczcompany.zzpj.shops.ShopCreateDto;

class LocalizationDefaultStrategy implements LocalizationStrategy {
    @Override
    public Localization getLocalization(ShopCreateDto dto) {
        return dto.getLocalization();
    }
}