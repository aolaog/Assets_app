package com.cdhxqh.polling_mobile.api;

import android.content.Context;
import android.util.Log;

import com.cdhxqh.polling_mobile.model.PersistenceHelper;
import com.cdhxqh.polling_mobile.model.PollModel;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yw on 2015/5/2.
 */
class WrappedJsonHttpResponseHandler<T extends PollModel> extends JsonHttpResponseHandler {
    private static final String TAG = "WrappedJsonHttpResponseHandler";
    HttpRequestHandler<ArrayList<T>> handler;
    Class c;
    Context context;
    String key;

    public WrappedJsonHttpResponseHandler(Context cxt, Class c, String key,
                                          HttpRequestHandler<ArrayList<T>> handler) {
        this.handler = handler;
        this.c = c;
        this.context = cxt;
        this.key = key;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        Log.i(TAG, "response123=" + response);
        ArrayList<T> models = new ArrayList<T>();
        boolean isSuccess;
        try {
            isSuccess = response.getBoolean("isSuccess");
            String message=response.getString("message");
            if (isSuccess) {
                String jsonObjectData = response.getString("data");
                Log.i(TAG, "jsonObjectData=" + jsonObjectData);
                JSONArray jsona = new JSONArray(jsonObjectData);
                for (int j = 0; j < jsona.length(); j++) {
                    JSONObject jsonObjitem = jsona.getJSONObject(j);
                    T obj = (T) Class.forName(c.getName()).newInstance();
                    obj.parse(jsonObjitem);
                    if (obj != null)
                        models.add(obj);
                }
                PersistenceHelper.saveModelList(context, models, key);
                SafeHandler.onSuccess(handler, models);
            }else{
                SafeHandler.onFailure(handler, message);
            }
        } catch (Exception e) {

        }

    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        ArrayList<T> models = new ArrayList<T>();
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject jsonObj = response.getJSONObject(i);
                Log.i(TAG, "jsonObj=" + jsonObj);
                T obj = (T) Class.forName(c.getName()).newInstance();
                obj.parse(jsonObj);
                if (obj != null)
                    models.add(obj);
            } catch (Exception e) {
            }
        }
        PersistenceHelper.saveModelList(context, models, key);
        SafeHandler.onSuccess(handler, models);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable e) {
        Log.i(TAG, "statusCode1=" + statusCode);
        handleFailure(statusCode, e.getMessage());
    }

    public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
        Log.i(TAG, "statusCode2=" + statusCode);
        handleFailure(statusCode, e.getMessage());
    }

    public void onFailure(int statusCode, Header[] headers, Throwable e, JSONArray errorResponse) {
        Log.i(TAG, "statusCode3=" + statusCode);
        handleFailure(statusCode, e.getMessage());
    }

    private void handleFailure(int statusCode, String error) {
        Log.i(TAG, "statusCode4=" + statusCode);
        error = ErrorType.errorMessage(context, ErrorType.ErrorApiForbidden);
        SafeHandler.onFailure(handler, error);
    }
}

