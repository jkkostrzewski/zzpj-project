package com.zachaczcompany.zzpj.shops;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpinionRepository extends CrudRepository<Opinion, Long> {
    List<Opinion> findByShopId(Long shopId);
    List<Opinion> findAll();
}
