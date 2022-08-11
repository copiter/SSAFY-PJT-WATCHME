package com.A108.Watchme.Http;

import com.A108.Watchme.oauth.entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Code {
    C200 (200, "SUCCESS"),
    C300 (300, "INVALID"),
    C500 (500, "NOT MEMBER"),
    C501 (501, "JWT EXPIRED"),
    // 520 에서부터 ROOM관련 ERROR
    C520 (520, "NO SEARCH DATA"),
    C521 (521, "INVALID VALUE"),
    C522 (522, "NO ROOM"),
    C523 (523,"NO PARTICIPANTS"),
    C524 (524, "NAME ALREADY EXIST"),
    C525 (525, "ROOM PWD NOT INTEGER"),
    C526 (526,"INVALID ROOM PEOPLE NUM"),
    C527 (527, "INVALID END DATE"),
    C528 (528, "NOT IMAGE FILE"),
    C529 (529, "IMAGE TOO BIG"),
    C530 (530, "NOT ROOM OWNER"),
    C531 (531, "FAILED CREATE STUDY LOG"),
    C532 (532, "FAILED SAVE STUDY LOG");

    private int errCode;

    private final String message;


}

