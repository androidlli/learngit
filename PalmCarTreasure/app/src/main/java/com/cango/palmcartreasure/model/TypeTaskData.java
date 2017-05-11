package com.cango.palmcartreasure.model;

import com.cango.palmcartreasure.util.CommUtil;

import java.util.List;

/**
 * Created by cango on 2017/4/24.
 */

public class TypeTaskData {

    /**
     * Success : true
     * Msg : 操作成功
     * Code : 0
     * Data : [{"AGENCYID":101,"CASEID":101,"APPLYID":101,"APPLYCD":"1010001","CUSTOMERNAME":"测试用户","CARPLATENO":"豫A5021","SHORTNAME":"璀璨金山","DISTANCE":"12000","AGENCYSTATUS":"4","FLOWSTAUTS":"30","ISSTART":"F","ISCHECKPOINT":"F","ISDONE":"F"},{"AGENCYID":102,"CASEID":102,"APPLYID":102,"APPLYCD":"1010002","CUSTOMERNAME":"测试用户2","CARPLATENO":"豫A5020","SHORTNAME":"璀璨金山2","DISTANCE":"16008","AGENCYSTATUS":"4","FLOWSTAUTS":"30","ISSTART":"F","ISCHECKPOINT":"F","ISDONE":"F"}]
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
        if (CommUtil.checkIsNull(Msg))
            Msg="";
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
         * AGENCYID : 101
         * CASEID : 101
         * APPLYID : 101
         * APPLYCD : 1010001
         * CUSTOMERNAME : 测试用户
         * CARPLATENO : 豫A5021
         * SHORTNAME : 璀璨金山
         * DISTANCE : 12000
         * AGENCYSTATUS : 4
         * FLOWSTAUTS : 30
         * ISSTART : F
         * ISCHECKPOINT : F
         * ISDONE : F
         */

        private int AGENCYID;
        private int CASEID;
        private int APPLYID;
        private String APPLYCD;
        private String CUSTOMERNAME;
        private String CARPLATENO;
        private String SHORTNAME;
        private String DISTANCE;
        private String AGENCYSTATUS;
        private String FLOWSTAUTS;
        private String ISSTART;
        private String ISCHECKPOINT;
        private String ISDONE;

        public int getAGENCYID() {
            return AGENCYID;
        }

        public void setAGENCYID(int AGENCYID) {
            this.AGENCYID = AGENCYID;
        }

        public int getCASEID() {
            return CASEID;
        }

        public void setCASEID(int CASEID) {
            this.CASEID = CASEID;
        }

        public int getAPPLYID() {
            return APPLYID;
        }

        public void setAPPLYID(int APPLYID) {
            this.APPLYID = APPLYID;
        }

        public String getAPPLYCD() {
            return APPLYCD;
        }

        public void setAPPLYCD(String APPLYCD) {
            if (CommUtil.checkIsNull(APPLYCD))
                APPLYCD="";
            this.APPLYCD = APPLYCD;
        }

        public String getCUSTOMERNAME() {
            return CUSTOMERNAME;
        }

        public void setCUSTOMERNAME(String CUSTOMERNAME) {
            if (CommUtil.checkIsNull(CUSTOMERNAME))
                CUSTOMERNAME="";
            this.CUSTOMERNAME = CUSTOMERNAME;
        }

        public String getCARPLATENO() {
            return CARPLATENO;
        }

        public void setCARPLATENO(String CARPLATENO) {
            if (CommUtil.checkIsNull(CARPLATENO))
                CARPLATENO="";
            this.CARPLATENO = CARPLATENO;
        }

        public String getSHORTNAME() {
            return SHORTNAME;
        }

        public void setSHORTNAME(String SHORTNAME) {
            if (CommUtil.checkIsNull(SHORTNAME))
                SHORTNAME="";
            this.SHORTNAME = SHORTNAME;
        }

        public String getDISTANCE() {
            return DISTANCE;
        }

        public void setDISTANCE(String DISTANCE) {
            if (CommUtil.checkIsNull(DISTANCE))
                DISTANCE="";
            this.DISTANCE = DISTANCE;
        }

        public String getAGENCYSTATUS() {
            return AGENCYSTATUS;
        }

        public void setAGENCYSTATUS(String AGENCYSTATUS) {
            if (CommUtil.checkIsNull(AGENCYSTATUS))
                AGENCYSTATUS="";
            this.AGENCYSTATUS = AGENCYSTATUS;
        }

        public String getFLOWSTAUTS() {
            return FLOWSTAUTS;
        }

        public void setFLOWSTAUTS(String FLOWSTAUTS) {
            if (CommUtil.checkIsNull(FLOWSTAUTS))
                FLOWSTAUTS="";
            this.FLOWSTAUTS = FLOWSTAUTS;
        }

        public String getISSTART() {
            return ISSTART;
        }

        public void setISSTART(String ISSTART) {
            if (CommUtil.checkIsNull(ISSTART))
                ISSTART="";
            this.ISSTART = ISSTART;
        }

        public String getISCHECKPOINT() {
            return ISCHECKPOINT;
        }

        public void setISCHECKPOINT(String ISCHECKPOINT) {
            if (CommUtil.checkIsNull(ISCHECKPOINT))
                ISCHECKPOINT="";
            this.ISCHECKPOINT = ISCHECKPOINT;
        }

        public String getISDONE() {
            return ISDONE;
        }

        public void setISDONE(String ISDONE) {
            if (CommUtil.checkIsNull(ISDONE))
                ISDONE="";
            this.ISDONE = ISDONE;
        }
    }
}
