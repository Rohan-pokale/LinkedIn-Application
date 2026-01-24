package com.linkedIn.Connections_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class sendConnectionRequestEvent {

    private Long senderId;
    private Long receiverId;
}
