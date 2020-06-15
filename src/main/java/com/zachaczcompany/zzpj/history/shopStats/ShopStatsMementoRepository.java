package com.zachaczcompany.zzpj.history.shopStats;

import com.zachaczcompany.zzpj.shops.domain.Shop;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

interface ShopStatsMementoRepository extends CrudRepository<ShopStatsMemento, Long> {
    List<ShopStatsMemento> findByShopOrderByTimestamp(Shop shop);
}
