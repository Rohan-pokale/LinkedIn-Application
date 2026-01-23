package com.linkedIn.post_service.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostLikeEvent {
    private Long postId;
    private Long creatorId;
    private Long likeByUserId;
}
