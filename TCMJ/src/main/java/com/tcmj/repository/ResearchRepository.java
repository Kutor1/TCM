package com.tcmj.repository;

import com.tcmj.domain.Research;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResearchRepository extends Neo4jRepository<Research, String> {
}
