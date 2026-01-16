package com.linkedin.post_service.Service;
import com.linkedin.post_service.Entity.PostLike;
import com.linkedin.post_service.Exceptions.BadRequestException;
import com.linkedin.post_service.Exceptions.ResourceNotFoundException;
import com.linkedin.post_service.Repository.PostLikeRepository;
import com.linkedin.post_service.Repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class LikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    public void likePost(Long postId,Long userId){
        log.info("attempting to like the post with postId:{}",postId);
        boolean exist = postRepository.existsById(postId);
        if(!exist){
            throw new ResourceNotFoundException("Post Not found for postId:"+postId);
        }
        boolean liked=postLikeRepository.existsByUserIdAndPostId(userId,postId);

        if(liked) throw new BadRequestException("Cannot like same post again.");

        PostLike postLike=new PostLike();
        postLike.setPostId(postId);
        postLike.setUserId(userId);

        postLikeRepository.save(postLike);

        log.info("liked the post successfully with postId:{}",postId);
    }


    public void unLikePost(Long postId, long userId) {
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
