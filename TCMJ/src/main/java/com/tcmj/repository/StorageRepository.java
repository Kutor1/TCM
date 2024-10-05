package com.tcmj.repository;

import com.tcmj.domain.Storage;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageRepository extends Neo4jRepository<Storage, String> {
}
