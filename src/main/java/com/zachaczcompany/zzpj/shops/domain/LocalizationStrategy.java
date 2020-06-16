package com.zachaczcompany.zzpj.shops.domain;

import com.zachaczcompany.zzpj.shops.ShopCreateDto;
import com.zachaczcompany.zzpj.shops.exceptions.LocationNotFoundException;

interface LocalizationStrategy {
    Localization getLocalization(ShopCreateDto dto) throws LocationNotFoundException;
}