package com.zachaczcompany.zzpj.shops;

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

    @GetMapping("/all")
    public List<Opinion> getOpinions() {
        return opinionService.getOpinions();
    }

    @GetMapping("{id}")
    public Optional<Opinion> getById(@PathVariable("id") Long id) {
        return opinionService.getById(id);
    }

    @PostMapping("/add")
    public void addOpinion(@RequestBody Opinion opinion) {
        opinionService.addOpinion(opinion);
    }

    @DeleteMapping("/remove/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        opinionService.deleteById(id);
    }

    @GetMapping("/shopId/{shopId}")
    public List<Opinion> getByShopId(@PathVariable("shopId") Long shopId) {
        return opinionService.getByShopId(shopId);
    }

}
