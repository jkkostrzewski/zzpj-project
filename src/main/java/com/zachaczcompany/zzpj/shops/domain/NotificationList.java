package com.zachaczcompany.zzpj.shops.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class NotificationList {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long shopId;

    private String email;

    private Timestamp notificationEnd;
}
