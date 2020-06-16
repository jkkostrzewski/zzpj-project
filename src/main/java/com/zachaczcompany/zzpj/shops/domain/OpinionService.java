package com.zachaczcompany.zzpj.shops.domain;

import com.zachaczcompany.zzpj.commons.response.Error;
import com.zachaczcompany.zzpj.commons.response.Response;
import com.zachaczcompany.zzpj.commons.response.Success;
import com.zachaczcompany.zzpj.shops.OpinionDto;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
class OpinionService {
    private final OpinionRepository opinionRepository;
    private final ShopFacade shopFacade;

    @Autowired
    public OpinionService(OpinionRepository opinionRepository, ShopFacade shopFacade) {
        this.opinionRepository = opinionRepository;
        this.shopFacade = shopFacade;
    }

    Response getById(Long id) {
        return Option.ofOptional(opinionRepository.findById(id)).toEither(Error.badRequest("OPINION_DOES_NOT_EXIST"))
                     .fold(Function.identity(), Success::ok);
    }

    Response addOpinion(OpinionDto opinion) {
        return Option.ofOptional(shopFacade.findShopById(opinion.getShopId()))
                     .map(shop -> new Opinion(shop, opinion.getRate(), opinion.getDescription()))
                     .map(opinionRepository::save)
                     .toEither(Error.badRequest("OPINION_NOT_ADDED_SHOP_DOES_NOT_EXIST"))
                     .fold(Function.identity(), Success::ok);
    }

    Response deleteById(Long id) {
        return Try.run(() -> opinionRepository.deleteById(id)).toEither(Error.badRequest("OPINION_DOES_NOT_EXIST"))
                  .fold(Function.identity(), Success::ok);
    }

    Response getByShopId(Long shopId) {
        return Option.ofOptional(shopFacade.findShopById(shopId)).map(opinionRepository::findByShop)
                     .toEither(Error.badRequest("SHOP_DOES_NOT_EXIST"))
                     .fold(Function.identity(), Success::ok);
    }
}
