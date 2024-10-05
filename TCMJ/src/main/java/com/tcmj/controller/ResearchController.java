package com.tcmj.controller;

import com.tcmj.domain.Research;
import com.tcmj.service.Impl.ResearchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/researchs")
public class ResearchController {

    @Autowired
    private ResearchServiceImpl researchService;

    @GetMapping
    public List<Research> getAllResearchs () {
        return researchService.getAllNodes();
    }
}
