package com.linkedIn.Connections_service.Controller;


import com.linkedIn.Connections_service.Auth.UserContextHolder;
import com.linkedIn.Connections_service.Entity.Person;
import com.linkedIn.Connections_service.Service.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class ConnectionsController {

    private final ConnectionService connectionService;

    @GetMapping("/first-degree")
    public ResponseEntity<List<Person>> getFirstConnections(){
        return ResponseEntity.ok(connectionService.getFirstDegreeConnections());
    }
}
