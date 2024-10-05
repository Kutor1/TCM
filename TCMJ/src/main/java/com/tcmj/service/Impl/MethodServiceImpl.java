package com.tcmj.service.Impl;

import com.tcmj.domain.Method;
import com.tcmj.repository.MethodRepository;
import com.tcmj.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MethodServiceImpl implements NodeService {
    @Autowired
    private MethodRepository methodRepository;

    @Override
    public List<Method> getAllNodes() {
        return methodRepository.findAll();
    }
}
