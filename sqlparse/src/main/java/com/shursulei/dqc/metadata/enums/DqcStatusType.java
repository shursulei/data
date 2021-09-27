package com.shursulei.dqc.metadata.enums;

public enum DqcStatusType {
    IS_OFFINE(1,"上线"),
    OUT_OFFINE(0,"下线");
    private int code;
    private String desc;

    DqcStatusType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
