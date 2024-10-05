package com.tcmj.domain;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Relationship;

// 关系节点
@Data
public class Link {
    private String source;
    private String target;
    private String group;
}
