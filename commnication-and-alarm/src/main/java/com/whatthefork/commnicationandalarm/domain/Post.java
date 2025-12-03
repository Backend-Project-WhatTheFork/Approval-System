package com.whatthefork.commnicationandalarm.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "topic_id", nullable=false)
    private Long topicId;

    @Column(name = "title", length = 30, nullable = false, columnDefinition = "TEXT")
    private String title;

    @Column(name = "content", length = 100, nullable = false)
    private String content;

    @Column(name = "view_count", nullable = false)
    private Integer viewCount;

    @Column(name = "comment_count", nullable=false)
    private Integer commentCount;

    @Column(name = "is_pinned", nullable = false)
    public boolean isPinned;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "is_deleted", nullable = false)
    public boolean isDeleted;

    public void createPost(Long memberId, String content, Long topicId) {
        this.memberId = memberId;
        this.content = content;
        this.topicId = topicId;
    }

    public void modifyPost(String content, Long topicId) {
        this.content = content;
        this.topicId = topicId;
    }

    public void increaseViewCont()
    {
        this.viewCount = this.viewCount + 1;
    }

    @PrePersist
    public void postDefault() {
        if (commentCount == null) commentCount = 0;
        if (viewCount == null) viewCount = 0;
    }

    public void increaseCommentCount() {
        if (commentCount == null) commentCount = 0;
        commentCount++;
    }

    public void decreaseCommentCount() {
        if (commentCount == null || commentCount == 0) {
            commentCount = 0;
        } else {
            commentCount--;
        }
    }
}
