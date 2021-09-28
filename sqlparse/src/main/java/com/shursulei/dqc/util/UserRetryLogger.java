package com.shursulei.dqc.util;

import com.aliyun.odps.OdpsException;
import com.aliyun.odps.rest.RestClient;

public class UserRetryLogger extends RestClient.RetryLogger {
    @Override
    public void onRetryLog(Throwable e, long retryCount, long sleepTime) {
        if (e != null && e instanceof OdpsException) {
            String requestId = ((OdpsException) e).getRequestId();
            if (requestId != null) {
                System.err.println(String.format(
                        "Warning: ODPS request failed, requestID:%s, retryCount:%d, will retry in %d seconds.", requestId, retryCount, sleepTime));
                return;
            }
        }
        System.err.println(String.format(
                "Warning: ODPS request failed:%s, retryCount:%d, will retry in %d seconds.", e.getMessage(), retryCount, sleepTime));
    }
}
