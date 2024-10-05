package com.tcmj.controller;

import com.tcmj.domain.Quality;
import com.tcmj.service.Impl.QualityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/qualitys")
public class QualityController {

    @Autowired
    private QualityServiceImpl qualityService;

    @GetMapping
    public List<Quality> getAllQualitys() {
        return qualityService.getAllNodes();
    }
}
