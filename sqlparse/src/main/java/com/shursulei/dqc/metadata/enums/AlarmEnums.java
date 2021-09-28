package com.shursulei.dqc.metadata.enums;

public enum AlarmEnums {
    IN_ALARM(1,"告警"),
    OUT_ALARM(2,"不告警");
    private int code;
    private String desc;
    AlarmEnums(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }



}
