package com.zachaczcompany.zzpj.shops.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopSearchRepository extends JpaRepository<ShopSearch, Long> {
    Optional<ShopSearch> findByShopId(Long id);
}
