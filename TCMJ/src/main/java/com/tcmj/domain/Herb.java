package com.tcmj.domain;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node("处方用名")
@Data
public class Herb {
    @Id
    private String id;
    @Property("name")
    private String name;
    private String group;
}
