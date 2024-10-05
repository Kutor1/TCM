package com.tcmj.domain;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

// 自定义节点
@Node
@Data
public class CustomNode {

    @Id
    private String id;
    private String name;
    private String group;
}
