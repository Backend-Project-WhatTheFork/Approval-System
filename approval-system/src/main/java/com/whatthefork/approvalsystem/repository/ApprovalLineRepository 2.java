package com.whatthefork.approvalsystem.repository;

import com.whatthefork.approvalsystem.domain.ApprovalLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApprovalLineRepository extends JpaRepository<ApprovalLine, Long> {
    @Query("DELETE FROM ApprovalLine al WHERE al.document = :docId")
    @Modifying(clearAutomatically = true)
    void deleteByDocumentId(@Param("docId") Long docId);
}
