package com.whatthefork.approvalsystem.controller;

import com.whatthefork.approvalsystem.common.ApiResponse;
import com.whatthefork.approvalsystem.dto.request.CreateDocumentRequestDto;
import com.whatthefork.approvalsystem.service.DocumentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/approval/drafting")
public class DocumentController {

    @Autowired
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createDraft(@RequestBody @Valid CreateDocumentRequestDto requestDto) {
        Long docId = documentService.createDocument(requestDto);
        return ResponseEntity.ok(ApiResponse.success(docId));
    }
}
