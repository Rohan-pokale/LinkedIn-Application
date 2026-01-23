package com.linkedIn.Notification_service.Clients;


import com.linkedIn.Notification_service.Dto.PersonDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "Connections-service" , path = "/connections")
public interface ConnectionsClient {

    @GetMapping("/core/first-degree")
    List<PersonDto>getFirstConnections(@RequestHeader Long userId);
}
