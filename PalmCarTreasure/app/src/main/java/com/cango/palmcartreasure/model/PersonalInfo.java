package com.cango.palmcartreasure.model;

/**
 * Created by cango on 2017/5/15.
 */

public class PersonalInfo {

    /**
     * Success : true
     * Msg : 操作成功
     * Code : 0
     * Data : {"userid":1,"username":"李公子","gender":"M","certtype":"ID","certno":"410522198709019876","mobile":"15677909876"}
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
         * userid : 1
         * username : 李公子
         * gender : M
         * certtype : ID
         * certno : 410522198709019876
         * mobile : 15677909876
         */

        private int userid;
        private String username;
        private String gender;
        private String certtype;
        private String certno;
        private String mobile;

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getCerttype() {
            return certtype;
        }

        public void setCerttype(String certtype) {
            this.certtype = certtype;
        }

        public String getCertno() {
            return certno;
        }

        public void setCertno(String certno) {
            this.certno = certno;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }
}
