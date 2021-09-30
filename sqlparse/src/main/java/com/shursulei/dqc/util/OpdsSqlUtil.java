package com.shursulei.dqc.util;

import com.aliyun.odps.*;
import com.aliyun.odps.account.Account;
import com.aliyun.odps.account.AliyunAccount;
import com.aliyun.odps.task.SQLTask;

public class OpdsSqlUtil {
    private static final String accessId = "xxx";
    private static final String accessKey = "xxxxx";
    private static final String endPoint = "http://service.cn-shanghai.maxcompute.aliyun.com/api";
    private static final String project = "xxx";
    private static final String sql = "select * from Information_Schema.TASKS_HISTORY1 where ds='20210927' LIMIT 1000;";
    public static void main(String[] args) throws OdpsException {
        Account account = new AliyunAccount(accessId, accessKey);
        Odps odps = new Odps(account);
        odps.setEndpoint(endPoint);
        odps.setDefaultProject(project);
        odps.getRestClient().setRetryLogger(new UserRetryLogger());
        for (Table t : odps.tables()) {
            System.out.println(t.getName()+":"+t.getComment()+":"+t.getPartitions()+":"+t.getJsonSchema());
        }
//        Instance instance= odps.instances().get("leoao_test_odps","xxx");
//        System.out.println(instance.getJobName());
//        System.out.println(instance.getEndTime());
//        System.out.println(instance.getId());
//        System.out.println(instance.getTaskDetailJson("AnonymousSQLTask"));
//        System.out.println();
//        for (String a:instance.getTaskNames()){
//            System.out.println(a);
//        }
//        System.out.println(instance.getStatus());
//        Instance.TaskSummary summary = instance.getTaskSummary("AnonymousSQLTask");
//        String a=summary.getJsonSummary();
//        String s = summary.getSummaryText();
//        System.out.println(a);
//        System.out.println(s);
//        System.out.println();
//        Instance i;
//        try {
//            i = SQLTask.run(odps, sql);
//            System.out.println(i.getId());
//        } catch (OdpsException e) {
//            e.printStackTrace();
//        }
    }
}
