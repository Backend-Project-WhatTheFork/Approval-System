package com.whatthefork.communicationandalarm.common.dto.response;

import com.whatthefork.communicationandalarm.common.enums.Category;
import com.whatthefork.communicationandalarm.post.domain.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GetPostResponse {

    private Long id;
    private Long memberId;
    private Category category;
    private String title;
    private String content;
    private Boolean isPinned;
    private long viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static GetPostResponse of(Post post, long viewCount) {
        return GetPostResponse.builder()
                .id(post.getId())
                .memberId(post.getMemberId())
                .category(post.getCategory())
                .title(post.getTitle())
                .content(post.getContent())
                .isPinned(post.getIsPinned())
                .viewCount(viewCount)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
