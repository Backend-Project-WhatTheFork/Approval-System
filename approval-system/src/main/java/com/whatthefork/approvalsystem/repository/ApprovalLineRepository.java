package com.whatthefork.approvalsystem.repository;

import com.whatthefork.approvalsystem.domain.ApprovalDocument;
import com.whatthefork.approvalsystem.domain.ApprovalLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalLineRepository extends JpaRepository<ApprovalLine, Long> {

}
