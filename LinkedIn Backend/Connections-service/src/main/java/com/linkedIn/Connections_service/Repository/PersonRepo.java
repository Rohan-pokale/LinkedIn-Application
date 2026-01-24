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

    //for checking connection Request send or not ?
    @Query("""
    MATCH (s:Person {userId: $senderId})-[:CONNECTION_REQUEST]->(r:Person {userId: $receiverId})
    RETURN COUNT(*) > 0
    """)
    boolean isConnectionRequestSent(Long senderId, Long receiverId);
    //Checks “Has A already sent a request to B?”
    //This is a one-direction relationship checking
    //Status: Requested

    //checking second user accepts the request
    @Query("""
    MATCH (p1:Person {userId: $senderId})-[:CONNECTED]-(p2:Person {userId: $receiverId})
    RETURN COUNT(*) > 0
    """)
    boolean isAlreadyConnected(Long senderId, Long receiverId);
    //This is a bidirectional (mutual) relationship
    //Status: connected / friends

    @Query("""
    MATCH (s:Person {userId: $senderId})
    MATCH (r:Person {userId: $receiverId})
    WHERE NOT (s)-[:CONNECTED]-(r)
      AND NOT (s)-[:CONNECTION_REQUEST]->(r)
    MERGE (s)-[:CONNECTION_REQUEST]->(r)
    """)
    void sendConnectionRequest(Long senderId, Long receiverId);
    //make one direction connection A to B

    @Query("""
    MATCH (s:Person {userId: $senderId})-[req:CONNECTION_REQUEST]->(r:Person {userId: $receiverId})
    DELETE req
    MERGE (s)-[:CONNECTED]-(r)
    """)
    void acceptConnectionRequest(Long senderId, Long receiverId);
    //Bidirectional relationship between A and B (connection established)

    @Query("""
    MATCH (s:Person {userId: $senderId})-[req:CONNECTION_REQUEST]->(r:Person {userId: $receiverId})
    DELETE req
    """)
    void rejectConnectionRequest(Long senderId, Long receiverId);
    //before reject there was one direction relation,but now there is no relation






}
