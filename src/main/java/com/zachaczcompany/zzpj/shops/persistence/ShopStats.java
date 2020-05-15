package com.zachaczcompany.zzpj.shops.persistence;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ShopStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
