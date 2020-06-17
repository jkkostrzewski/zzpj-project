package com.zachaczcompany.zzpj.history.shopStats;

import com.zachaczcompany.zzpj.commons.response.Error;
import com.zachaczcompany.zzpj.commons.response.Response;
import com.zachaczcompany.zzpj.commons.response.Success;
import com.zachaczcompany.zzpj.shops.domain.ShopFacade;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
class ShopStatsMementoService {
    private final ShopStatsMementoRepository repository;
    private final ShopFacade shopFacade;

    @Autowired
    ShopStatsMementoService(ShopStatsMementoRepository repository, ShopFacade shopFacade) {
        this.repository = repository;
        this.shopFacade = shopFacade;
    }

    @EventListener
    public void handleShopStatsChanged(ShopStatsChangedEvent event) {
        var memento = new ShopStatsMemento(event.shop);
        repository.save(memento);
    }

    Response getDiffsForShop(long shopId) {
        var diffs = shopFacade.findShopById(shopId)
                              .map(repository::findByShopOrderByTimestamp)
                              .map(this::getDiffs);

        return Option.ofOptional(diffs)
                     .toEither(Error.badRequest("SHOP_NOT_FOUND"))
                     .fold(Function.identity(), Success::ok);
    }

    List<ShopStatsDiff> getDiffs(List<ShopStatsMemento> mementos) {
        if (mementos.size() < 2) {
            return List.of();
        }

        var diffs = new ArrayList<ShopStatsDiff>(mementos.size() - 1);
        for (int i = 1; i < mementos.size(); i++) {
            var older = mementos.get(i - 1);
            var younger = mementos.get(i);
            diffs.add(ShopStatsDiff.of(older, younger));
        }

        return diffs;
    }
}
