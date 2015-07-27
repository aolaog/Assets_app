package com.yaoyumeng.v2ex.api;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.yaoyumeng.v2ex.model.PersistenceHelper;
import com.yaoyumeng.v2ex.model.V2EXModel;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yw on 2015/5/2.
 */
class WrappedJsonHttpResponseHandler<T extends V2EXModel> extends JsonHttpResponseHandler {
    private static final String TAG="WrappedJsonHttpResponseHandler";
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
        Log.i(TAG,"response123="+response);
        ArrayList<T> models = new ArrayList<T>();
        try {
            T obj = (T) Class.forName(c.getName()).newInstance();
            obj.parse(response);
            if (obj != null)
                models.add(obj);
        } catch (Exception e) {
        }
        PersistenceHelper.saveModelList(context, models, key);
        try {
            handler.onSuccess(models);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        Log.i(TAG,"response="+response);
        ArrayList<T> models = new ArrayList<T>();
        Log.i(TAG,"response size="+response.length());
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject jsonObj = response.getJSONObject(i);
                Log.i(TAG,"jsonObj="+jsonObj);
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
        handleFailure(statusCode, e.getMessage());
    }

    public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
        handleFailure(statusCode, e.getMessage());
    }

    public void onFailure(int statusCode, Header[] headers, Throwable e, JSONArray errorResponse) {
        handleFailure(statusCode, e.getMessage());
    }

    private void handleFailure(int statusCode, String error) {
        error = V2EXErrorType.errorMessage(context, V2EXErrorType.ErrorApiForbidden);
        SafeHandler.onFailure(handler, error);
    }
}

