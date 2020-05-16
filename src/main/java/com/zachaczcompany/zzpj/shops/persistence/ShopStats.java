package com.zachaczcompany.zzpj.shops.persistence;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Getter
@NoArgsConstructor(access = PRIVATE)
public class ShopStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
