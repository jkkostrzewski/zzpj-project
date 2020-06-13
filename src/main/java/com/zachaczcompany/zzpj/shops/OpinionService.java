package com.zachaczcompany.zzpj.shops;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
class OpinionService {
    private  final OpinionRepository opinionRepository;
    private final ShopFacade shopFacade;

    @Autowired
    public OpinionService(OpinionRepository opinionRepository, ShopFacade shopFacade) {
        this.opinionRepository = opinionRepository;
        this.shopFacade = shopFacade;
    }

    List<Opinion> getOpinions() {
        return opinionRepository.findAll();
    }

    Optional<Opinion> getById(Long id) {
        return opinionRepository.findById(id);
    }

    void addOpinion(Opinion opinion) {
        shopFacade.findShopById(opinion.getShopId()).addOpinion(opinion);
        opinionRepository.save(opinion);
    }

    void deleteById(Long id) {
        opinionRepository.findById(id).ifPresent(opinion -> shopFacade.findShopById(opinion.getShopId())
                                                                      .removeOpinion(opinion));
        opinionRepository.deleteById(id);
    }

    List<Opinion> getByShopId(Long shopId) {
        return opinionRepository.findByShopId(shopId);
    }
}
