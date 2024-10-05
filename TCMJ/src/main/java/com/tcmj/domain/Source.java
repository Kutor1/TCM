package com.tcmj.domain;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node("来源")
public class Source {
    @Id
    private String id;
    private String name;
    private String group;
}
