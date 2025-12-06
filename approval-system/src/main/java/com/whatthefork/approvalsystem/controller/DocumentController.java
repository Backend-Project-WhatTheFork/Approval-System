package com.whatthefork.approvalsystem.controller;

import com.whatthefork.approvalsystem.common.ApiResponse;
import com.whatthefork.approvalsystem.dto.request.CreateDocumentRequestDto;
import com.whatthefork.approvalsystem.dto.request.UpdateDocumentRequestDto;
import com.whatthefork.approvalsystem.service.DocumentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/document")
public class DocumentController {

    @Autowired
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    /* 12.06 모든 메소드의 매개변수에 userId 대신 @AuthenticationPrincipal UserDetailsImpl로 로그인된 사용자 정보에서 Id 추출할 예정. 현재는 임시 기입 */
    @PostMapping("/drafting")
    public ResponseEntity<ApiResponse> createDocument(Long userId, @RequestBody @Valid CreateDocumentRequestDto requestDto) {
        Long docId = documentService.createDocument(userId, requestDto);
        return ResponseEntity.ok(ApiResponse.success(docId));
    }

    @PutMapping("/{docId}")
    public ResponseEntity<ApiResponse> updateDocument(Long userId, @PathVariable Long docId, @RequestBody @Valid UpdateDocumentRequestDto requestDto) {
        documentService.updateDocument(userId, docId, requestDto);
        return ResponseEntity.ok(ApiResponse.success("기안 수정 완료"));
    }

    @DeleteMapping("/{docId}")
    public ResponseEntity<ApiResponse> deleteDocument(Long userId, @PathVariable Long docId) {
        documentService.deleteDocument(userId, docId);
        return ResponseEntity.ok(ApiResponse.success("기안 삭제 완료"));
    }
}
