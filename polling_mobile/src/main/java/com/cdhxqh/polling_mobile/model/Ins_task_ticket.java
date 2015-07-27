package com.cdhxqh.polling_mobile.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 巡检任务表
 */
public class Ins_task_ticket extends PollModel implements Parcelable {
    public static final Creator<Ins_task_ticket> CREATOR = new Creator<Ins_task_ticket>() {
        @Override
        public Ins_task_ticket createFromParcel(Parcel source) {
            return new Ins_task_ticket(source);
        }

        @Override
        public Ins_task_ticket[] newArray(int size) {
            return new Ins_task_ticket[size];
        }
    };
    private static final long serialVersionUID = 2015050102L;

    /**
     * id*
     */
    public String id;
    /**
     * 计划巡检日期
     */
    public String inspectDate;


    /**
     * 巡检人
     */
    public String inspectUser;
    /**
     * 巡检人ID
     */
    public String inspectUserID;


    /**
     * 备注*
     */
    public String remark;

    /**
     * 开始时间*
     */
    public String startTime;

    /**
     * 结束时间*
     */
    public String endTime;


    /**
     * 状态*
     */
    public int status;

    /**
     * 模版Id*
     */
    public String taskTempletID;

    /**
     * 模版名称*
     */
    public String taskTempletName;

    public Ins_task_ticket() {
    }

    private Ins_task_ticket(Parcel in) {
        id = in.readString();
        inspectDate = in.readString();
        inspectUser = in.readString();
        inspectUserID = in.readString();
        status = in.readInt();
        remark = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        taskTempletID = in.readString();
        taskTempletName = in.readString();
    }

    public void parse(JSONObject jsonObject) throws JSONException {
        id = jsonObject.getString("id");
        inspectDate = jsonObject.getString("inspectDate");
        inspectUser = jsonObject.optString("inspectUser");
        inspectUserID = jsonObject.optString("inspectUserID");
        remark = jsonObject.optString("remark");
        status = jsonObject.optInt("status");
        startTime = jsonObject.optString("startTime");
        endTime = jsonObject.optString("endTime");
        taskTempletID = jsonObject.optString("taskTempletID");
        taskTempletName = jsonObject.optString("taskTempletName");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(inspectDate);
        dest.writeString(inspectUser);
        dest.writeString(inspectUserID);
        dest.writeString(remark);
        dest.writeString(startTime);
        dest.writeInt(status);
        dest.writeString(endTime);
        dest.writeString(taskTempletID);
        dest.writeString(taskTempletName);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInspectDate() {
        return inspectDate;
    }

    public void setInspectDate(String inspectDate) {
        this.inspectDate = inspectDate;
    }

    public String getInspectUser() {
        return inspectUser;
    }

    public void setInspectUser(String inspectUser) {
        this.inspectUser = inspectUser;
    }

    public String getInspectUserID() {
        return inspectUserID;
    }

    public void setInspectUserID(String inspectUserID) {
        this.inspectUserID = inspectUserID;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTaskTempletID() {
        return taskTempletID;
    }

    public void setTaskTempletID(String taskTempletID) {
        this.taskTempletID = taskTempletID;
    }

    public String getTaskTempletName() {
        return taskTempletName;
    }

    public void setTaskTempletName(String taskTempletName) {
        this.taskTempletName = taskTempletName;
    }
}
