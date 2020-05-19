package com.zachaczcompany.zzpj.shops;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "shops")
interface ShopRepository extends JpaRepository<Shop, Long>, JpaSpecificationExecutor<Shop> {
}
