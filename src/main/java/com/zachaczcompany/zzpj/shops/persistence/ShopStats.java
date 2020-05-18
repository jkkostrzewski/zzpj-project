package com.zachaczcompany.zzpj.shops.persistence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    public ShopStats(int maxCapacity, int peopleInside, int peopleInQueue) {
        this.maxCapacity = maxCapacity;
        this.peopleInside = peopleInside;
        this.peopleInQueue = peopleInQueue;
    }

    public void moveInsideFromQueue(int count) {
        if (peopleInQueue > count) {
            throw new IllegalArgumentException("Cannot move more people than queue size!");
        }

        this.peopleInQueue -= count;
        this.peopleInside += count;
    }

    public void addToQueue(int count) {
        peopleInQueue += count;
    }
}
