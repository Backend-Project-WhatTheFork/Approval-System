package com.whatthefork.communicationandalarm.post.controller;

import com.whatthefork.communicationandalarm.common.ApiResponse;
import com.whatthefork.communicationandalarm.post.domain.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /*
    * 게시글 등록
    * */
    public ResponseEntity<ApiResponse<Void>> createPost() {
        return null;
    }
    // 게시글을 만들기위해서 먼저 post 제목과

}
