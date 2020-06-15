package com.zachaczcompany.zzpj.shops.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpinionRepository extends CrudRepository<Opinion, Long> {
    List<Opinion> findByShop(Shop shop);

    @Query(value = "from Opinion where shop.id=:shopId")
    List<Opinion> findByShopId(@Param("shopId") Long shopId);
}
