package com.tcmj.controller;

import com.tcmj.service.Impl.EffectServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/effects")
public class EffectController {

    @Autowired
    private EffectServiceImpl effectService;

    @GetMapping
    public List<?> getAllEffects () {
        return effectService.getAllNodes();
    }
}
