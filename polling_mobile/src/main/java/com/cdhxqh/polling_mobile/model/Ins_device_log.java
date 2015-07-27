package com.cdhxqh.polling_mobile.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 巡检问题纪录
 */
public class Ins_device_log extends PollModel implements Parcelable {
    public static final Creator<Ins_device_log> CREATOR = new Creator<Ins_device_log>() {
        @Override
        public Ins_device_log createFromParcel(Parcel source) {
            return new Ins_device_log(source);
        }

        @Override
        public Ins_device_log[] newArray(int size) {
            return new Ins_device_log[size];
        }
    };
    private static final long serialVersionUID = 2015050102L;

    /**
     * 　资产名称
     */
    public String assetNo;
    /**
     * 巡检时间
     */
    public String inspectTime;
    /**
     * 资产编码*
     */
    public String insDeviceID;
    /**
     * 巡检用户的ID
     */
    public String inspectUserID;
    /**
     * 状态
     */
    public String status;
    /**
     * 问题标题
     */
    public String questionTitle;
    /**
     * 问题详情
     */
    public String questionDetail;


    public Ins_device_log() {
    }

    private Ins_device_log(Parcel in) {
        assetNo = in.readString();
        inspectTime = in.readString();
        insDeviceID = in.readString();
        inspectUserID = in.readString();
        status = in.readString();
        questionTitle = in.readString();
        questionDetail = in.readString();
    }

    public void parse(JSONObject jsonObject) throws JSONException {
        assetNo = jsonObject.getString("assetNo");
        inspectTime = jsonObject.getString("inspectTime");
        insDeviceID = jsonObject.optString("rfid");
        inspectUserID = jsonObject.optString("inspectUserID");
        status = jsonObject.optString("status");
        questionTitle = jsonObject.optString("questionTitle");
        questionDetail = jsonObject.optString("questionDetail");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(assetNo);
        dest.writeString(inspectTime);
        dest.writeString(insDeviceID);
        dest.writeString(inspectUserID);
        dest.writeString(status);
        dest.writeString(questionTitle);
        dest.writeString(questionDetail);
    }

    public String getAssetNo() {
        return assetNo;
    }

    public void setAssetNo(String assetNo) {
        this.assetNo = assetNo;
    }

    public String getInspectTime() {
        return inspectTime;
    }

    public void setInspectTime(String inspectTime) {
        this.inspectTime = inspectTime;
    }

    public String getInsDeviceID() {
        return insDeviceID;
    }

    public void setInsDeviceID(String insDeviceID) {
        this.insDeviceID = insDeviceID;
    }

    public String getInspectUserID() {
        return inspectUserID;
    }

    public void setInspectUserID(String inspectUserID) {
        this.inspectUserID = inspectUserID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getQuestionDetail() {
        return questionDetail;
    }

    public void setQuestionDetail(String questionDetail) {
        this.questionDetail = questionDetail;
    }
}
