package com.shursulei.dqc.metadata.enums;

public enum RuleSqlType {
    SQL_TEMPLAET(1,"sql 模版"),
    SQL_CUSTOM(2,"自定义SQL");
    private int code;
    private String desc;
    RuleSqlType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
