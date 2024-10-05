package com.tcmj.controller;

import com.tcmj.domain.Storage;
import com.tcmj.service.Impl.StorageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/storages")
public class StorageController {

    @Autowired
    private StorageServiceImpl storageService;

    @GetMapping
    public List<Storage> getAllStorages () {
        return storageService.getAllNodes();
    }
}
