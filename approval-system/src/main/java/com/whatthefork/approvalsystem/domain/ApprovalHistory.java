package com.whatthefork.approvalsystem.domain;

import com.whatthefork.approvalsystem.enums.ActionTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private ApprovalDocument document;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private Member actor;

    @Column(nullable = false)
    private LocalDateTime openDate =  LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionTypeEnum actionType;

}
