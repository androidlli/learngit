package com.cango.palmcartreasure.model;

import com.cango.palmcartreasure.util.CommUtil;

import java.util.List;

/**
 * Created by cango on 2017/5/11.
 */

public class GroupList {

    /**
     * Success : true
     * Msg : 操作成功
     * Code : 0
     * Data : {"groupList":[{"groupid":1,"orgID":1,"groupName":"分组1","groupLeader":"李公子","groupLeaderID":1,"userList":[{"userid":1,"groupid":1,"userName":"李公子"},{"userid":2,"groupid":1,"userName":"洪公子"}]},{"groupid":2,"orgID":1,"groupName":"分组2","groupLeader":"张公子","groupLeaderID":3,"userList":[{"userid":3,"groupid":1,"userName":"张公子"},{"userid":4,"groupid":1,"userName":"温公子"}]}]}
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
        private List<GroupListBean> groupList;

        public List<GroupListBean> getGroupList() {
            return groupList;
        }

        public void setGroupList(List<GroupListBean> groupList) {
            this.groupList = groupList;
        }

        public static class GroupListBean {
            /**
             * groupid : 1
             * orgID : 1
             * groupName : 分组1
             * groupLeader : 李公子
             * groupLeaderID : 1
             * userList : [{"userid":1,"groupid":1,"userName":"李公子"},{"userid":2,"groupid":1,"userName":"洪公子"}]
             */

            private int groupid;
            private int orgID;
            private String groupName;
            private String groupLeader;
            private int groupLeaderID;

            public String getAction() {
                return action;
            }

            public void setAction(String action) {
                this.action = action;
            }

            //test
            private String action;

            public boolean isSelected() {
                return isSelected;
            }

            public void setSelected(boolean selected) {
                isSelected = selected;
            }

            //增加这个为了给租分配任务的时候有点中效果
            private boolean isSelected;
            private List<UserListBean> userList;

            public int getGroupid() {
                return groupid;
            }

            public void setGroupid(int groupid) {
                this.groupid = groupid;
            }

            public int getOrgID() {
                return orgID;
            }

            public void setOrgID(int orgID) {
                this.orgID = orgID;
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

            public int getGroupLeaderID() {
                return groupLeaderID;
            }

            public void setGroupLeaderID(int groupLeaderID) {
                this.groupLeaderID = groupLeaderID;
            }

            public List<UserListBean> getUserList() {
                return userList;
            }

            public void setUserList(List<UserListBean> userList) {
                this.userList = userList;
            }

            public static class UserListBean {
                /**
                 * userid : 1
                 * groupid : 1
                 * userName : 李公子
                 */

                private int userid;
                private int groupid;
                private String userName;

                public boolean isSelected() {
                    return isSelected;
                }

                public void setSelected(boolean selected) {
                    isSelected = selected;
                }

                private boolean isSelected;

                public int getUserid() {
                    return userid;
                }

                public void setUserid(int userid) {
                    this.userid = userid;
                }

                public int getGroupid() {
                    return groupid;
                }

                public void setGroupid(int groupid) {
                    this.groupid = groupid;
                }

                public String getUserName() {
                    return userName;
                }

                public void setUserName(String userName) {
                    if (CommUtil.checkIsNull(userName))
                        userName="";
                    this.userName = userName;
                }
            }
        }
    }
}
