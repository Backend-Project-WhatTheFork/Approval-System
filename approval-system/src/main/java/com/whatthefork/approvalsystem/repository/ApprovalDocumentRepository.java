package com.whatthefork.approvalsystem.repository;

import com.whatthefork.approvalsystem.domain.ApprovalDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApprovalDocumentRepository extends JpaRepository<ApprovalDocument, Long> {
    Optional<ApprovalDocument> findById(long id);
}
