package com.tcmj.repository;

import com.tcmj.domain.Method;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MethodRepository extends Neo4jRepository<Method, String> {
}
