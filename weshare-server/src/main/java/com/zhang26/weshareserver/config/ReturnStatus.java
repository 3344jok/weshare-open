package com.zhang26.weshareserver.config;

public enum ReturnStatus {
    // 响应正常
    SUCCESS(1000, "服务器响应正常"),
    RESPONSE_SUCCESS(1001, "服务器响应正常且数据操作正常"),

    // 事务异常
    TRANSACTION_ERROR(2000, "事务操作异常"),

    // 服务器非正常
    SERVER_ERROR(3000, "服务器异常"),

    // 用户非正常
    USER_AUTH_DENY(4000, "用户权限不足"),
    USER_PASSWORD_ERROR(4001, "用户密码错误"),
    USER_ACCOUNT_EXPIRED(4002, "用户账号过期"),
    USER_PASSWORD_EXPIRED(4003, "用户密码过期"),
    USER_ACCOUNT_DISABLE(4004, "用户账号不可用"),
    USER_ACCOUNT_LOCKED(4005, "用户账号锁定"),
    USER_ACCOUNT_NOT_EXIST(4006, "用户不存在"),
    USER_NOT_LOGIN(4007, "用户未登录"),

    // 其他错误
    COMMON_ERROR(9000, "未知错误");

    private int statusCode;
    private String statusMsg;

    ReturnStatus(int statusCode, String statusMsg) {
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public static String getMsgByCode(int statusCode) {
        for (ReturnStatus rs : ReturnStatus.values()) {
            if (rs.getStatusCode() == statusCode) return rs.getStatusMsg();
        }
        return "";
    }
}
