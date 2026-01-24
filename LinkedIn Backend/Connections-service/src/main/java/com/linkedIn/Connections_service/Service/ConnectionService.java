package com.linkedIn.Connections_service.Service;

import com.linkedIn.Connections_service.Auth.UserContextHolder;
import com.linkedIn.Connections_service.Entity.Person;
import com.linkedIn.Connections_service.Repository.PersonRepo;
import com.linkedIn.Connections_service.event.acceptConnectionRequestEvent;
import com.linkedIn.Connections_service.event.sendConnectionRequestEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ConnectionService {

    private final PersonRepo personRepo;
    private final KafkaTemplate<Long, sendConnectionRequestEvent> sendConnectionRequestKafkaTemplate;
    private final KafkaTemplate<Long, acceptConnectionRequestEvent> acceptConnectionRequestKafkaTemplate;


    // 1. getFirstDegreeConnections

    public List<Person> getFirstDegreeConnections(){
        Long userId= UserContextHolder.getCurrentUserId();
        log.info("getting first degree connections by id: {}",userId);
        return personRepo.getFirstDegreeConnections(userId);
    }


    // 2. sendConnectionRequest

    public Boolean sendConnectionRequest(Long receiverId) {
        Long senderId=UserContextHolder.getCurrentUserId();
        log.info("sending connection request from sender: {} to receiver: {} ",senderId,receiverId);

        if(senderId.equals(receiverId)) {
            throw new RuntimeException("Cannot send self request");
        }

        boolean isAlreadyConnected= personRepo.isAlreadyConnected(senderId,receiverId);
        if(isAlreadyConnected){
            log.error("You both are already friends, cannot send request");
            throw new RuntimeException("You both are already friends, cannot send request.");
        }

        boolean isConnectionRequestSent=personRepo.isConnectionRequestSent(senderId,receiverId);
        if(isConnectionRequestSent){
            log.error("Connection Request is Already send, cannot send again.");
            throw new RuntimeException("Connection Request is Already send, cannot send again.");
        }

        personRepo.sendConnectionRequest(senderId,receiverId);

        sendConnectionRequestEvent sendConnectionRequestMsg = sendConnectionRequestEvent
                .builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();
        sendConnectionRequestKafkaTemplate.send("send-connection-request-topic",sendConnectionRequestMsg);

        log.info("send connection request successfully.");
        return true;
    }


    // 3. acceptConnectionRequest

    public Boolean acceptConnectionRequest(Long senderId) {
        Long receiverId=UserContextHolder.getCurrentUserId();
        log.info("Accepting connection Request.....");

        boolean isAlreadyConnected= personRepo.isAlreadyConnected(senderId,receiverId);
        if(isAlreadyConnected){
            log.error("You both are already friends, cannot send request.");
            throw new RuntimeException("You both are already friends, cannot send request.");
        }

        boolean isConnectionRequestReceived=personRepo.isConnectionRequestSent(senderId,receiverId);
        if(!isConnectionRequestReceived){
            log.error("request not received for accept,cannot accept");
            throw new RuntimeException("request not received for accept,cannot accept");
        }

        personRepo.acceptConnectionRequest(senderId,receiverId);

        acceptConnectionRequestEvent acceptConnectionRequestMsg = acceptConnectionRequestEvent
                .builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();
        acceptConnectionRequestKafkaTemplate.send("accept-connection-request-topic",acceptConnectionRequestMsg);


        log.info("Connection Request accepted successfully.");
        return true;

    }



    // 4. rejectConnectionRequest

    public Boolean rejectConnectionRequest(Long senderId) {
        Long receiverId=UserContextHolder.getCurrentUserId();
        log.info("Rejecting connection Request.....");

        boolean isAlreadyConnected= personRepo.isAlreadyConnected(senderId,receiverId);
        if(isAlreadyConnected){
            log.error("You both are already friends, no request is there.");
            throw new RuntimeException("You both are already friends, no request is there");
        }

        boolean isConnectionRequestReceived=personRepo.isConnectionRequestSent(senderId,receiverId);
        if(!isConnectionRequestReceived){
            log.error("request not received for reject,cannot reject");
            throw new RuntimeException("request not received for reject,cannot reject");
        }

        personRepo.rejectConnectionRequest(senderId,receiverId);
        log.info("Connection Request rejected successfully.");

        return true;

    }
}
