package com.zachaczcompany.zzpj.shops.domain;

import com.zachaczcompany.zzpj.shops.OpinionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class OpinionService {
    private final OpinionRepository opinionRepository;
    private final ShopFacade shopFacade;

    @Autowired
    public OpinionService(OpinionRepository opinionRepository, ShopFacade shopFacade) {
        this.opinionRepository = opinionRepository;
        this.shopFacade = shopFacade;
    }

    Optional<Opinion> getById(Long id) {
        return opinionRepository.findById(id);
    }

    void addOpinion(OpinionDto opinion) {
        shopFacade.findShopById(opinion.getShopId())
                  .ifPresent(shop -> opinionRepository
                          .save(new Opinion(shop, opinion.getRate(), opinion.getDescription())));
    }

    void deleteById(Long id) {
        opinionRepository.deleteById(id);
    }

    public List<Opinion> getByShopId(Long shopId) {
        return shopFacade.findShopById(shopId).map(opinionRepository::findByShop).orElse(Collections.emptyList());
    }
}
