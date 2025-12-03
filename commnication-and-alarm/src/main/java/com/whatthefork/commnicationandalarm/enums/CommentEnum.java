package com.whatthefork.commnicationandalarm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentEnum {
    ACTIVE("게시"),
    DELETED("삭제");

    private final String description;
}