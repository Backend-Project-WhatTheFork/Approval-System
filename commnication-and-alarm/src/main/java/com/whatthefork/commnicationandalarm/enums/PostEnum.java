package com.whatthefork.commnicationandalarm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostEnum {
    PINNED,
    ANNOUNCEMENT,
    COMMON,
    ACTIVE,
    DELETED
}