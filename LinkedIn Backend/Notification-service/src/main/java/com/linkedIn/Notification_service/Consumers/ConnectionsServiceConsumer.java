package com.linkedIn.Notification_service.Consumers;

import com.linkedIn.Connections_service.event.acceptConnectionRequestEvent;
import com.linkedIn.Connections_service.event.sendConnectionRequestEvent;
import com.linkedIn.Notification_service.service.sendNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConnectionsServiceConsumer {

    private final sendNotification sendNotification;

    @KafkaListener(topics = "send-connection-request-topic")
    public void handleSendConnectionRequestMsg(sendConnectionRequestEvent sendConnectionRequestEvent){

        String msg=String.format("you have connection request from user " +
                "with userId:%d",sendConnectionRequestEvent.getSenderId());

        log.info(msg);

        sendNotification.send(sendConnectionRequestEvent.getReceiverId(),msg);
    }


    @KafkaListener(topics = "accept-connection-request-topic")
    public void handleAcceptConnectionRequestMsg(acceptConnectionRequestEvent acceptConnectionRequestEvent){
        String msg=String.format("your connection request accepted " +
                "by user with user id:%d",acceptConnectionRequestEvent.getReceiverId());

        log.info(msg);

        sendNotification.send(acceptConnectionRequestEvent.getSenderId(),msg);
    }
}


