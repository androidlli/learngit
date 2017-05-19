package com.cango.palmcartreasure.model;

import com.cango.palmcartreasure.util.CommUtil;

import java.util.List;

/**
 * Created by cango on 2017/5/17.
 */

public class HomeVisitRecord {

    /**
     * Success : true
     * Msg : 操作成功
     * Code : 0
     * Data : [{"visitStatus":"已审批","visitTime":"2017-55-03 22:55","visitUser":"李公子","visitAciton":"工作单位调查","recommendActions":"现场跟进","redueTrueReason":"手头紧","applyUser":"张公子"},{"visitStatus":"已审批","visitTime":"2017-55-03 22:55","visitUser":"李公子","visitAciton":"工作单位调查","recommendActions":"现场跟进","redueTrueReason":"手头紧","applyUser":"张公子"},{"visitStatus":"已审批","visitTime":"2017-55-03 22:55","visitUser":"李公子","visitAciton":"工作单位调查","recommendActions":"现场跟进","redueTrueReason":"手头紧","applyUser":"张公子"}]
     */

    private boolean Success;
    private String Msg;
    private int Code;
    private List<DataBean> Data;

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

    public List<DataBean> getData() {
        return Data;
    }

    public void setData(List<DataBean> Data) {
        this.Data = Data;
    }

    public static class DataBean {
        /**
         * visitStatus : 已审批
         * visitTime : 2017-55-03 22:55
         * visitUser : 李公子
         * visitAciton : 工作单位调查
         * recommendActions : 现场跟进
         * redueTrueReason : 手头紧
         * applyUser : 张公子
         */

        private String visitStatus;
        private String visitTime;
        private String visitUser;
        private String visitAciton;
        private String recommendActions;
        private String redueTrueReason;
        private String applyUser;

        public String getVisitStatus() {
            return visitStatus;
        }

        public void setVisitStatus(String visitStatus) {
            if (CommUtil.checkIsNull(visitStatus))
                visitStatus="";
            this.visitStatus = visitStatus;
        }

        public String getVisitTime() {
            return visitTime;
        }

        public void setVisitTime(String visitTime) {
            if (CommUtil.checkIsNull(visitTime))
                visitTime="";
            this.visitTime = visitTime;
        }

        public String getVisitUser() {
            return visitUser;
        }

        public void setVisitUser(String visitUser) {
            if (CommUtil.checkIsNull(visitUser))
                visitUser="";
            this.visitUser = visitUser;
        }

        public String getVisitAciton() {
            return visitAciton;
        }

        public void setVisitAciton(String visitAciton) {
            if (CommUtil.checkIsNull(visitAciton))
                visitAciton="";
            this.visitAciton = visitAciton;
        }

        public String getRecommendActions() {
            return recommendActions;
        }

        public void setRecommendActions(String recommendActions) {
            if (CommUtil.checkIsNull(recommendActions))
                recommendActions="";
            this.recommendActions = recommendActions;
        }

        public String getRedueTrueReason() {
            return redueTrueReason;
        }

        public void setRedueTrueReason(String redueTrueReason) {
            if (CommUtil.checkIsNull(redueTrueReason))
                redueTrueReason="";
            this.redueTrueReason = redueTrueReason;
        }

        public String getApplyUser() {
            return applyUser;
        }

        public void setApplyUser(String applyUser) {
            if (CommUtil.checkIsNull(applyUser))
                applyUser="";
            this.applyUser = applyUser;
        }
    }
}
