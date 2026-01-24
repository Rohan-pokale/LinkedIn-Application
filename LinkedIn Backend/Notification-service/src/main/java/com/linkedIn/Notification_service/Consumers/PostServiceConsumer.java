package com.linkedIn.Notification_service.Consumers;

import com.linkedIn.Notification_service.Clients.ConnectionsClient;
import com.linkedIn.Notification_service.Dto.PersonDto;
import com.linkedIn.Notification_service.Entity.Notification;
import com.linkedIn.Notification_service.Repository.NotificationRepo;
import com.linkedIn.Notification_service.service.sendNotification;
import com.linkedIn.post_service.events.PostCreatedEvent;
import com.linkedIn.post_service.events.PostLikeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.pqc.jcajce.provider.Frodo;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceConsumer {

    private final ConnectionsClient connectionsClient;
    private final sendNotification sendNotification;

    @KafkaListener(topics = "post-created-topic")
    public void handlePostCreated(PostCreatedEvent postCreatedEvent){
        List<PersonDto> connections=connectionsClient.getFirstConnections(postCreatedEvent.getCreatorId());

        log.info("Sending Post creation notification to all connection:{}",connections);
        for(PersonDto personDto:connections){
            sendNotification.send(personDto.getUserId(),"Your Connection "+postCreatedEvent.getCreatorId()+" had created post" +
                    "check it out");
        }
    }

    @KafkaListener(topics = "post-like-topic")
    public void handlePostLikes(PostLikeEvent postLikeEvent){
        log.info("Sending Post Like notification to post CreatorId:{}",postLikeEvent.getCreatorId());
        String msg=String.format("Your post, %d had been liked by %d",postLikeEvent.getPostId(),
                postLikeEvent.getLikeByUserId());

        sendNotification.send(postLikeEvent.getCreatorId(),msg);
    }


}
