package com.zachaczcompany.zzpj.shops;

import com.zachaczcompany.zzpj.shops.persistence.Shop;
import com.zachaczcompany.zzpj.shops.persistence.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
class ShopService {
    private final ShopRepository shopRepository;

    @Autowired
    public ShopService(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    Collection<Shop> filter(Specification<Shop> specification) {
        return List.of();
    }
}
