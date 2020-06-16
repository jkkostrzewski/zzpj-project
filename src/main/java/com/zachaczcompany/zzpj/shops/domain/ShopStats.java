package com.zachaczcompany.zzpj.shops.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zachaczcompany.zzpj.shops.exceptions.IllegalShopOperation;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PACKAGE;

@Entity
@Getter
@NoArgsConstructor(access = PACKAGE)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ShopStats {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private int maxCapacity;

    private int peopleInside;

    private int peopleInQueue;

    ShopStats(int maxCapacity, int peopleInside, int peopleInQueue) {
        this.maxCapacity = maxCapacity;
        this.peopleInside = peopleInside;
        this.peopleInQueue = peopleInQueue;
    }

    void updatePeopleInQueue(int delta) throws IllegalShopOperation {
        if (peopleInQueue + delta < 0) {
            throw new IllegalShopOperation("Queue length cannot be negative!");
        }
        peopleInQueue += delta;
    }

    void updatePeopleInside(int delta) throws IllegalShopOperation {
        if (peopleInside + delta < 0) {
            throw new IllegalShopOperation("Number of people inside cannot be negative!");
        }

        if (peopleInside + delta > maxCapacity) {
            throw new IllegalShopOperation("Inside cannot be that many people!");
        }
        peopleInside += delta;
    }
}
