package com.tcmj.service.Impl;

import com.tcmj.domain.Research;
import com.tcmj.repository.ResearchRepository;
import com.tcmj.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResearchServiceImpl implements NodeService {
    @Autowired
    private ResearchRepository researchRepository;

    @Override
    public List<Research> getAllNodes() {
        return researchRepository.findAll();
    }
}
