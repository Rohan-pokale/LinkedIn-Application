package com.linkedin.post_service.Controller;
import com.linkedin.post_service.Auth.UserContextHolder;
import com.linkedin.post_service.Dto.PostCreateRequestDto;
import com.linkedin.post_service.Dto.PostDto;
import com.linkedin.post_service.Entity.Post;
import com.linkedin.post_service.Service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @RequestBody PostCreateRequestDto postCreateRequestDto, HttpServletRequest httpServletRequest){
        PostDto createdPost=postService.createPost(postCreateRequestDto,1L);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long postId){
        Long userId= UserContextHolder.getCurrentUserId();
        log.info("user id :{}",userId);
        PostDto postDto=postService.getPostById(postId);
        return ResponseEntity.ok(postDto);
    }

    @GetMapping("/user/{userId}/allPosts")
    public ResponseEntity<List<PostDto>> getAllPostsOfUser(@PathVariable Long userId){
        List<PostDto> posts=postService.getAllPostsOfUser(userId);
        return ResponseEntity.ok(posts);
    }
}
