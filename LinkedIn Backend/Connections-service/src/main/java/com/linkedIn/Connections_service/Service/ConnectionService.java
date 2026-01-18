package com.linkedIn.Connections_service.Service;

import com.linkedIn.Connections_service.Entity.Person;
import com.linkedIn.Connections_service.Repository.PersonRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ConnectionService {

    private final PersonRepo personRepo;

    public List<Person> getFirstDegreeConnections(Long userId){
        log.info("getting first degree connections by id: {}",userId);
        return personRepo.getFirstDegreeConnections(userId);
    }
}
