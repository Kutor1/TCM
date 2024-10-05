package com.tcmj.repository;

import com.tcmj.domain.Effect;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EffectRepository extends Neo4jRepository<Effect, String> {
}
