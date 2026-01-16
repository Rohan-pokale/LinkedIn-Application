package com.linkedin.post_service.Controller;

import com.linkedin.post_service.Service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class PostLikeController {

    private final LikeService likeService;

    @PostMapping("/{postId}")
    public ResponseEntity<Void> likePost(@PathVariable Long postId){
        likeService.likePost(postId,1L);
        return ResponseEntity.noContent().build()   ;
    }
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> unLikePost(@PathVariable Long postId){
        likeService.unLikePost(postId,1L);
        return ResponseEntity.noContent().build()   ;
    }
}
