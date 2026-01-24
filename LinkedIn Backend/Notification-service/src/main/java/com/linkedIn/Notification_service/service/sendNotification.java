package com.linkedIn.Notification_service.service;

import com.linkedIn.Notification_service.Entity.Notification;
import com.linkedIn.Notification_service.Repository.NotificationRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class sendNotification {

    private final NotificationRepo notificationRepo;

    public void send(Long userId,String msg){
        Notification notification=new Notification();
        notification.setUserId(userId);
        notification.setMessage(msg);

        notificationRepo.save(notification);

        log.info("for user id:{} saved msg-->{}",userId,msg);

    }
}
