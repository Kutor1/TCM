package com.tcmj.service.Impl;

import com.tcmj.domain.Source;
import com.tcmj.repository.SourceRepository;
import com.tcmj.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SourceServiceImpl implements NodeService {

    @Autowired
    private SourceRepository sourceRepository;

    @Override
    public List<Source> getAllNodes() {
        return sourceRepository.findAll();
    }
}
