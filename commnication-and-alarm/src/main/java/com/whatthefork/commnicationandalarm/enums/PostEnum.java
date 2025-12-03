package com.whatthefork.commnicationandalarm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostEnum {
    PINNED("고정"),
    ANNOUNCEMENT("공지사항 게시판"),
    COMMON("일반 게시판"),
    ACTIVE("게시"),
    DELETED("삭제");

    private final String description;
}