package com.tcmj.repository;

import com.tcmj.domain.Quality;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QualityRepository extends Neo4jRepository<Quality, String> {
}
