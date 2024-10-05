package com.tcmj.domain;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node("炮制研究")
@Data
public class Research {
    @Id
    private String id;
    @Property("name")
    private String name;
    private String group;
}
