package com.zachaczcompany.zzpj.reports;

import com.zachaczcompany.zzpj.shops.domain.Opinion;
import com.zachaczcompany.zzpj.shops.domain.OpinionService;
import com.zachaczcompany.zzpj.shops.domain.ShopSearch;
import com.zachaczcompany.zzpj.shops.domain.ShopSearchRepository;
import io.vavr.control.Either;
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

    Either<String, byte[]> getSearchStatistics(ReportTypes reportTypes, Long shopId) {
        try {
            ShopSearch shopSearch = getShopSearchData(shopId);
            byte[] shopSearchFileBytes = FilesGenerator.getShopSearchFileBytes(List.of(shopSearch), reportTypes.get());
            return Either.right(shopSearchFileBytes);
        } catch (NoDataException e) {
            return Either.left(e.getMessage());
        }
    }

    private ShopSearch getShopSearchData(Long shopId) throws NoDataException {
        Optional<ShopSearch> shopSearchOptional = shopSearchRepository.findByShopId(shopId);
        return shopSearchOptional.orElseThrow(NoDataException::new);
    }

    Either<String, byte[]> getOpinions(ReportTypes reportTypes, Long shopId) {
        try {
            byte[] opinionsFileBytes = FilesGenerator.getOpinionsFileBytes(getOpinionsData(shopId), reportTypes.get());
            return Either.right(opinionsFileBytes);
        } catch (NoDataException e) {
            return Either.left(e.getMessage());
        }
    }

    private List<Opinion> getOpinionsData(Long shopId) throws NoDataException {
        List<Opinion> opinions = opinionService.getByShopId(shopId);
        if (opinions.size() < 1) {
            throw new NoDataException();
        }
        return opinions;
    }

    private static class NoDataException extends Exception {
    }
}
