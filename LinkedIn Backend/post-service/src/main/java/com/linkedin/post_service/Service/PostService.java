package com.linkedin.post_service.Service;

import com.linkedin.post_service.Dto.PostCreateRequestDto;
import com.linkedin.post_service.Dto.PostDto;
import com.linkedin.post_service.Entity.Post;
import com.linkedin.post_service.Exceptions.ResourceNotFoundException;
import com.linkedin.post_service.Repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public PostDto createPost(PostCreateRequestDto postCreateRequestDto,Long userId) {

        Post post=modelMapper.map(postCreateRequestDto,Post.class);
        post.setUserId(userId);
        Post savedPost=postRepository.save(post);
        return modelMapper.map(savedPost,PostDto.class);

    }

    public PostDto getPostById(Long postId) {
        log.debug("retrive post by id: {}",postId);
        Post post=  postRepository.findById(postId).orElseThrow(()->
                new ResourceNotFoundException("Post Not found for postId:"+postId));

        return modelMapper.map(post,PostDto.class);
    }

    public List<PostDto> getAllPostsOfUser(Long userId) {
        List<Post> posts= postRepository.findByUserId(userId);
        return posts
                .stream()
                .map((element) -> modelMapper.map(element, PostDto.class))
                .collect(Collectors.toList());
    }
}
