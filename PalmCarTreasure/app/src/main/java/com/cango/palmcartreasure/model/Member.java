package com.cango.palmcartreasure.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.cango.palmcartreasure.util.CommUtil;

/**
 * Created by cango on 2017/5/4.
 */

public class Member implements Parcelable {

    private int id;
    private String name;
    private boolean isSelected;
    private boolean isGroupLeader;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (CommUtil.checkIsNull(name))
            name="";
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isGroupLeader() {
        return isGroupLeader;
    }

    public void setGroupLeader(boolean groupLeader) {
        isGroupLeader = groupLeader;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isGroupLeader ? (byte) 1 : (byte) 0);
    }

    public Member() {
    }

    protected Member(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.isSelected = in.readByte() != 0;
        this.isGroupLeader = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Member> CREATOR = new Parcelable.Creator<Member>() {
        @Override
        public Member createFromParcel(Parcel source) {
            return new Member(source);
        }

        @Override
        public Member[] newArray(int size) {
            return new Member[size];
        }
    };
}
