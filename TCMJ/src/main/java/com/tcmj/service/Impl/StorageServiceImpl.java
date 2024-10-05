package com.tcmj.service.Impl;

import com.tcmj.domain.Storage;
import com.tcmj.repository.StorageRepository;
import com.tcmj.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StorageServiceImpl implements NodeService {

    @Autowired
    private StorageRepository storageRepository;

    @Override
    public List<Storage> getAllNodes() {
        return storageRepository.findAll();
    }
}
