package com.whatthefork.communicationandalarm.common.dto.response;

import com.whatthefork.communicationandalarm.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class PostResponse {
    private Long id;
    private Long memberId;
    private Boolean isAnnouncement;
    private String title;
    private String content;
    private Boolean isPinned;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PostResponse from(Post post, Boolean isAnnouncement, String username) {
        return null;
    }
}
