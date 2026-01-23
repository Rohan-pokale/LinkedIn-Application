package com.linkedIn.post_service.Service;
import com.linkedIn.post_service.Auth.UserContextHolder;
import com.linkedIn.post_service.Entity.Post;
import com.linkedIn.post_service.Entity.PostLike;
import com.linkedIn.post_service.Exceptions.BadRequestException;
import com.linkedIn.post_service.Exceptions.ResourceNotFoundException;
import com.linkedIn.post_service.Repository.PostLikeRepository;
import com.linkedIn.post_service.Repository.PostRepository;
import com.linkedIn.post_service.events.PostLikeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class LikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final KafkaTemplate<Long,PostLikeEvent> kafkaTemplate;

    public void likePost(Long postId){
        Long userId= UserContextHolder.getCurrentUserId();
        log.info("attempting to like the post with postId:{}",postId);

        Post post=postRepository.findById(postId).orElseThrow(
                ()->new ResourceNotFoundException("Post not found for postId: "+postId));

        boolean liked=postLikeRepository.existsByUserIdAndPostId(userId,postId);

        if(liked) throw new BadRequestException("Cannot like same post again.");

        PostLike postLike=new PostLike();
        postLike.setPostId(postId);
        postLike.setUserId(userId);

        postLikeRepository.save(postLike);

        log.info("liked the post successfully with postId:{}",postId);

        PostLikeEvent postLikeEvent=PostLikeEvent
                .builder()
                .likeByUserId(userId)
                .postId(postId)
                .creatorId(post.getUserId())
                .build();

        kafkaTemplate.send("post-like-topic",postId,postLikeEvent);
    }


    public void unLikePost(Long postId) {
        Long userId= UserContextHolder.getCurrentUserId();
        log.info("attempting to unLike the post with postId:{}",postId);

        boolean exist = postRepository.existsById(postId);
        if(!exist){
            throw new ResourceNotFoundException("Post Not found for postId:"+postId);
        }
        boolean liked=postLikeRepository.existsByUserIdAndPostId(userId,postId);

        if(!liked) throw new BadRequestException("Post is already unLiked");

        postLikeRepository.deleteByUserIdAndPostId(userId,postId);

        log.info("unLike the post successfully with postId:{}",postId);
    }
}
