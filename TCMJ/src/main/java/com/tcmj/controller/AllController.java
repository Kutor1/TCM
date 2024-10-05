package com.tcmj.controller;

import com.tcmj.domain.CustomNode;
import com.tcmj.domain.Herb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AllController {

    @Autowired
    private Neo4jClient neo4jClient;

    // 获取所有节点
    @GetMapping("/nodes")
    public List<?> getAllNodes(){
        return neo4jClient.query("MATCH (n) RETURN n")
                .fetchAs(CustomNode.class)
                .mappedBy((typeSystem, record) -> {
                    CustomNode customNode = new CustomNode();
                    customNode.setId(record.get("n").get("id").asString());
                    customNode.setName(record.get("n").get("name").asString());
                    return customNode;
                })
                .all()
                .stream()
                .collect(Collectors.toList());  // 转换为 List
    }

    // 获取所有节点链接
    @GetMapping("/links")
    public List<Map<String, String>> getAllLinks() {
        String query = """
            MATCH (n:处方用名)-[r]->(m)
            RETURN n.id AS source, m.id AS target
        """;

        return neo4jClient.query(query)
//                .in("neo4j") // 可选，指定数据库
                .fetch()
                .all()
                .stream()
                .map(record -> Map.of(
                        "source", record.get("source").toString(),
                        "target", record.get("target").toString()
                ))
                .collect(Collectors.toList());
    }

    // 查询点击节点的关系
    @GetMapping("/click/{id}")
    public List<Map<String, String>> onClick(@PathVariable String id) {
        // 接收前端的id，查询id对应节点的所有链接关系，并返回m节点的id和name属性
        // 定义两个查询
        String queryTarget = "MATCH (n {id: $id})-[r]->(m) RETURN m.id AS id, m.name AS name, m.group AS group, r.type AS relationship";
        String querySource = "MATCH (n {id: $id})<-[r]-(m) RETURN m.id AS id, m.name AS name, m.group AS group, r.type AS relationship";

        // 先执行 queryTarget 查询
        List<Map<String, String>> result = neo4jClient.query(queryTarget)
                .bind(id).to("id")  // 绑定id参数到Cypher查询
                .fetch()
                .all()
                .stream()
                .map(record -> Map.of(
                        "id", record.get("id").toString(),
                        "name", record.get("name") != null ? record.get("name").toString() : "",
                        "group", record.get("group").toString(),
                        "target", record.get("id").toString(),
                        "relationship", record.get("relationship").toString()
                ))
                .collect(Collectors.toList());

        // 如果 queryTarget 查询没有结果，再执行 querySource 查询
        if (result.isEmpty()) {
            result = neo4jClient.query(querySource)
                    .bind(id).to("id")  // 绑定id参数到Cypher查询
                    .fetch()
                    .all()
                    .stream()
                    .map(record -> Map.of(
                            "id", record.get("id").toString(),
                            "name", record.get("name") != null ? record.get("name").toString() : "",
                            "group", record.get("group").toString(),
                            "target", id,
                            "relationship", record.get("relationship").toString()
                    ))
                    .collect(Collectors.toList());
        }

        return result;
    }

    // 根据搜索内容查找节点
    @GetMapping("/search/{context}")
    public List<?> searchNode(@PathVariable String context) {

        // 使用CONTAINS来实现模糊查询
        String query = "MATCH (n) WHERE n.name CONTAINS $context RETURN n";

        return neo4jClient.query(query)
                .bind(context).to("context")
                .fetchAs(CustomNode.class)
                .mappedBy((typeSystem, record) -> {
                    CustomNode customNode = new CustomNode();
                    customNode.setId(record.get("n").get("id").asString());
                    customNode.setName(record.get("n").get("name").asString());
                    customNode.setGroup(record.get("n").get("group").asString());
                    return customNode;
                })
                .all()
                .stream()
                .collect(Collectors.toList());  // 转换为 List
    }

    // 根据id查找单个节点信息
    @GetMapping("/info/{id}")
    public List<?> getNodeInfo (@PathVariable String id) {
        String query = "MATCH (n {id: $id}) RETURN n";

        return neo4jClient.query(query)
                .bind(id).to("id")
                .fetchAs(CustomNode.class)
                .mappedBy((typeSystem, record) -> {
                    CustomNode customNode = new CustomNode();
                    customNode.setId(record.get("n").get("id").asString());
                    customNode.setName(record.get("n").get("name").asString());
                    customNode.setGroup(record.get("n").get("group").asString());
                    return customNode;
                })
                .all()
                .stream()
                .collect(Collectors.toList());  // 转换为 List
    }
}

