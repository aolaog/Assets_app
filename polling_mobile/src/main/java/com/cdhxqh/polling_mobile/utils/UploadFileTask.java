package com.cdhxqh.polling_mobile.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class UploadFileTask extends AsyncTask<String, Void, String> {
    private static  final String TAG="UploadFileTask";
    /**
     * 可变长的输入参数，与AsyncTask.exucute()对应
     */
    private ProgressDialog pdialog;
    private Activity context = null;

    String requestURL;

    private List<File> files;


    public UploadFileTask(Activity ctx, String requestURL, ProgressDialog mProgressDialog,List<File> files) {
        this.context = ctx;
        this.requestURL = requestURL;
        this.pdialog = mProgressDialog;
        this.files=files;
    }

    @Override
    protected void onPostExecute(String result) {
        // 返回HTML页面的内容
        pdialog.dismiss();
        if (UploadUtils.SUCCESS.equalsIgnoreCase(result)) {
            Toast.makeText(context, "上传成功!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "上传失败!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected String doInBackground(String... params) {
        return UploadUtils.uploadFile(files, requestURL);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }


}