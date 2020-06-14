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
    private long address;
    private long name;
    private long stockType;
    private long isOpen;
    private long canEnter;
    private long maxQueueLength;
    private long maxCapacity;

    public ShopSearch(Long shopId) {    //TODO paluszkiewiczB dodać to wywołanie konstruktora podczas tworzenia nowego sklepu
        this.shopId = shopId;
        this.address = 0;
        this.name = 0;
        this.stockType = 0;
        this.isOpen = 0;
        this.canEnter = 0;
        this.maxQueueLength = 0;
        this.maxCapacity = 0;
    }

    public void incrementAddress() {
        this.address++;
    }

    public void incrementName() {
        this.name++;
    }

    public void incrementStockType() {
        this.stockType++;
    }

    public void incrementIsOpen() {
        this.isOpen++;
    }

    public void incrementCanEnter() {
        this.canEnter++;
    }

    public void incrementMaxQueueLength() {
        this.maxQueueLength++;
    }

    public void incrementMaxCapacity() {
        this.maxCapacity++;
    }
}
