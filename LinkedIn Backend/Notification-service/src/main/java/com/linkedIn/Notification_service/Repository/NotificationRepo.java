package com.linkedIn.Notification_service.Repository;

import com.linkedIn.Notification_service.Entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepo extends JpaRepository<Notification,Long> {
}
