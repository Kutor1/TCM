package com.tcmj.controller;

import com.tcmj.service.Impl.HerbServiceImpl;
import com.tcmj.domain.Herb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/herbs")
public class HerbController {

    @Autowired
    private  HerbServiceImpl herbService;

    @GetMapping
    public List<Herb> getAllHerbs() {
        return herbService.getAllHerbs();
    }

}
