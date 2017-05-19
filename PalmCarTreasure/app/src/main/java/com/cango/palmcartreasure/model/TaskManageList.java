package com.cango.palmcartreasure.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.cango.palmcartreasure.util.CommUtil;

import java.util.List;

/**
 * Created by cango on 2017/5/9.
 */

public class TaskManageList {

    /**
     * Success : true
     * Msg : 操作成功
     * Code : 0
     * Data : {"taskList":[{"agencyID":101,"caseID":101,"applyID":101,"applyCD":"1010001","customerName":"测试用户","carPlateNO":"豫A5021","shortName":"璀璨金山","distance":"12000","agencyStatus":"4","flowStauts":"10","feerate":1.01},{"agencyID":102,"caseID":102,"applyID":102,"applyCD":"1010002","customerName":"测试用户2","carPlateNO":"豫A5020","shortName":"璀璨金山2","distance":"16008","agencyStatus":"4","flowStauts":"10","feerate":1.01}]}
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
        private List<TaskListBean> taskList;

        public List<TaskListBean> getTaskList() {
            return taskList;
        }

        public void setTaskList(List<TaskListBean> taskList) {
            this.taskList = taskList;
        }

        public static class TaskListBean implements Parcelable {
            /**
             * agencyID : 101
             * caseID : 101
             * applyID : 101
             * applyCD : 1010001
             * customerName : 测试用户
             * carPlateNO : 豫A5021
             * shortName : 璀璨金山
             * distance : 12000
             * agencyStatus : 4
             * flowStauts : 10
             * feerate : 1.01
             */

            private int agencyID;
            private int caseID;
            private int applyID;
            private String applyCD;
            private String customerName;
            private String carPlateNO;
            private String shortName;
            private String distance;
            private String agencyStatus;
            private String flowStauts;
            private double feerate;
            private int redueDays;
            private double redueAmount;

            public int getRedueDays() {
                return redueDays;
            }

            public void setRedueDays(int redueDays) {
                this.redueDays = redueDays;
            }

            public double getRedueAmount() {
                return redueAmount;
            }

            public void setRedueAmount(double redueAmount) {
                this.redueAmount = redueAmount;
            }

            public boolean isChecked() {
                return isChecked;
            }

            public void setChecked(boolean checked) {
                isChecked = checked;
            }

            private boolean isChecked;

            public double getAgencyAmount() {
                return agencyAmount;
            }

            public void setAgencyAmount(double agencyAmount) {
                this.agencyAmount = agencyAmount;
            }

            private double agencyAmount;

            public int getAgencyID() {
                return agencyID;
            }

            public void setAgencyID(int agencyID) {
                this.agencyID = agencyID;
            }

            public int getCaseID() {
                return caseID;
            }

            public void setCaseID(int caseID) {
                this.caseID = caseID;
            }

            public int getApplyID() {
                return applyID;
            }

            public void setApplyID(int applyID) {
                this.applyID = applyID;
            }

            public String getApplyCD() {
                return applyCD;
            }

            public void setApplyCD(String applyCD) {
                if (CommUtil.checkIsNull(applyCD))
                    applyCD="";
                this.applyCD = applyCD;
            }

            public String getCustomerName() {
                return customerName;
            }

            public void setCustomerName(String customerName) {
                if (CommUtil.checkIsNull(customerName))
                    customerName="";
                this.customerName = customerName;
            }

            public String getCarPlateNO() {
                return carPlateNO;
            }

            public void setCarPlateNO(String carPlateNO) {
                this.carPlateNO = carPlateNO;
            }

            public String getShortName() {
                return shortName;
            }

            public void setShortName(String shortName) {
                if (CommUtil.checkIsNull(shortName))
                    shortName="";
                this.shortName = shortName;
            }

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                if (CommUtil.checkIsNull(distance))
                    distance="";
                this.distance = distance;
            }

            public String getAgencyStatus() {
                return agencyStatus;
            }

            public void setAgencyStatus(String agencyStatus) {
                this.agencyStatus = agencyStatus;
            }

            public String getFlowStauts() {
                return flowStauts;
            }

            public void setFlowStauts(String flowStauts) {
                if (CommUtil.checkIsNull(flowStauts))
                    flowStauts="";
                this.flowStauts = flowStauts;
            }

            public double getFeerate() {
                return feerate;
            }

            public void setFeerate(double feerate) {
                this.feerate = feerate;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.agencyID);
                dest.writeInt(this.caseID);
                dest.writeInt(this.applyID);
                dest.writeString(this.applyCD);
                dest.writeString(this.customerName);
                dest.writeString(this.carPlateNO);
                dest.writeString(this.shortName);
                dest.writeString(this.distance);
                dest.writeString(this.agencyStatus);
                dest.writeString(this.flowStauts);
                dest.writeDouble(this.feerate);
                dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
                dest.writeDouble(this.agencyAmount);
            }

            public TaskListBean() {
            }

            protected TaskListBean(Parcel in) {
                this.agencyID = in.readInt();
                this.caseID = in.readInt();
                this.applyID = in.readInt();
                this.applyCD = in.readString();
                this.customerName = in.readString();
                this.carPlateNO = in.readString();
                this.shortName = in.readString();
                this.distance = in.readString();
                this.agencyStatus = in.readString();
                this.flowStauts = in.readString();
                this.feerate = in.readDouble();
                this.isChecked = in.readByte() != 0;
                this.agencyAmount = in.readDouble();
            }

            public static final Parcelable.Creator<TaskListBean> CREATOR = new Parcelable.Creator<TaskListBean>() {
                @Override
                public TaskListBean createFromParcel(Parcel source) {
                    return new TaskListBean(source);
                }

                @Override
                public TaskListBean[] newArray(int size) {
                    return new TaskListBean[size];
                }
            };
        }
    }
}
