package com.shursulei.dqc.metadata.enums;

public enum DqcType {
    TABLE_LEVEL(1,"表级"),
    COLUMN_LEVEL(2,"字段级"),
    DATASORUCE_LEVEL(3,"数据源级");
    private int code;
    private String desc;
    DqcType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
