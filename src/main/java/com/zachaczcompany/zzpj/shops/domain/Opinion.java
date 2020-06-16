package com.zachaczcompany.zzpj.shops.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
public class Opinion {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToOne
    private Shop shop;

    @Column(nullable = false)
    private Integer rate;

    @Column(nullable = false)
    private String description;

    Opinion(Shop shop, Integer rate, String description) {
        this.shop = shop;
        this.rate = rate;
        this.description = description;
    }
}
