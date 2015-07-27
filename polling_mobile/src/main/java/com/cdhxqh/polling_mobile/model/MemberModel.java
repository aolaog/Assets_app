package com.cdhxqh.polling_mobile.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class MemberModel extends PollModel implements Parcelable {
    public static final Creator<MemberModel> CREATOR = new Creator<MemberModel>() {
        @Override
        public MemberModel createFromParcel(Parcel source) {
            return new MemberModel(source);
        }

        @Override
        public MemberModel[] newArray(int size) {
            return new MemberModel[size];
        }
    };
    private static final long serialVersionUID = 2015050102L;
    public String login_id;
    public String access_token;
    public String expires_in;      //73*73
    public String display_name;

    public MemberModel() {
    }

    private MemberModel(Parcel in) {
        login_id = in.readString();
        access_token = in.readString();
        expires_in = in.readString();
        display_name = in.readString();
    }

    public void parse(JSONObject jsonObject) throws JSONException {
        login_id = jsonObject.getString("login_id");
        access_token = jsonObject.getString("access_token");
        expires_in = jsonObject.optString("expires_in");
        display_name = jsonObject.optString("display_name");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(login_id);
        dest.writeString(access_token);
        dest.writeString(expires_in);
        dest.writeString(display_name);
    }


    public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }
}
