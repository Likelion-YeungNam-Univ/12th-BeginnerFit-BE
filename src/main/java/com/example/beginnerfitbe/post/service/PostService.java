package com.example.beginnerfitbe.post.service;

import com.example.beginnerfitbe.category.domain.Category;
import com.example.beginnerfitbe.category.repository.CategoryRepository;
import com.example.beginnerfitbe.error.StateResponse;
import com.example.beginnerfitbe.post.domain.Post;
import com.example.beginnerfitbe.post.dto.PostCreateDto;
import com.example.beginnerfitbe.post.dto.PostDto;
import com.example.beginnerfitbe.post.dto.PostUpdateDto;
import com.example.beginnerfitbe.post.repository.PostRepository;
import com.example.beginnerfitbe.s3.util.S3Uploader;
import com.example.beginnerfitbe.user.domain.User;
import com.example.beginnerfitbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final S3Uploader s3Uploader;

    //글 생성
    public ResponseEntity<StateResponse> create(Long userId, PostCreateDto postCreateDto, MultipartFile postPicture){
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("not found user"));

            Category category = categoryRepository.findByCategoryName(postCreateDto.getCategoryName())
                    .orElseThrow(() -> new IllegalArgumentException("not found category"));

            String pictureUrl = null;
            if (postPicture != null && !postPicture.isEmpty()) {
                pictureUrl = s3Uploader.upload(postPicture, "Post");
            }

            Post post = Post.builder()
                    .title(postCreateDto.getTitle())
                    .content(postCreateDto.getContent())
                    .pictureUrl(pictureUrl)
                    .createdAt(LocalDateTime.now())
                    .user(user)
                    .category(category)
                    .build();

            postRepository.save(post);

            return ResponseEntity.ok(StateResponse.builder().code("SUCCESS").message("글을 성공적으로 생성했습니다.").build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(StateResponse.builder().code("ERROR").message("오류가 발생했습니다: " + e.getMessage()).build());
        }
    }
    //전체 글 조회
    public List<PostDto> list(){
        return postRepository.findAll().stream()
                .map(PostDto::fromEntity)
                .collect(Collectors.toList());
    }
    //postId로 조회
    public PostDto read(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다. id= " + postId));
        return PostDto.fromEntity(post);
    }
    //내가 작성한 글 조회
    public List<PostDto> me(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("not found user"));
        List<Post> posts= postRepository.findAllByUser(user);
        return posts.stream()
                .map(PostDto::fromEntity)
                .collect(Collectors.toList());
    }
    public List<PostDto> getPostsByCategoryName(String categoryName){
        Category category = categoryRepository.findByCategoryName(categoryName).orElseThrow(() -> new IllegalArgumentException("not found category"));
        List<Post> posts = postRepository.findPostsByCategory(category);

        return posts.stream()
                .map(PostDto::fromEntity)
                .collect(Collectors.toList());
    }
    public ResponseEntity<StateResponse> update(Long postId, Long id, PostUpdateDto updateDto, MultipartFile postPicture) {
        try {
            Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("not found post"));

            Long userId = post.getUser().getId();
            String previousPictureUrl = post.getPictureUrl();

            if (!userId.equals(id)) throw new IllegalArgumentException("작성자만 글을 수정할 수 있습니다.");

            String title = updateDto.getTitle();
            String content = updateDto.getContent();
            String categoryName = updateDto.getCategoryName();

            if (title != null && content != null && categoryName != null) {
                Category category = categoryRepository.findByCategoryName(categoryName).orElseThrow(() -> new IllegalArgumentException("not found category"));
                post.update(title, content, category);
            }

            if (previousPictureUrl != null) {
                s3Uploader.delete("Post",previousPictureUrl);
            }
            String newPictureUrl=null;
            // 사진 수정
            if (postPicture != null && !postPicture.isEmpty()) {
                if (previousPictureUrl != null) {
                    s3Uploader.delete("Post",previousPictureUrl);
                }
                newPictureUrl = s3Uploader.upload(postPicture, "Post");
            }
            post.updatePicture(newPictureUrl);
            postRepository.save(post);
            return ResponseEntity.ok(StateResponse.builder().code("SUCCESS").message("게시글을 성공적으로 업데이트했습니다.").build());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(StateResponse.builder().code("ERROR").message("오류가 발생했습니다: " + e.getMessage()).build());
        }
    }

    @Transactional
    public ResponseEntity<StateResponse> delete(Long postId, Long id){
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("not found post"));
        Long userId = post.getUser().getId();
        if(!userId.equals(id)) throw new IllegalArgumentException("작성자만 글을 삭제할 수 있습니다.");

        String previousPictureUrl = post.getPictureUrl();
        if(previousPictureUrl!=null){
            s3Uploader.delete("Post",previousPictureUrl);
        }

        postRepository.delete(post);
        return ResponseEntity.ok(StateResponse.builder().code("SUCCESS").message("글을 성공적으로 삭제했습니다.").build());
    }


}
