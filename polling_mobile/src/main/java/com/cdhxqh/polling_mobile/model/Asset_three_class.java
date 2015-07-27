package com.cdhxqh.polling_mobile.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**资产三级分类**/
public class Asset_three_class extends PollModel implements Parcelable {
    public static final Creator<Asset_three_class> CREATOR = new Creator<Asset_three_class>() {
        @Override
        public Asset_three_class createFromParcel(Parcel source) {
            return new Asset_three_class(source);
        }

        @Override
        public Asset_three_class[] newArray(int size) {
            return new Asset_three_class[size];
        }
    };
    private static final long serialVersionUID = 2015050102L;
    /**资产分类**/
    public String id;
    /**编号**/
    public String assetNo;
    /**内容**/
    public String attributes;
    /**相关状态**/
    public String metrics;
    /**rfid**/
    public String rfid;




    public Asset_three_class() {
    }

    private Asset_three_class(Parcel in) {
        id = in.readString();
        assetNo = in.readString();
        attributes = in.readString();
        metrics = in.readString();
        rfid = in.readString();
    }

    public void parse(JSONObject jsonObject) throws JSONException {
        id = jsonObject.getString("id");
        assetNo = jsonObject.getString("assetNo");
        attributes = jsonObject.getString("attributes");
        metrics = jsonObject.getString("metrics");
        rfid = jsonObject.getString("rfid");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(assetNo);
        dest.writeString(attributes);
        dest.writeString(metrics);
        dest.writeString(rfid);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAssetNo() {
        return assetNo;
    }

    public void setAssetNo(String assetNo) {
        this.assetNo = assetNo;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getMetrics() {
        return metrics;
    }

    public void setMetrics(String metrics) {
        this.metrics = metrics;
    }

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }
}
