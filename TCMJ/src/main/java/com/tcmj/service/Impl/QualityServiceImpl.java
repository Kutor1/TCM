package com.tcmj.service.Impl;

import com.tcmj.domain.Quality;
import com.tcmj.repository.QualityRepository;
import com.tcmj.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QualityServiceImpl implements NodeService {

    @Autowired
    private QualityRepository qualityRepository;

    @Override
    public List<Quality> getAllNodes() {

        return qualityRepository.findAll();
    }
}
