package com.cango.palmcartreasure.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.cango.palmcartreasure.util.CommUtil;

import java.util.List;

/**
 * Created by cango on 2017/5/15.
 */

public class MessageList {

    /**
     * Success : true
     * Msg : 操作成功
     * Code : 0
     * Data : {"messageList":[{"messageID":4,"messageContent":"这是第二个测试消息","sendTime":"2017-05-15T16:53:49","status":"1"},{"messageID":3,"messageContent":"这是一个测试消息","sendTime":"2017-05-15T16:53:16","status":"1"}],"nextPage":false}
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
         * messageList : [{"messageID":4,"messageContent":"这是第二个测试消息","sendTime":"2017-05-15T16:53:49","status":"1"},{"messageID":3,"messageContent":"这是一个测试消息","sendTime":"2017-05-15T16:53:16","status":"1"}]
         * nextPage : false
         */

        private boolean nextPage;
        private List<MessageListBean> messageList;

        public boolean isNextPage() {
            return nextPage;
        }

        public void setNextPage(boolean nextPage) {
            this.nextPage = nextPage;
        }

        public List<MessageListBean> getMessageList() {
            return messageList;
        }

        public void setMessageList(List<MessageListBean> messageList) {
            this.messageList = messageList;
        }

        public static class MessageListBean implements Parcelable {
            /**
             * messageID : 4
             * messageContent : 这是第二个测试消息
             * sendTime : 2017-05-15T16:53:49
             * status : 1
             */

            private int messageID;
            private String messageContent;
            private String sendTime;
            private String status;

            public int getMessageID() {
                return messageID;
            }

            public void setMessageID(int messageID) {
                this.messageID = messageID;
            }

            public String getMessageContent() {
                return messageContent;
            }

            public void setMessageContent(String messageContent) {
                if (CommUtil.checkIsNull(messageContent))
                    messageContent="";
                this.messageContent = messageContent;
            }

            public String getSendTime() {
                return sendTime;
            }

            public void setSendTime(String sendTime) {
                if (CommUtil.checkIsNull(sendTime))
                    sendTime="";
                this.sendTime = sendTime;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                if (CommUtil.checkIsNull(status))
                    status="";
                this.status = status;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.messageID);
                dest.writeString(this.messageContent);
                dest.writeString(this.sendTime);
                dest.writeString(this.status);
            }

            public MessageListBean() {
            }

            protected MessageListBean(Parcel in) {
                this.messageID = in.readInt();
                this.messageContent = in.readString();
                this.sendTime = in.readString();
                this.status = in.readString();
            }

            public static final Parcelable.Creator<MessageListBean> CREATOR = new Parcelable.Creator<MessageListBean>() {
                @Override
                public MessageListBean createFromParcel(Parcel source) {
                    return new MessageListBean(source);
                }

                @Override
                public MessageListBean[] newArray(int size) {
                    return new MessageListBean[size];
                }
            };
        }
    }
}
