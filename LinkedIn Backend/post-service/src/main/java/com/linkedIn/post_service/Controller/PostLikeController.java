package com.linkedIn.post_service.Controller;

import com.linkedIn.post_service.Service.LikeService;
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
        likeService.likePost(postId);
        return ResponseEntity.noContent().build()   ;
    }
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> unLikePost(@PathVariable Long postId){
        likeService.unLikePost(postId);
        return ResponseEntity.noContent().build()   ;
    }
}
