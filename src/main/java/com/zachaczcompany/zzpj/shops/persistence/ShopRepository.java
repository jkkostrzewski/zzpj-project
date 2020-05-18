package com.zachaczcompany.zzpj.shops.persistence;

import com.zachaczcompany.zzpj.commons.ReadOnlyRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "shops")
public interface ShopRepository extends ReadOnlyRepository<Shop, Long> {

}
