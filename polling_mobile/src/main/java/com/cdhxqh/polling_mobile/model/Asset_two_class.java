package com.cdhxqh.polling_mobile.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**资产二级分类**/
public class Asset_two_class extends PollModel implements Parcelable {
    public static final Creator<Asset_two_class> CREATOR = new Creator<Asset_two_class>() {
        @Override
        public Asset_two_class createFromParcel(Parcel source) {
            return new Asset_two_class(source);
        }

        @Override
        public Asset_two_class[] newArray(int size) {
            return new Asset_two_class[size];
        }
    };
    private static final long serialVersionUID = 2015050102L;
    /**资产分类**/
    public String id;
    /**分类名称(英文)**/
    public String object_name;
    /**分类名称(中文)**/
    public String object_name_ch;


    public Asset_two_class() {
    }

    private Asset_two_class(Parcel in) {
        id = in.readString();
        object_name = in.readString();
        object_name_ch = in.readString();
    }

    public void parse(JSONObject jsonObject) throws JSONException {
        id = jsonObject.getString("id");
        object_name = jsonObject.getString("object_name");
        object_name_ch = jsonObject.getString("object_name_ch");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(object_name);
        dest.writeString(object_name_ch);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject_name() {
        return object_name;
    }

    public void setObject_name(String object_name) {
        this.object_name = object_name;
    }

    public String getObject_name_ch() {
        return object_name_ch;
    }

    public void setObject_name_ch(String object_name_ch) {
        this.object_name_ch = object_name_ch;
    }
}
