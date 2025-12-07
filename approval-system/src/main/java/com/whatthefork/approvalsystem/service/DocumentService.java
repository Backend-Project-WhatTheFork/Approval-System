package com.whatthefork.approvalsystem.service;

import com.whatthefork.approvalsystem.common.error.BusinessException;
import com.whatthefork.approvalsystem.common.error.ErrorCode;
import com.whatthefork.approvalsystem.domain.ApprovalDocument;
import com.whatthefork.approvalsystem.domain.ApprovalHistory;
import com.whatthefork.approvalsystem.domain.ApprovalLine;
import com.whatthefork.approvalsystem.domain.ApprovalReferrer;
import com.whatthefork.approvalsystem.dto.request.CreateDocumentRequestDto;
import com.whatthefork.approvalsystem.dto.request.UpdateDocumentRequestDto;
import com.whatthefork.approvalsystem.enums.ActionTypeEnum;
import com.whatthefork.approvalsystem.enums.DocStatusEnum;
import com.whatthefork.approvalsystem.enums.LineStatusEnum;
import com.whatthefork.approvalsystem.repository.ApprovalDocumentRepository;
import com.whatthefork.approvalsystem.repository.ApprovalHistoryRepositoy;
import com.whatthefork.approvalsystem.repository.ApprovalLineRepository;
import com.whatthefork.approvalsystem.repository.ApprovalReferrerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/* 12.06 UserDetails 부분이 완성이 되면 모든 메소드의 Long userId는 Userdetials로 교체할 것 */
@Service
@RequiredArgsConstructor
public class DocumentService {

    private final ApprovalDocumentRepository approvalDocumentRepository;
    private final ApprovalLineRepository approvalLineRepository;
    private final ApprovalHistoryRepositoy approvalHistoryRepositoy;
    private final ApprovalReferrerRepository approvalReferrerRepository;

    @Transactional
    public Long createDocument(Long userId, CreateDocumentRequestDto requestDto) {

        /* (추후 구현) memberRepository에서 해당 멤버가 존재하는지 getDraftId로 판독 */

        // 기안 작성
        ApprovalDocument approvalDocument = ApprovalDocument.builder()
                        .drafter(userId)
                        .title(requestDto.getTitle())
                        .content(requestDto.getContent())
                        .createdAt(LocalDateTime.now())
                        .currentSequence(1)
                        .docStatus(DocStatusEnum.TEMP)
                        .version(1L)
                        .startVacationDate(requestDto.getStartVacationDate())
                        .endVacationDate(requestDto.getEndVacationDate())
                        .build();

        ApprovalDocument savedDoc = approvalDocumentRepository.save(approvalDocument);
        Long documentId = savedDoc.getId();

        // 결재선 저장
        List<Long> approvalIds = requestDto.getApproverIds();

        if(approvalIds == null || approvalIds.isEmpty()) {
            throw new IllegalArgumentException("결재자가 지정되지 않았습니다.");
        }

        if(approvalIds.size() == 3) {
            for (int i = 0; i < requestDto.getApproverIds().size(); i++) {
                Long approvalId = requestDto.getApproverIds().get(i);

                /* (추후 구현) MemberRepository로 결재자 ID가 존재하는지 검증 */

                ApprovalLine approvalLine = ApprovalLine.builder()
                                .document(documentId)
                                .approver(approvalId)
                                .lineStatus(LineStatusEnum.WAIT)
                                .sequence(i + 1)
                                .build();

                approvalLineRepository.save(approvalLine);
            }
        } else {
            throw new IllegalArgumentException("결재자 3명이 지정되지 않았습니다.");
        }

        // 참조자 설정
        if(requestDto.getReferrer() != null) {
            for(Long referrerId : requestDto.getReferrer()) {
                ApprovalReferrer approvalReferrer = ApprovalReferrer.builder()
                        .document(documentId)
                        .referrer(referrerId)
                        .viewedAt(null)
                        .build();

                approvalReferrerRepository.save(approvalReferrer);
            }
        }

        // 결재 로그 저장
        ApprovalHistory approvalHistory = ApprovalHistory.builder()
                .document(documentId)
                .actor(userId)
                .actionType(ActionTypeEnum.CREATE)
                .build();

        approvalHistoryRepositoy.save(approvalHistory);

        return savedDoc.getId();
    }

    @Transactional
    public void updateDocument(Long userId, Long docId, UpdateDocumentRequestDto requestDto) {
        List<Long> approvalIds = requestDto.getApproverIds();
        if(approvalIds == null || approvalIds.size() != 3) {
            throw new IllegalArgumentException("결재자 3명이 지정되지 않았습니다.");
        }

        ApprovalDocument approvalDocument = validateUpdateAuthority(userId, docId);

        approvalDocument.updateDocument(
                requestDto.getTitle(),
                requestDto.getContent(),
                requestDto.getStartVacationDate(),
                requestDto.getEndVacationDate()
        );

        approvalLineRepository.deleteByDocumentId(docId);

        for(int i = 0; i < approvalIds.size(); i++) {
            /* memberRepository로 결재자 검증 로직 필요 */

            Long approvalId = requestDto.getApproverIds().get(i);

            ApprovalLine newLine = ApprovalLine.builder()
                    .document(docId)
                    .approver(approvalId)
                    .lineStatus(LineStatusEnum.WAIT)
                    .sequence(i + 1)
                    .build();

            approvalLineRepository.save(newLine);
        }
    }

    @Transactional
    public void deleteDocument(Long userId, Long docId) {
        ApprovalDocument approvalDocument = validateDeleteAuthority(userId, docId);
        approvalDocument.deleteDocument();
    }

    private ApprovalDocument validateUpdateAuthority(Long userId, Long docId) {
        ApprovalDocument approvalDocument = approvalDocumentRepository.findById(docId).orElseThrow(
                () -> new BusinessException(ErrorCode.DOCUMENT_NOT_FOUND)
        );

        if(!approvalDocument.getDrafter().equals(userId)) {
            throw new BusinessException(ErrorCode.NOT_DRAFTER);
        }

        if(approvalDocument.getDocStatus() != DocStatusEnum.TEMP) {
            throw new BusinessException(ErrorCode.CANNOT_MODIFY_DOCUMENT);
        }

        return approvalDocument;
    }

    private ApprovalDocument validateDeleteAuthority(Long userId, Long docId) {
        ApprovalDocument approvalDocument = approvalDocumentRepository.findById(docId).orElseThrow(
                () -> new BusinessException(ErrorCode.DOCUMENT_NOT_FOUND)
        );

        if(!approvalDocument.getDrafter().equals(userId)) {
            throw new BusinessException(ErrorCode.NOT_DRAFTER);
        }

        if(approvalDocument.getDocStatus() != DocStatusEnum.TEMP) {
            throw new BusinessException(ErrorCode.CANNOT_DELETE_DOCUMENT);
        }

        return approvalDocument;
    }
}
