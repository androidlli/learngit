package com.cango.palmcartreasure.model;

import com.cango.palmcartreasure.util.CommUtil;

import java.util.List;

/**
 * Created by cango on 2017/5/9.
 */

public class GroupTaskCount {

    /**
     * Success : true
     * Msg : 操作成功
     * Code : 0
     * Data : {"taskCountList":[{"groupid":1,"groupName":"分组1","groupLeader":"李公子","newTaskCount":20,"taskInProgressCount":15,"taskDoneCount":100},{"groupid":2,"groupName":"分组2","groupLeader":"张公子","newTaskCount":20,"taskInProgressCount":15,"taskDoneCount":100}]}
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
        private List<TaskCountListBean> taskCountList;

        public List<TaskCountListBean> getTaskCountList() {
            return taskCountList;
        }

        public void setTaskCountList(List<TaskCountListBean> taskCountList) {
            this.taskCountList = taskCountList;
        }

        public static class TaskCountListBean {
            /**
             * groupid : 1
             * groupName : 分组1
             * groupLeader : 李公子
             * newTaskCount : 20
             * taskInProgressCount : 15
             * taskDoneCount : 100
             */

            private int groupid;
            private String groupName;
            private String groupLeader;
            private int newTaskCount;
            private int taskInProgressCount;
            private int taskDoneCount;

            public boolean isChecked() {
                return isChecked;
            }

            public void setChecked(boolean checked) {
                isChecked = checked;
            }

            private boolean isChecked;

            public int getGroupid() {
                return groupid;
            }

            public void setGroupid(int groupid) {
                this.groupid = groupid;
            }

            public String getGroupName() {
                return groupName;
            }

            public void setGroupName(String groupName) {
                if (CommUtil.checkIsNull(groupName))
                    groupName="";
                this.groupName = groupName;
            }

            public String getGroupLeader() {
                return groupLeader;
            }

            public void setGroupLeader(String groupLeader) {
                if (CommUtil.checkIsNull(groupLeader))
                    groupLeader="";
                this.groupLeader = groupLeader;
            }

            public int getNewTaskCount() {
                return newTaskCount;
            }

            public void setNewTaskCount(int newTaskCount) {
                this.newTaskCount = newTaskCount;
            }

            public int getTaskInProgressCount() {
                return taskInProgressCount;
            }

            public void setTaskInProgressCount(int taskInProgressCount) {
                this.taskInProgressCount = taskInProgressCount;
            }

            public int getTaskDoneCount() {
                return taskDoneCount;
            }

            public void setTaskDoneCount(int taskDoneCount) {
                this.taskDoneCount = taskDoneCount;
            }
        }
    }
}
