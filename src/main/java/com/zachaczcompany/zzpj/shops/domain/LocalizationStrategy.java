package com.zachaczcompany.zzpj.shops.domain;

import com.zachaczcompany.zzpj.shops.ShopCreateDto;
import javassist.NotFoundException;

interface LocalizationStrategy {
    Localization getLocalization(ShopCreateDto dto) throws NotFoundException;
}