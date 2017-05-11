package com.cango.palmcartreasure.model;

import android.text.TextUtils;

/**
 * Created by cango on 2017/4/18.
 */

public class LoginData {

    /**
     * Success : true
     * Msg : 登录成功
     * Code : 0
     * Data : {"Token":"E8E4FFEB81A7A3ECCFCBA51690CA62593282A29CA0952EA85AEF1F1780ABA66A4F0F6243AAB052192C0CD878CC988581B6DF5AFF032120569431EDBBBACB5E44","User":{"USERID":6202,"USERROLEID":1031}}
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
         * Token : E8E4FFEB81A7A3ECCFCBA51690CA62593282A29CA0952EA85AEF1F1780ABA66A4F0F6243AAB052192C0CD878CC988581B6DF5AFF032120569431EDBBBACB5E44
         * User : {"USERID":6202,"USERROLEID":1031}
         */

        private String Token;
        private UserBean User;

        public String getToken() {
            return Token;
        }

        public void setToken(String Token) {
            this.Token = Token;
        }

        public UserBean getUser() {
            return User;
        }

        public void setUser(UserBean User) {
            this.User = User;
        }

        public static class UserBean {
            /**
             * USERID : 6202
             * USERROLEID : 1031
             */

            private int USERID;
            private int USERROLEID;

            public int getUSERID() {
                return USERID;
            }

            public void setUSERID(int USERID) {
                this.USERID = USERID;
            }

            public int getUSERROLEID() {
                return USERROLEID;
            }

            public void setUSERROLEID(int USERROLEID) {
                this.USERROLEID = USERROLEID;
            }
        }
    }
}
