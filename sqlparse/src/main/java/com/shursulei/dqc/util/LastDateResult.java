package com.shursulei.dqc.util;

/**
 * @author hanfeng
 * @version 1.0
 * @date 2021/12/23 17:46
 */
public class LastDateResult {
    private String userId;
    private String name;
    private String groupName;
    private String groupCenter;
    private String invoiceTitle;
    private String beginTime;
    private String endTime;
    private String aliTime;
    private String factTime;
    private String dubTime;
    private String deleteTime;
    private String deleteDays;
    private String lastFactTime;

    public String getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(String deleteTime) {
        this.deleteTime = deleteTime;
    }

    public String getDeleteDays() {
        return deleteDays;
    }

    public void setDeleteDays(String deleteDays) {
        this.deleteDays = deleteDays;
    }

    public String getLastFactTime() {
        return lastFactTime;
    }

    public void setLastFactTime(String lastFactTime) {
        this.lastFactTime = lastFactTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupCenter() {
        return groupCenter;
    }

    public void setGroupCenter(String groupCenter) {
        this.groupCenter = groupCenter;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAliTime() {
        return aliTime;
    }

    public void setAliTime(String aliTime) {
        this.aliTime = aliTime;
    }

    public String getFactTime() {
        return factTime;
    }

    public void setFactTime(String factTime) {
        this.factTime = factTime;
    }

    public String getDubTime() {
        return dubTime;
    }

    public void setDubTime(String dubTime) {
        this.dubTime = dubTime;
    }
}
