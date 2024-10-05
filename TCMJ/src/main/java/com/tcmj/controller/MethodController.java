package com.tcmj.controller;

import com.tcmj.domain.Method;
import com.tcmj.service.Impl.MethodServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/methods")
public class MethodController {

    @Autowired
    private MethodServiceImpl methodService;

    @GetMapping
    public List<Method>  getAllMethods() {
        return methodService.getAllNodes();
    }
}
