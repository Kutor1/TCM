package com.tcmj.repository;

import com.tcmj.domain.Source;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceRepository extends Neo4jRepository<Source, String> {
}
