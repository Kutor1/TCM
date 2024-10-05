package com.tcmj.service.Impl;

import com.tcmj.domain.Effect;
import com.tcmj.repository.EffectRepository;
import com.tcmj.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EffectServiceImpl implements NodeService {

    @Autowired
    private EffectRepository effectRepository;

    // 查询所有effect节点
    @Override
    public List<Effect> getAllNodes() {
        return effectRepository.findAll();
    }
}
