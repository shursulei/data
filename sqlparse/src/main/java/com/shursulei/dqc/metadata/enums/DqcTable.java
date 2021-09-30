package com.shursulei.dqc.metadata.enums;

public enum DqcTable {
    PRO_SQL_RESULT_TABLE("dqc_pro_result_monitor","生产环境规则执行任务结果表"),
    TEST_SQL_RESULT_TABLE("dqc_test_result_monitor","测试环境规则执行任务结果表"),
    PRO_MODEL_SQL_TABLE("dqc_pro_model_sql_result","模版sql生产执行"),
    TEST_MODEL_SQL_TABLE("dqc_test_model_sql_result","模版sql测试执行");
    private String tablename;
    private String desc;

    DqcTable(String tablename, String desc) {
        this.tablename = tablename;
        this.desc = desc;
    }
}
