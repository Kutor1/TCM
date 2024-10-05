package com.tcmj.controller;

import com.tcmj.service.Impl.SourceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sources")
public class SourceController {

    @Autowired
    private SourceServiceImpl sourceService;

    @GetMapping
    public List<?> getAllNodes() {
        return sourceService.getAllNodes();
    }
}
