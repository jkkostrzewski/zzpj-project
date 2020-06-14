package com.zachaczcompany.zzpj.shops.domain;

import io.vavr.Function3;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.function.Supplier;

@Setter
@NoArgsConstructor
public class ShopFilterCriteria {
    private String address;
    private String name;
    private StockType stockType;
    private Boolean isOpen;
    private Boolean canEnter;
    private Integer maxQueueLength;
    private Integer maxCapacity;

    Specification<Shop> toSpecification() {
        return getAddress().and(getStockType()
                .and(getAddress()
                        .and(isOpen()
                                .and(canEnter()
                                        .and(getMaxCapacity()
                                                .and(getMaxQueueLength()
                                                        .and(getName())))))));
    }

    Specification<Shop> getName() {
        return getPredicate(name, this::getName);
    }

    private Predicate getName(Root<Shop> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        var nameLike = getStringLike(name);
        return cb.like(cb.lower(root.get(Shop_.name)), nameLike);
    }

    Specification<Shop> getAddress() {
        return getPredicate(address, this::addressContainsString);
    }

    private Predicate addressContainsString(Root<Shop> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        var addressLike = getStringLike(address);
        Path<Address> add = root.get(Shop_.address);
        Predicate city = cb.like(cb.lower(add.get(Address_.city)), addressLike);
        Predicate street = cb.like(cb.lower(add.get(Address_.street)), addressLike);
        return cb.or(city, street);
    }

    Specification<Shop> getStockType() {
        return getPredicate(stockType, this::stockTypeContainsString);
    }

    private Predicate stockTypeContainsString(Root<Shop> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        Path<ShopDetails> details = root.get(Shop_.details);
        return cb.equal(details.get(ShopDetails_.stockType), stockType);
    }

    Specification<Shop> isOpen() {
        return getPredicate(isOpen, this::isOpen);
    }

    private Predicate isOpen(Root<Shop> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek today = now.getDayOfWeek();
        LocalTime time = now.toLocalTime();

        Join<OpenHours, DailyOpenHours> openHours = root.join(Shop_.details)
                .join(ShopDetails_.openHours)
                .join(OpenHours_.openHours);

        Predicate dayOfWeek = cb.equal(openHours.get(DailyOpenHours_.dayOfWeek), today);
        Predicate beforeCloseTime = cb.greaterThan(openHours.get(DailyOpenHours_.openTo), time);
        Predicate afterOpenTime = cb.lessThan(openHours.get(DailyOpenHours_.openFrom), time);

        Predicate isShopOpen = cb.and(dayOfWeek, beforeCloseTime, afterOpenTime);
        Predicate isShopClosed = cb.and(dayOfWeek, cb.or(beforeCloseTime.not(), afterOpenTime.not()));

        return this.isOpen ? isShopOpen : isShopClosed;
    }

    Specification<Shop> canEnter() {
        return getPredicate(canEnter, this::canEnter);
    }

    private Predicate canEnter(Root<Shop> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        Path<ShopStats> stats = root.get(Shop_.shopStats);

        Path<Integer> maxCapacity = stats.get(ShopStats_.maxCapacity);
        Path<Integer> peopleInside = stats.get(ShopStats_.peopleInside);
        Path<Integer> peopleInQueue = stats.get(ShopStats_.peopleInQueue);
        Predicate canOneMorePersonEnter = cb.greaterThan(maxCapacity, cb.sum(peopleInside, peopleInQueue));
        return this.canEnter ? canOneMorePersonEnter : canOneMorePersonEnter.not();
    }

    Specification<Shop> getMaxQueueLength() {
        return getPredicate(maxQueueLength, this::getMaxQueueLength);
    }

    private Predicate getMaxQueueLength(Root<Shop> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        Path<ShopStats> shopDetails = root.get(Shop_.shopStats);
        return cb.lessThanOrEqualTo(shopDetails.get(ShopStats_.peopleInQueue), this.maxQueueLength);
    }

    Specification<Shop> getMaxCapacity() {
        return getPredicate(maxCapacity, this::getMaxCapacity);
    }

    private Predicate getMaxCapacity(Root<Shop> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        Path<ShopStats> shopDetails = root.get(Shop_.shopStats);
        return cb.lessThanOrEqualTo(shopDetails.get(ShopStats_.maxCapacity), this.maxCapacity);
    }

    private String getStringLike(String string) {
        return "%" + string.toLowerCase() + "%";
    }

    private Specification<Shop> getPredicate(Object param, Function3<Root<Shop>, CriteriaQuery<?>, CriteriaBuilder, Predicate> function3) {
        return (Root<Shop> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> trueOnNull(cb, param,
                () -> function3.apply(root, cq, cb));
    }

    private Predicate trueOnNull(CriteriaBuilder cb, Object param, Supplier<Predicate> supplier) {
        return Objects.isNull(param) ? cb.isTrue(cb.literal(true)) : supplier.get();
    }

    boolean addressIsNotEmpty() {
        return address != null;
    }

    boolean nameIsNotEmpty() {
        return name != null;
    }

    boolean stockTypeIsNotEmpty() {
        return stockType != null;
    }

    boolean isOpenIsUsed() {
        return isOpen != null;
    }

    boolean canEnterIsUsed() {
        return canEnter != null;
    }

    boolean maxQueueLengthIsNotEmpty() {
        return maxQueueLength != null;
    }

    boolean maxCapacityIsNotEmpty() {
        return maxCapacity != null;
    }
}
