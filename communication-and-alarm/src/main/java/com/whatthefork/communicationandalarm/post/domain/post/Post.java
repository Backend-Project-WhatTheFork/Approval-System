package com.whatthefork.communicationandalarm.post.domain.post;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Post {

    @Id
    @Column(name = "post_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Column(name = "is_announcement", nullable=false)
    private Boolean isAnnouncement;

    @Column(name = "title", length = 20, nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "is_pinned", nullable = false)
    public Boolean isPinned;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "is_deleted", nullable = false)
    public Boolean isDeleted = false;

    @Builder
    public Post(Long memberId, String memberName, Boolean isAnnouncement, String title, String content, Boolean isPinned, LocalDateTime updatedAt, LocalDateTime createdAt, Boolean isDeleted) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.isAnnouncement = isAnnouncement;
        this.title = title;
        this.content = content;
        this.isPinned = isPinned;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.isDeleted = isDeleted;
    }

    public static Post create(Long memberId, String memberName, String title, String content) {
        LocalDateTime now = LocalDateTime.now();
        return Post.builder()
                .memberId(memberId)
                .memberName(memberName)
                .isAnnouncement(false)
                .title(title)
                .content(content)
                .isPinned(false)
                .updatedAt(now)
                .createdAt(now)
                .isDeleted(false)
                .build();
    }
}
