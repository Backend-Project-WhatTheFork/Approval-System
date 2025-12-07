package com.whatthefork.approvalsystem.repository;

import com.whatthefork.approvalsystem.domain.ApprovalReferrer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalReferrerRepository extends JpaRepository<ApprovalReferrer, Long> {
}
