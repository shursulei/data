package com.shursulei.dqc.metadata;

/**
 * @author hanfeng
 * @version 1.0
 * @date 2021/9/26 17:12
 */
public enum DbType {
    MYSQL("mysql"),
    ODPS("ODPS");
    private String type;

    DbType(String type) {
        this.type = type;
    }
}
