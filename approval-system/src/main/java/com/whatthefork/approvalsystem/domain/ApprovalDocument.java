package com.whatthefork.approvalsystem.domain;

import com.whatthefork.approvalsystem.enums.DocStatusEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 기안자
    @Column(name = "drafter_id", nullable = false)
    private Long drafter;

    @Enumerated(EnumType.STRING)
    @Column(name = "doc_status", nullable = false)
    private DocStatusEnum docStatus; // ENUM: TEMP, IN_PROGRESS, APPROVED, REJECTED

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Version
    private Long version;

    @Column(name = "current_seq", nullable = false)
    private int currentSequence = 1;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

}