package com.zachaczcompany.zzpj.shops;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
class Opinion {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long shopId;

    @Column(nullable = false)
    private Integer rate;

    @Column(nullable = false)
    private String description;

    Opinion(Long shopId, Integer rate, String description) {
        this.shopId = shopId;
        this.rate = rate;
        this.description = description;
    }
}
