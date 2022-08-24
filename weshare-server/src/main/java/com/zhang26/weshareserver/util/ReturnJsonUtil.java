package com.zhang26.weshareserver.util;

import com.zhang26.weshareserver.config.ReturnStatus;
import lombok.Data;

@Data
public class ReturnJsonUtil<T> {
    private int statusCode;
    private String statusMsg;
    private T data;

    public ReturnJsonUtil() {}

    public ReturnJsonUtil(ReturnStatus rs) {
        statusCode = rs.getStatusCode();
        statusMsg = rs.getStatusMsg();
    }

    public ReturnJsonUtil(int statusCode, T data) {
        this.statusCode = statusCode;
        statusMsg = ReturnStatus.getMsgByCode(statusCode);
        this.data = data;
    }

    public ReturnJsonUtil(int statusCode) {
        this.statusCode = statusCode;
        statusMsg = ReturnStatus.getMsgByCode(statusCode);
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        statusMsg = ReturnStatus.getMsgByCode(statusCode);
    }

    public void setStatus(ReturnStatus rs) {
        statusCode = rs.getStatusCode();
        statusMsg = rs.getStatusMsg();
    }

    public void setData(T data) {
        this.data = data;
    }
}
