package com.cdhxqh.polling_mobile.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**资产设备**/
public class Asset_device extends PollModel implements Parcelable {
    public static final Creator<Asset_device> CREATOR = new Creator<Asset_device>() {
        @Override
        public Asset_device createFromParcel(Parcel source) {
            return new Asset_device(source);
        }

        @Override
        public Asset_device[] newArray(int size) {
            return new Asset_device[size];
        }
    };
    private static final long serialVersionUID = 2015050102L;
    /**label**/
    public String label;
    /**name**/
    public String name;
    /**value**/
    public String value;

    public Asset_device() {
    }

    private Asset_device(Parcel in) {
        label = in.readString();
        name = in.readString();
        value = in.readString();
    }

    public void parse(JSONObject jsonObject) throws JSONException {
        label = jsonObject.getString("label");
        name = jsonObject.getString("name");
        value = jsonObject.getString("value");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label);
        dest.writeString(name);
        dest.writeString(value);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
