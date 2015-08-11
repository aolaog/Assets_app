package com.cdhxqh.polling_mobile.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 巡检设备对应表
 */
public class Ins_task_device extends PollModel implements Parcelable {
    public static final Creator<Ins_task_device> CREATOR = new Creator<Ins_task_device>() {
        @Override
        public Ins_task_device createFromParcel(Parcel source) {
            return new Ins_task_device(source);
        }

        @Override
        public Ins_task_device[] newArray(int size) {
            return new Ins_task_device[size];
        }
    };
    private static final long serialVersionUID = 2015050102L;

    /**
     * 　资产名称
     */
    public String assetNo;
    /**
     * 资产位置
     */
    public String position;
    /**
     * 相关的设备
     */
    public String relatedDevices;
    /**
     * 资产编码*
     */
    public String rfid;
    /**
     * 巡检任务Id
     */
    public String ticketID;
    /**insDeviceID**/
    public String insDeviceID;


    public Ins_task_device() {
    }

    private Ins_task_device(Parcel in) {
        assetNo = in.readString();
        position = in.readString();
        relatedDevices = in.readString();
        rfid = in.readString();
        ticketID = in.readString();
        insDeviceID = in.readString();
    }

    public void parse(JSONObject jsonObject) throws JSONException {
        assetNo = jsonObject.getString("assetNo");
        position = jsonObject.getString("position");
        relatedDevices = jsonObject.optString("relatedDevices");
        rfid = jsonObject.optString("rfid");
        ticketID = jsonObject.optString("ticketID");
        insDeviceID = jsonObject.optString("insDeviceID");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(assetNo);
        dest.writeString(position);
        dest.writeString(relatedDevices);
        dest.writeString(rfid);
        dest.writeString(ticketID);
        dest.writeString(insDeviceID);
    }

    public String getAssetNo() {
        return assetNo;
    }

    public void setAssetNo(String assetNo) {
        this.assetNo = assetNo;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getRelatedDevices() {
        return relatedDevices;
    }

    public void setRelatedDevices(String relatedDevices) {
        this.relatedDevices = relatedDevices;
    }

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public String getTicketID() {
        return ticketID;
    }

    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }

    public String getInsDeviceID() {
        return insDeviceID;
    }

    public void setInsDeviceID(String insDeviceID) {
        this.insDeviceID = insDeviceID;
    }
}
