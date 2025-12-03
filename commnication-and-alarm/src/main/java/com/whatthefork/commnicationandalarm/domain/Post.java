package com.whatthefork.commnicationandalarm.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @Column(name = "post_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "topic_id", nullable=false)
    private Long topicId;

    @Column(name = "title", length = 20, nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
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
    public boolean isDeleted = false;

    public void increaseViewCont()
    {
        this.viewCount = this.viewCount + 1;
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
