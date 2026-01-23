package com.linkedIn.post_service.Service;

import com.linkedIn.post_service.Auth.UserContextHolder;
import com.linkedIn.post_service.Clients.ConnectionsClient;
import com.linkedIn.post_service.Dto.PostCreateRequestDto;
import com.linkedIn.post_service.Dto.PostDto;
import com.linkedIn.post_service.Entity.Post;
import com.linkedIn.post_service.Exceptions.ResourceNotFoundException;
import com.linkedIn.post_service.Repository.PostRepository;
import com.linkedIn.post_service.events.PostCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final ConnectionsClient connectionsClient;
    private final KafkaTemplate<Long, PostCreatedEvent> kafkaTemplate;

    public PostDto createPost(PostCreateRequestDto postCreateRequestDto) {
        Long userId=UserContextHolder.getCurrentUserId();
        Post post=modelMapper.map(postCreateRequestDto,Post.class);
        post.setUserId(userId);
        Post savedPost=postRepository.save(post);

        PostCreatedEvent postCreatedEvent=PostCreatedEvent
                .builder()
                .content(savedPost.getContent())
                .postId(savedPost.getId())
                .creatorId(savedPost.getUserId())
                .build();
        kafkaTemplate.send("post-created-topic",postCreatedEvent);
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
