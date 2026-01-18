package com.linkedIn.Connections_service.Repository;

import com.linkedIn.Connections_service.Entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface PersonRepo extends Neo4jRepository<Person,Long>{

    Optional<Person> getByName(String name);

    @Query("""
    MATCH (p:Person)-[:CONNECTED_TO]-(conn:Person)
    WHERE p.userId = $userId
    RETURN conn
    """)
    List<Person> getFirstDegreeConnections(Long userId);
}
