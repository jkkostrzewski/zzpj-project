package com.zachaczcompany.zzpj.shops.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@Getter
@Entity
public class ShopSearch {
    @Id
    private Long shopId;
    private long address = 0L;
    private long name = 0L;
    private long stockType = 0L;
    private long isOpen = 0L;
    private long canEnter = 0L;
    private long maxQueueLength = 0L;
    private long maxCapacity = 0L;

    public ShopSearch(Long shopId) {    //TODO paluszkiewiczB dodać to wywołanie konstruktora podczas tworzenia nowego sklepu
        this.shopId = shopId;
    }

    public void incrementValues(ShopFilterCriteria criteria) {
        if (criteria.addressIsNotEmpty()) {
            this.address++;
        }
        if (criteria.nameIsNotEmpty()) {
            this.name++;
        }
        if (criteria.stockTypeIsNotEmpty()) {
            this.stockType++;
        }
        if (criteria.isOpenIsUsed()) {
            this.isOpen++;
        }
        if (criteria.canEnterIsUsed()) {
            this.canEnter++;
        }
        if (criteria.maxQueueLengthIsNotEmpty()) {
            this.maxQueueLength++;
        }
        if (criteria.maxCapacityIsNotEmpty()) {
            this.maxCapacity++;
        }
    }
}
