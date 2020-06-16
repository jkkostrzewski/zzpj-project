package com.zachaczcompany.zzpj.reports;

import com.zachaczcompany.zzpj.shops.domain.Opinion;
import com.zachaczcompany.zzpj.shops.domain.OpinionService;
import com.zachaczcompany.zzpj.shops.domain.ShopSearch;
import com.zachaczcompany.zzpj.shops.domain.ShopSearchRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportsGenerator {
    ShopSearchRepository shopSearchRepository;
    OpinionService opinionService;

    ReportsGenerator(ShopSearchRepository shopSearchRepository, OpinionService opinionService) {
        this.shopSearchRepository = shopSearchRepository;
        this.opinionService = opinionService;
    }

    byte[] getSearchStatistics(ReportTypes reportTypes, Long shopId) {
        Optional<ShopSearch> shopSearchOptional = shopSearchRepository.findByShopId(shopId);
        ShopSearch shopSearch = shopSearchOptional.orElseThrow(IllegalArgumentException::new);
        return FilesGenerator.getShopSearchFileBytes(List.of(shopSearch), reportTypes.get());
    }

    byte[] getOpinions(ReportTypes reportTypes, Long shopId) {
        List<Opinion> opinions = opinionService.getByShopId(shopId);
        return FilesGenerator.getOpinionsFileBytes(opinions, reportTypes.get());
    }
}
