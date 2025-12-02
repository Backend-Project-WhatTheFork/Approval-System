package com.whatthefork.approvalsystem.domain;

import com.whatthefork.approvalsystem.enums.DocStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class ApprovalDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_id")
    private Long id;

    // 기안자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drafter_id", nullable = false)
    private Member drafter;

    @Column(nullable = false)
    private Long categoryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "doc_status", nullable = false)
    private DocStatusEnum docStatus;

    @Column(nullable = false)
    private String title;

    // 사진 등의 대용량 데이터 처리를 위한 어노테이션
    @Lob
    @Column(nullable = false)
    private String content;

    @Version
    private Long version;

    @Column(name = "current_seq", nullable = false)
    private int currentSequence = 1;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // 하나의 문서에 여러 개의 결재 라인
    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApprovalLine> lines = new ArrayList<>();
}