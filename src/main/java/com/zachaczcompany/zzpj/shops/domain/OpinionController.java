package com.zachaczcompany.zzpj.shops.domain;

import com.zachaczcompany.zzpj.shops.OpinionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @Operation(summary = "Get opinion by id", description = "Requires id of an opinion as path variable")
    @GetMapping("{id}")
    public Optional<Opinion> getById(@PathVariable("id") Long id) {
        return opinionService.getById(id);
    }

    @Operation(summary = "Add new opinion", description = "Requires shop id, rating and description in body")
    @PostMapping
    public void addOpinion(@RequestBody OpinionDto opinion) {
        opinionService.addOpinion(opinion);
    }

    @Operation(summary = "Delete an opinion", description = "Requires id of an opinion as path variable")
    @DeleteMapping("{id}")
    public void deleteById(@PathVariable("id") Long id) {
        opinionService.deleteById(id);
    }

    @Operation(summary = "Get all opinions for given shop", description = "Requires shop id in param")
    @GetMapping
    public List<Opinion> getByShopId(@RequestParam Long shopId) {
        return opinionService.getByShopId(shopId);
    }

}
