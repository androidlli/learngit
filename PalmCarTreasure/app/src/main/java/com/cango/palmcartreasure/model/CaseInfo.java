package com.cango.palmcartreasure.model;

import com.cango.palmcartreasure.util.CommUtil;

/**
 * Created by cango on 2017/5/17.
 */

public class CaseInfo {

    /**
     * Success : true
     * Msg : 操作成功
     * Code : 0
     * Data : {"applycd":"FWZ00115016356","applytime":"2015-05-16T01:57:39","carbrandcg":"东风风行","carmodelidcg":"景逸X32015款 1.5L 豪华型","hasgpsflg":"已安装","isgpsonline":"在线","IMEI1":"865067020216571","IMEI2":"865067020192038"}
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
         * applycd : FWZ00115016356
         * applytime : 2015-05-16T01:57:39
         * carbrandcg : 东风风行
         * carmodelidcg : 景逸X32015款 1.5L 豪华型
         * hasgpsflg : 已安装
         * isgpsonline : 在线
         * IMEI1 : 865067020216571
         * IMEI2 : 865067020192038
         */

        private String applycd;
        private String applytime;
        private String carbrandcg;
        private String carmodelidcg;
        private String hasgpsflg;
        private String isgpsonline;
        private String IMEI1;
        private String IMEI2;

        public String getApplycd() {
            return applycd;
        }

        public void setApplycd(String applycd) {
            this.applycd = applycd;
        }

        public String getApplytime() {
            return applytime;
        }

        public void setApplytime(String applytime) {
            if (CommUtil.checkIsNull(applytime))
                applytime="";
            this.applytime = applytime;
        }

        public String getCarbrandcg() {
            return carbrandcg;
        }

        public void setCarbrandcg(String carbrandcg) {
            if (CommUtil.checkIsNull(carbrandcg))
                carbrandcg="";
            this.carbrandcg = carbrandcg;
        }

        public String getCarmodelidcg() {
            return carmodelidcg;
        }

        public void setCarmodelidcg(String carmodelidcg) {
            if (CommUtil.checkIsNull(carmodelidcg))
                carmodelidcg="";
            this.carmodelidcg = carmodelidcg;
        }

        public String getHasgpsflg() {
            return hasgpsflg;
        }

        public void setHasgpsflg(String hasgpsflg) {
            if (CommUtil.checkIsNull(hasgpsflg))
                hasgpsflg="";
            this.hasgpsflg = hasgpsflg;
        }

        public String getIsgpsonline() {
            return isgpsonline;
        }

        public void setIsgpsonline(String isgpsonline) {
            if (CommUtil.checkIsNull(isgpsonline))
                isgpsonline="";
            this.isgpsonline = isgpsonline;
        }

        public String getIMEI1() {
            return IMEI1;
        }

        public void setIMEI1(String IMEI1) {
            if (CommUtil.checkIsNull(IMEI1))
                IMEI1="";
            this.IMEI1 = IMEI1;
        }

        public String getIMEI2() {
            return IMEI2;
        }

        public void setIMEI2(String IMEI2) {
            if (CommUtil.checkIsNull(IMEI2))
                IMEI2="";
            this.IMEI2 = IMEI2;
        }
    }
}
