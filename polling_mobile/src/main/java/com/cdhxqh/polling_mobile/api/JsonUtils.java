package com.cdhxqh.polling_mobile.api;

import android.content.Context;
import android.util.Log;


import com.cdhxqh.polling_mobile.model.Asset_class;
import com.cdhxqh.polling_mobile.model.Asset_three_class;
import com.cdhxqh.polling_mobile.model.Asset_two_class;
import com.cdhxqh.polling_mobile.model.MemberModel;
import com.cdhxqh.polling_mobile.model.PersistenceHelper;
import com.cdhxqh.polling_mobile.utils.AccountUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Json数据解析类
 */
public class JsonUtils {
    private static final String TAG = "JsonUtils";
    private static final String TAG1 = "JsonUtils1";

    /**
     * 解析登陆信息*
     * data json数据
     * isChecked 是否保存密码
     */
    static boolean parsingAuthStr(final Context cxt, String data) {
        boolean isSuccess = false;
        try {
            JSONObject json = new JSONObject(data);
            isSuccess = json.getBoolean("isSuccess");

            if (isSuccess) {
                String jsonObject = json.getString("data");
                JSONObject jsonstr = new JSONObject(jsonObject);
                String login_id = jsonstr.getString("login_id");
                String access_token = jsonstr.getString("access_token");
                String expires_in = jsonstr.getString("expires_in");
                String display_name = jsonstr.getString("display_name");


                MemberModel mem = new MemberModel();
                mem.setLogin_id(login_id);
                mem.setAccess_token(access_token);
                mem.setExpires_in(expires_in);
                mem.setDisplay_name(display_name);

                AccountUtils.writeLoginMember(cxt, mem);
            } else {
                return isSuccess;
            }


        } catch (JSONException e) {
            e.printStackTrace();
            return isSuccess;
        }
        return isSuccess;
    }


    /**
     * 解析资产分类信息*
     */
    public static ArrayList<Asset_class> parsingAssetClass(final Context cxt, String data, String key) {

        boolean isSuccess = false;
        ArrayList<Asset_class> asset_classes = new ArrayList<Asset_class>();
        try {
            JSONObject json = new JSONObject(data);
            isSuccess = json.getBoolean("isSuccess");
            if (isSuccess) {
                JSONArray jsonArray = json.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    Asset_class asset_classe = new Asset_class();
                    String jsons = jsonArray.getString(i);
                    asset_classe.setId(i + 1 + "");
                    asset_classe.setClassName(jsons);
                    asset_classes.add(asset_classe);

                    PersistenceHelper.saveModelList(cxt, asset_classes, key);

                }

            } else {
                return null;
            }


        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return asset_classes;
    }

    /**
     * 解析二级分类*
     */
    public static ArrayList<Asset_two_class> parsingAssetTwoClass(final Context cxt, String data) {

        boolean isSuccess = false;
        ArrayList<Asset_two_class> asset_two_classs = new ArrayList<Asset_two_class>();
        try {
            JSONObject json = new JSONObject(data);
            isSuccess = json.getBoolean("isSuccess");
            if (isSuccess) {
                JSONArray jsonArray = json.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Asset_two_class asset_two_class = new Asset_two_class();
                    asset_two_class.setObject_name(jsonObject.getString("object_name"));
                    asset_two_class.setObject_name_ch(jsonObject.getString("object_name_ch"));

                    Log.i(TAG, "object_name=" + jsonObject.getString("object_name") + ",object_name_ch=" + jsonObject.getString("object_name_ch"));
                    asset_two_classs.add(asset_two_class);
                }

            } else {
                return null;
            }


        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return asset_two_classs;
    }

    /**
     * 解析三级分类*
     */
    public static List<Asset_three_class> parsingAssetthreeClass(final Context cxt, String data) {

        boolean isSuccess = false;
        List<Asset_three_class> asset_three_classs = new ArrayList<Asset_three_class>();
        try {
            JSONObject json = new JSONObject(data);
            isSuccess = json.getBoolean("isSuccess");
            if (isSuccess) {
                JSONArray jsonArray = json.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Asset_three_class asset_three_class = new Asset_three_class();


                    asset_three_class.setAssetNo(jsonObject.getString("assetNo"));
                    asset_three_class.setAttributes(jsonObject.getString("attributes"));
                    asset_three_class.setMetrics(jsonObject.getString("metrics"));
                    asset_three_class.setRfid(jsonObject.getString("rfid"));
                    asset_three_classs.add(asset_three_class);
                }

            } else {
                return null;
            }


        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return asset_three_classs;
    }


    /**
     * 解析某个任务单的数据*
     */
    public static boolean parsingItemTask(final Context cxt, String data) {

        boolean isSuccess = false;
        try {
            JSONObject json = new JSONObject(data);
            isSuccess = json.getBoolean("isSuccess");


        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(TAG, "资产 JsonExceotion=" + e);
            return isSuccess;
        }
        return isSuccess;
    }


    /**
     * 解析登出数据*
     */
    public static boolean parsingLoinout(final Context cxt, String data) {

        boolean isSuccess = false;
        try {
            JSONObject json = new JSONObject(data);
            isSuccess = json.getBoolean("isSuccess");


        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(TAG, " 登出 JsonExceotion=" + e);
            return isSuccess;
        }
        return isSuccess;
    }
}
