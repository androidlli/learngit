package com.cango.palmcartreasure.model;

import java.util.List;

/**
 * Created by cango on 2017/5/17.
 */

public class CallRecord {

    /**
     * Success : true
     * Msg : 操作成功
     * Code : 0
     * Data : {"CallRecordData":[{"callUser":"李公子","callTime":"2017-05-03 22:48:18","comment":"态度可以"},{"callUser":"李公子","callTime":"2017-05-03 22:48","comment":"态度可以"},{"callUser":"李公子","callTime":"2017-05-03 22:48","comment":"态度可以"}],"OverdueData":{"overdueReason":"手头有点紧","overdueComment":"老套路"}}
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
         * CallRecordData : [{"callUser":"李公子","callTime":"2017-05-03 22:48:18","comment":"态度可以"},{"callUser":"李公子","callTime":"2017-05-03 22:48","comment":"态度可以"},{"callUser":"李公子","callTime":"2017-05-03 22:48","comment":"态度可以"}]
         * OverdueData : {"overdueReason":"手头有点紧","overdueComment":"老套路"}
         */

        private OverdueDataBean OverdueData;
        private List<CallRecordDataBean> CallRecordData;

        public OverdueDataBean getOverdueData() {
            return OverdueData;
        }

        public void setOverdueData(OverdueDataBean OverdueData) {
            this.OverdueData = OverdueData;
        }

        public List<CallRecordDataBean> getCallRecordData() {
            return CallRecordData;
        }

        public void setCallRecordData(List<CallRecordDataBean> CallRecordData) {
            this.CallRecordData = CallRecordData;
        }

        public static class OverdueDataBean {
            /**
             * overdueReason : 手头有点紧
             * overdueComment : 老套路
             */

            private String overdueReason;
            private String overdueComment;

            public String getOverdueReason() {
                return overdueReason;
            }

            public void setOverdueReason(String overdueReason) {
                this.overdueReason = overdueReason;
            }

            public String getOverdueComment() {
                return overdueComment;
            }

            public void setOverdueComment(String overdueComment) {
                this.overdueComment = overdueComment;
            }
        }

        public static class CallRecordDataBean {
            /**
             * callUser : 李公子
             * callTime : 2017-05-03 22:48:18
             * comment : 态度可以
             */

            private String callUser;
            private String callTime;
            private String comment;

            public String getCallUser() {
                return callUser;
            }

            public void setCallUser(String callUser) {
                this.callUser = callUser;
            }

            public String getCallTime() {
                return callTime;
            }

            public void setCallTime(String callTime) {
                this.callTime = callTime;
            }

            public String getComment() {
                return comment;
            }

            public void setComment(String comment) {
                this.comment = comment;
            }
        }
    }
}
