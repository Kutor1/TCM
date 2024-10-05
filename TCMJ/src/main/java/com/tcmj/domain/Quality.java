package com.tcmj.domain;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node("质量要求")
@Data
public class Quality {
    @Id
    private String id;
    @Property("name")
    private String name;
    private String group;
}
