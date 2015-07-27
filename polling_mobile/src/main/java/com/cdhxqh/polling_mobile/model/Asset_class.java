package com.cdhxqh.polling_mobile.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;
/**资产分类**/
public class Asset_class extends PollModel implements Parcelable {
    public static final Creator<Asset_class> CREATOR = new Creator<Asset_class>() {
        @Override
        public Asset_class createFromParcel(Parcel source) {
            return new Asset_class(source);
        }

        @Override
        public Asset_class[] newArray(int size) {
            return new Asset_class[size];
        }
    };
    private static final long serialVersionUID = 2015050102L;
    /**资产分类**/
    public String id;
    /**分类名称**/
    public String className;

    public Asset_class() {
    }

    private Asset_class(Parcel in) {
        id = in.readString();
        className = in.readString();
    }

    public void parse(JSONObject jsonObject) throws JSONException {
        id = jsonObject.getString("id");
        className = jsonObject.getString("className");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(className);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
