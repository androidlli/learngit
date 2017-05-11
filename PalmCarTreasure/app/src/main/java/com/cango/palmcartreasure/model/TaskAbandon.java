package com.cango.palmcartreasure.model;

/**
 * Created by cango on 2017/5/10.
 */

public class TaskAbandon {

    /**
     * Success : true
     * Msg : 操作成功
     * Code : 0
     * Data : null
     */

    private boolean Success;
    private String Msg;
    private int Code;
    private Object Data;

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean Success) {
        this.Success = Success;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public Object getData() {
        return Data;
    }

    public void setData(Object Data) {
        this.Data = Data;
    }
}
