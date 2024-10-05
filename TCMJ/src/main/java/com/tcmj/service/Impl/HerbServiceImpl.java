package com.tcmj.service.Impl;

import com.tcmj.domain.Herb;
import com.tcmj.repository.HerbRepository;
import com.tcmj.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HerbServiceImpl implements NodeService {

    @Autowired
    private Neo4jClient neo4jClient;

    @Autowired
    private HerbRepository herbRepository;

    public List<Herb> getAllHerbs() {
        return herbRepository.findAll();
    }

    @Override
    public List<Herb> getAllNodes() {
        // 使用 Neo4jClient 查询所有 Herb 节点，并将结果转换为 List
        return neo4jClient.query("MATCH (n:处方用名) RETURN n")
                .fetchAs(Herb.class)
                .mappedBy((typeSystem, record) -> {
                    Herb herb = new Herb();
                    herb.setId(record.get("n").get("id").asString());
                    herb.setName(record.get("n").get("name").asString());
                    return herb;
                })
                .all()
                .stream()
                .collect(Collectors.toList());  // 转换为 List
    }

//    @Override
//    public List<Edge> getAllEdges() {
//        // 查询 Herb 节点之间的关系，并将结果转换为 List
//        return neo4jClient.query("MATCH (n:处方用名)-[r]->(m:处方用名) RETURN n.id AS source, m.id AS target, type(r) AS relationship")
//                .fetchAs(Edge.class)
//                .mappedBy((typeSystem, record) -> {
//                    Edge edge = new Edge();
//                    edge.setSource(record.get("source").asString());
//                    edge.setTarget(record.get("target").asString());
//                    edge.setRelationship(record.get("relationship").asString());
//                    return edge;
//                })
//                .all()
//                .stream()
//                .collect(Collectors.toList());  // 转换为 List
//    }
}
