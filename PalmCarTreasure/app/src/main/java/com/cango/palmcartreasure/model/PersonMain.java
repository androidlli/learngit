package com.cango.palmcartreasure.model;

import com.cango.palmcartreasure.util.CommUtil;

/**
 * Created by cango on 2017/5/15.
 */

public class PersonMain {

    /**
     * Success : true
     * Msg : 操作成功
     * Code : 0
     * Data : {"allTaskCount":20,"undoneTaskCount":9,"doneTaskCount":5,"unreadMessage":6}
     */

    private boolean Success;
    private String Msg;
    private int Code;
    private DataBean Data;

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

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public static class DataBean {
        /**
         * allTaskCount : 20
         * undoneTaskCount : 9
         * doneTaskCount : 5
         * unreadMessage : 6
         */

        private int allTaskCount;
        private int undoneTaskCount;
        private int doneTaskCount;
        private int unreadMessage;

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            if (CommUtil.checkIsNull(nickName))
                nickName="";
            this.nickName = nickName;
        }

        private String nickName;

        public int getAllTaskCount() {
            return allTaskCount;
        }

        public void setAllTaskCount(int allTaskCount) {
            this.allTaskCount = allTaskCount;
        }

        public int getUndoneTaskCount() {
            return undoneTaskCount;
        }

        public void setUndoneTaskCount(int undoneTaskCount) {
            this.undoneTaskCount = undoneTaskCount;
        }

        public int getDoneTaskCount() {
            return doneTaskCount;
        }

        public void setDoneTaskCount(int doneTaskCount) {
            this.doneTaskCount = doneTaskCount;
        }

        public int getUnreadMessage() {
            return unreadMessage;
        }

        public void setUnreadMessage(int unreadMessage) {
            this.unreadMessage = unreadMessage;
        }
    }
}
