package com.shursulei.dqc.metadata.enums;

public enum ScheduleType {
    HOUR(1,"小时调度"),
    DAY(2,"天调度"),
    WEEK(3,"周调度"),
    MONTH(4,"月调度");
    private int code;
    private String desc;

    ScheduleType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
