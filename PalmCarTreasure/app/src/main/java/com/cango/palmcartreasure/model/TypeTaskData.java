package com.cango.palmcartreasure.model;

import android.os.Parcel;
import android.os.Parcelable;

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
     * Data : {"taskList":[{"agencyID":1419,"caseID":51599,"applyID":25536,"applyCD":"BWX00215075516","customerName":"邓碧强","carPlateNO":"苏FL205R","shortName":"皖新租赁","agencyStatus":"拖车中","flowStauts":"已分配","distance":null,"isStart":"T","isCheckPoint":"F","isDone":"F"},{"agencyID":847,"caseID":26875,"applyID":3521,"applyCD":"FWZ00114123518","customerName":"纪万记","carPlateNO":"皖AB5238","shortName":"上海网金","agencyStatus":"拖车中","flowStauts":"已分配","distance":null,"isStart":"T","isCheckPoint":"F","isDone":"F"},{"agencyID":1414,"caseID":48673,"applyID":29059,"applyCD":"BWX00115089037","customerName":"王玉春","carPlateNO":"鄂HF6952","shortName":"皖新租赁","agencyStatus":"拖车中","flowStauts":"已分配","distance":null,"isStart":"T","isCheckPoint":"F","isDone":"F"},{"agencyID":96,"caseID":17009,"applyID":6363,"applyCD":"FWZ00115016356","customerName":"吴美玲","carPlateNO":"川AQX713","shortName":"上海网金","agencyStatus":"拖车中","flowStauts":"已分配","distance":null,"isStart":"T","isCheckPoint":"F","isDone":"F"},{"agencyID":1185,"caseID":45719,"applyID":35578,"applyCD":"BWX53115105553","customerName":"李德良","carPlateNO":"鲁AH8192","shortName":"皖新租赁","agencyStatus":"拖车中","flowStauts":"已分配","distance":null,"isStart":"T","isCheckPoint":"F","isDone":"F"},{"agencyID":1425,"caseID":33543,"applyID":22682,"applyCD":"FBY00115062664","customerName":"占必远","carPlateNO":"0","shortName":"皖新租赁","agencyStatus":"拖车中","flowStauts":"已分配","distance":null,"isStart":"T","isCheckPoint":"F","isDone":"F"},{"agencyID":990,"caseID":34317,"applyID":27082,"applyCD":"BWX00115087060","customerName":"李晓霞","carPlateNO":"冀AG2Z18","shortName":"皖新租赁","agencyStatus":"拖车中","flowStauts":"已分配","distance":null,"isStart":"T","isCheckPoint":"F","isDone":"F"},{"agencyID":47,"caseID":2177,"applyID":7977,"applyCD":"FBY00115017970","customerName":"肖杨","carPlateNO":"苏AN8P91","shortName":"上海网金","agencyStatus":"拖车中","flowStauts":"已分配","distance":null,"isStart":"T","isCheckPoint":"F","isDone":"F"},{"agencyID":522,"caseID":23407,"applyID":10850,"applyCD":"FBY00115020840","customerName":"米春波","carPlateNO":"桂A9G234","shortName":"上海网金","agencyStatus":"拖车中","flowStauts":"已分配","distance":null,"isStart":"T","isCheckPoint":"F","isDone":"F"},{"agencyID":1019,"caseID":23408,"applyID":12593,"applyCD":"FBY00115032583","customerName":"晏红兵","carPlateNO":"云AU9R88","shortName":"上海网金","agencyStatus":"拖车中","flowStauts":"已分配","distance":null,"isStart":"T","isCheckPoint":"F","isDone":"F"}],"nextPage":0}
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
         * taskList : [{"agencyID":1419,"caseID":51599,"applyID":25536,"applyCD":"BWX00215075516","customerName":"邓碧强","carPlateNO":"苏FL205R","shortName":"皖新租赁","agencyStatus":"拖车中","flowStauts":"已分配","distance":null,"isStart":"T","isCheckPoint":"F","isDone":"F"},{"agencyID":847,"caseID":26875,"applyID":3521,"applyCD":"FWZ00114123518","customerName":"纪万记","carPlateNO":"皖AB5238","shortName":"上海网金","agencyStatus":"拖车中","flowStauts":"已分配","distance":null,"isStart":"T","isCheckPoint":"F","isDone":"F"},{"agencyID":1414,"caseID":48673,"applyID":29059,"applyCD":"BWX00115089037","customerName":"王玉春","carPlateNO":"鄂HF6952","shortName":"皖新租赁","agencyStatus":"拖车中","flowStauts":"已分配","distance":null,"isStart":"T","isCheckPoint":"F","isDone":"F"},{"agencyID":96,"caseID":17009,"applyID":6363,"applyCD":"FWZ00115016356","customerName":"吴美玲","carPlateNO":"川AQX713","shortName":"上海网金","agencyStatus":"拖车中","flowStauts":"已分配","distance":null,"isStart":"T","isCheckPoint":"F","isDone":"F"},{"agencyID":1185,"caseID":45719,"applyID":35578,"applyCD":"BWX53115105553","customerName":"李德良","carPlateNO":"鲁AH8192","shortName":"皖新租赁","agencyStatus":"拖车中","flowStauts":"已分配","distance":null,"isStart":"T","isCheckPoint":"F","isDone":"F"},{"agencyID":1425,"caseID":33543,"applyID":22682,"applyCD":"FBY00115062664","customerName":"占必远","carPlateNO":"0","shortName":"皖新租赁","agencyStatus":"拖车中","flowStauts":"已分配","distance":null,"isStart":"T","isCheckPoint":"F","isDone":"F"},{"agencyID":990,"caseID":34317,"applyID":27082,"applyCD":"BWX00115087060","customerName":"李晓霞","carPlateNO":"冀AG2Z18","shortName":"皖新租赁","agencyStatus":"拖车中","flowStauts":"已分配","distance":null,"isStart":"T","isCheckPoint":"F","isDone":"F"},{"agencyID":47,"caseID":2177,"applyID":7977,"applyCD":"FBY00115017970","customerName":"肖杨","carPlateNO":"苏AN8P91","shortName":"上海网金","agencyStatus":"拖车中","flowStauts":"已分配","distance":null,"isStart":"T","isCheckPoint":"F","isDone":"F"},{"agencyID":522,"caseID":23407,"applyID":10850,"applyCD":"FBY00115020840","customerName":"米春波","carPlateNO":"桂A9G234","shortName":"上海网金","agencyStatus":"拖车中","flowStauts":"已分配","distance":null,"isStart":"T","isCheckPoint":"F","isDone":"F"},{"agencyID":1019,"caseID":23408,"applyID":12593,"applyCD":"FBY00115032583","customerName":"晏红兵","carPlateNO":"云AU9R88","shortName":"上海网金","agencyStatus":"拖车中","flowStauts":"已分配","distance":null,"isStart":"T","isCheckPoint":"F","isDone":"F"}]
         * nextPage : 0
         */

        private int nextPage;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        private int totalCount;
        private List<TaskListBean> taskList;

        public int getNextPage() {
            return nextPage;
        }

        public void setNextPage(int nextPage) {
            this.nextPage = nextPage;
        }

        public List<TaskListBean> getTaskList() {
            return taskList;
        }

        public void setTaskList(List<TaskListBean> taskList) {
            this.taskList = taskList;
        }

        public static class TaskListBean implements Parcelable {
            /**
             * agencyID : 1419
             * caseID : 51599
             * applyID : 25536
             * applyCD : BWX00215075516
             * customerName : 邓碧强
             * carPlateNO : 苏FL205R
             * shortName : 皖新租赁
             * agencyStatus : 拖车中
             * flowStauts : 已分配
             * distance : null
             * isStart : T
             * isCheckPoint : F
             * isDone : F
             */

            private int agencyID;
            private int caseID;
            private int applyID;
            private String applyCD;
            private String customerName;
            private String carPlateNO;
            private String shortName;
            private String agencyStatus;
            private String flowStauts;
            private String distance;
            private String isStart;
            private String isCheckPoint;
            private String isDone;

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

            public String getAgencyStatus() {
                return agencyStatus;
            }

            public void setAgencyStatus(String agencyStatus) {
                if (CommUtil.checkIsNull(agencyStatus))
                    agencyStatus="";
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

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                if (CommUtil.checkIsNull(distance))
                    distance="";
                this.distance = distance;
            }

            public String getIsStart() {
                return isStart;
            }

            public void setIsStart(String isStart) {
                if (CommUtil.checkIsNull(isStart))
                    isStart="";
                this.isStart = isStart;
            }

            public String getIsCheckPoint() {
                return isCheckPoint;
            }

            public void setIsCheckPoint(String isCheckPoint) {
                if (CommUtil.checkIsNull(isCheckPoint))
                    isCheckPoint="";
                this.isCheckPoint = isCheckPoint;
            }

            public String getIsDone() {
                return isDone;
            }

            public void setIsDone(String isDone) {
                if (CommUtil.checkIsNull(isDone))
                    isDone="";
                this.isDone = isDone;
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
                dest.writeString(this.agencyStatus);
                dest.writeString(this.flowStauts);
                dest.writeString(this.distance);
                dest.writeString(this.isStart);
                dest.writeString(this.isCheckPoint);
                dest.writeString(this.isDone);
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
                this.agencyStatus = in.readString();
                this.flowStauts = in.readString();
                this.distance = in.readString();
                this.isStart = in.readString();
                this.isCheckPoint = in.readString();
                this.isDone = in.readString();
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
