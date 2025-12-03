package com.whatthefork.commnicationandalarm.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor

public class Comment {

    @Id
    @Column(name = "comment_id", nullable = false)
    private Long commentId;

    @Column(name = "member_id",  nullable = false)
    private Long memberId;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "parent_id")
    private Long parentCommentId;

    @Column(name = "memberName", nullable = false)
    private String memberName;

    @Column(name = "depth", nullable = false)
    private Integer depth = 0;

    @Column(name = "content", nullable = false, length = 100)
    private String content;

    @Column(name = "like_count")
    private Integer likeCount = 0;

    public Comment(Long memberId, Long postId, Long parentCommentId, String memberName, Integer depth, String content) {
        this.memberId = memberId;
        this.postId = postId;
        this.parentCommentId = parentCommentId;
        this.memberName = memberName;
        this.depth = depth;
        this.content = content;
        this.likeCount = 0;
    }

    public void updateContent(String newContent) {
        this.content = newContent;
    }
}
