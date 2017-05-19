package com.cango.palmcartreasure.model;

import com.cango.palmcartreasure.util.CommUtil;

import java.util.List;

/**
 * Created by cango on 2017/5/18.
 */

public class WareHouse {

    /**
     * Success : true
     * Msg : 操作成功
     * Code : 0
     * Data : [{"warehouseName":"上海⑥号库","resultLAT":129.121,"resultLON":39.0987,"warehouseAdd":"上海浦东新区","contactPerson":"李经理","mobilePhone1":"13178967898","mobilePhone2":"13178967898","mobilePhone3":"13178967898"}]
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
         * warehouseName : 上海⑥号库
         * resultLAT : 129.121
         * resultLON : 39.0987
         * warehouseAdd : 上海浦东新区
         * contactPerson : 李经理
         * mobilePhone1 : 13178967898
         * mobilePhone2 : 13178967898
         * mobilePhone3 : 13178967898
         */

        private String warehouseName;
        private float resultLAT;
        private float resultLON;
        private String warehouseAdd;
        private String contactPerson;
        private String mobilePhone1;
        private String mobilePhone2;
        private String mobilePhone3;

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            if (CommUtil.checkIsNull(distance))
                distance="";
            this.distance = distance;
        }

        private String distance;

        public String getWarehouseName() {
            return warehouseName;
        }

        public void setWarehouseName(String warehouseName) {
            if (CommUtil.checkIsNull(warehouseName))
                warehouseName="";
            this.warehouseName = warehouseName;
        }

        public float getResultLAT() {
            return resultLAT;
        }

        public void setResultLAT(float resultLAT) {
            this.resultLAT = resultLAT;
        }

        public float getResultLON() {
            return resultLON;
        }

        public void setResultLON(float resultLON) {
            this.resultLON = resultLON;
        }

        public String getWarehouseAdd() {
            return warehouseAdd;
        }

        public void setWarehouseAdd(String warehouseAdd) {
            if (CommUtil.checkIsNull(warehouseAdd))
                warehouseAdd="";
            this.warehouseAdd = warehouseAdd;
        }

        public String getContactPerson() {
            return contactPerson;
        }

        public void setContactPerson(String contactPerson) {
            if (CommUtil.checkIsNull(contactPerson))
                contactPerson="";
            this.contactPerson = contactPerson;
        }

        public String getMobilePhone1() {
            return mobilePhone1;
        }

        public void setMobilePhone1(String mobilePhone1) {
            if (CommUtil.checkIsNull(mobilePhone1))
                mobilePhone1="";
            this.mobilePhone1 = mobilePhone1;
        }

        public String getMobilePhone2() {
            return mobilePhone2;
        }

        public void setMobilePhone2(String mobilePhone2) {
            if (CommUtil.checkIsNull(mobilePhone2))
                mobilePhone2="";
            this.mobilePhone2 = mobilePhone2;
        }

        public String getMobilePhone3() {
            return mobilePhone3;
        }

        public void setMobilePhone3(String mobilePhone3) {
            if (CommUtil.checkIsNull(mobilePhone3))
                mobilePhone3="";
            this.mobilePhone3 = mobilePhone3;
        }
    }
}
