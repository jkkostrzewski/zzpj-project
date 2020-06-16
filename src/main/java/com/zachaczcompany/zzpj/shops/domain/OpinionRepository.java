package com.zachaczcompany.zzpj.shops.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpinionRepository extends CrudRepository<Opinion, Long> {
    List<Opinion> findByShop(Shop shop);
}
