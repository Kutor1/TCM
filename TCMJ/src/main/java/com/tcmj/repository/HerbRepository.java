package com.tcmj.repository;

import com.tcmj.domain.Herb;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HerbRepository extends Neo4jRepository<Herb, String> {
}
