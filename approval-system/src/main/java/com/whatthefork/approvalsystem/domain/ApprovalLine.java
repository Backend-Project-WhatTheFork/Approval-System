package com.whatthefork.approvalsystem.domain;

import com.whatthefork.approvalsystem.enums.LineStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_id", nullable = false)
    private ApprovalDocument document;

    // 지정된 결재자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id", nullable = false)
    private Member approver;

    @Column(nullable = false)
    private int sequence;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LineStatusEnum lineStatus;  // ENUM: WAIT, APPROVED, REJECTED, CANCLED

    private String comment;

    @Column(name = "approval_at")
    private LocalDateTime approvalDate;

    @Column(nullable = false)
    private boolean isArbitrary = false;

}
