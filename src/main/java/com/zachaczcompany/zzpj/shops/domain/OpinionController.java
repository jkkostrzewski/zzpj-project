package com.zachaczcompany.zzpj.shops.domain;

import com.zachaczcompany.zzpj.shops.OpinionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/opinion")
class OpinionController {
    private final OpinionService opinionService;

    @Autowired
    public OpinionController(OpinionService opinionService) {
        this.opinionService = opinionService;
    }

    @GetMapping("{id}")
    public Optional<Opinion> getById(@PathVariable("id") Long id) {
        return opinionService.getById(id);
    }

    @PostMapping
    public void addOpinion(@RequestBody OpinionDto opinion) {
        opinionService.addOpinion(opinion);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable("id") Long id) {
        opinionService.deleteById(id);
    }

    @GetMapping
    public List<Opinion> getByShopId(@RequestParam Long shopId) {
        return opinionService.getByShopId(shopId);
    }

}
