package com.cdhxqh.polling_mobile.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cdhxqh.polling_mobile.R;
import com.cdhxqh.polling_mobile.adapter.GalleryAdapter;
import com.cdhxqh.polling_mobile.api.HttpRequestHandler;
import com.cdhxqh.polling_mobile.api.Manager;
import com.cdhxqh.polling_mobile.model.Ins_device_log;
import com.cdhxqh.polling_mobile.utils.FileUtils;
import com.cdhxqh.polling_mobile.utils.FormFile;
import com.cdhxqh.polling_mobile.utils.MessageUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class ResultFragment extends BaseFragment {

    private static final String TAG = "ResultFragment";


    /**
     * 设备编号
     */
    TextView insDeviceIDtext;
    /**
     * 巡检任务单号*
     */
    TextView ticketID;
    /**
     * 巡检时间*
     */
    TextView inspectTime;
    /**
     * 巡检人*
     */
    TextView inspectUserID;
    /**
     * 状态Spinner*
     */
    Spinner spinner;
    /**
     * 问题标题*
     */
    Spinner problemspinner;
    /**
     * 问题描述*
     */
    EditText problemdesc;

    /**
     * 拍照*
     */
    ImageView camera;

    /**
     * 暂无图片*
     */
    ImageView notImage;

    /**
     * RecyclerView*
     */
    RecyclerView recyclerView;


    /**
     * 状态选择*
     */
    Spinner statusSpinner;


    /**
     * 提交按钮*
     */
    Button submit;

    /**
     * 进度条*
     */
    ProgressDialog mProgressDialog;

    /**
     * Bitmaps*
     */
    ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
    /**
     * GalleryAdapter*
     */
    GalleryAdapter galleryAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        insDeviceIDtext = (TextView) view.findViewById(R.id.asset_device_id);
        ticketID = (TextView) view.findViewById(R.id.asset_task_id);
        inspectTime = (TextView) view.findViewById(R.id.asset_inspect_time);
        inspectUserID = (TextView) view.findViewById(R.id.asset_inspectuser_id);

        spinner = (Spinner) view.findViewById(R.id.spinnerBase);


        problemspinner = (Spinner) view.findViewById(R.id.spinner_problem_id);


        problemdesc = (EditText) view.findViewById(R.id.task_problem_desc_id);
        camera = (ImageView) view.findViewById(R.id.asset_image_id);
        notImage = (ImageView) view.findViewById(R.id.asset_not_image_id);
        recyclerView = (RecyclerView) view.findViewById(R.id.list_recyclerView);
        submit = (Button) view.findViewById(R.id.asset_submit_id);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments().containsKey("rfid")) {
            String rfid = getArguments().getString("rfid");
            insDeviceIDtext.setText(rfid);
        }

        ticketID.setText("2015-07-14-0000001");
        inspectTime.setText(getCurrentTime());
        inspectUserID.setText("admin");
        setStatusSpinner();
        setProblemSpinner();
        problemdesc.setText("温度过高");
        camera.setOnClickListener(cameraOnClickListener);
        submit.setOnClickListener(submitOnClickListener);
        spinner.setOnItemSelectedListener(spinnerOnItemSelectedListener);
        spinner.setOnItemSelectedListener(spinnerProOnItemSelectedListener);

        galleryAdapter = new GalleryAdapter(getActivity());
        recyclerView.setAdapter(galleryAdapter);
    }




    private AdapterView.OnItemSelectedListener spinnerOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            MessageUtils.showMiddleToast(getActivity(), position + "");
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    private AdapterView.OnItemSelectedListener spinnerProOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            MessageUtils.showMiddleToast(getActivity(), position + "");
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    /**
     * 设置状态数据源*
     */
    private void setStatusSpinner() {
        // 声明一个ArrayAdapter用于存放简单数据
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), R.layout.spinner_list_item_1, R.id.spinner_item_id,
                getSpinnerData());


        spinner.setAdapter(adapter);
    }

    /**设置问题数据源**/
    private void setProblemSpinner() {
        // 声明一个ArrayAdapter用于存放简单数据
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), R.layout.spinner_list_item_1, R.id.spinner_item_id,
                getProSpinnerData());


        problemspinner.setAdapter(adapter);
    }


    /**
     * 设置数据*
     */
    private List<String> getSpinnerData() {
        // 数据源
        List<String> dataList = new ArrayList<String>();
        dataList.add("正常");
        dataList.add("异常");
        dataList.add("故障");
        return dataList;
    }
    /**
     * 设置数据*
     */
    private List<String> getProSpinnerData() {
        // 数据源
        List<String> dataList = new ArrayList<String>();
        dataList.add("电器正常");
        dataList.add("电器异常");
        dataList.add("电器故障");
        return dataList;
    }

    /**
     * 获取系统当前时间*
     */
    private String getCurrentTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String time = df.format(new Date()); //获取当前系统时间
        return time;
    }


    /**
     * 获取需要提交的数据*
     */
    private Ins_device_log getData() {

        Ins_device_log ins_device_log = new Ins_device_log();
        ins_device_log.setAssetNo(insDeviceIDtext.getText().toString());
        ins_device_log.setInspectTime(inspectTime.getText().toString());
        ins_device_log.setInspectUserID(inspectUserID.getText().toString());

//        ins_device_log.setQuestionTitle(problemtitle.getText().toString());
        ins_device_log.setQuestionDetail(problemdesc.getText().toString());
        return ins_device_log;
    }


    /**
     * 封装需要提交的数据*
     */

    private JSONArray posttingData(Ins_device_log ins_device_log) {
        List<Ins_device_log> list = new ArrayList<Ins_device_log>();
        list.add(ins_device_log);
        JSONArray jsonArray = new JSONArray();
        for (Ins_device_log a : list) {
            JSONObject jo = new JSONObject();
            try {
                jo.put("inspectTime", a.inspectTime);
                jo.put("insDeviceID", "100038829922");
                jo.put("inspectUserID", a.inspectUserID);
                jo.put("status", a.status);
                jo.put("questionTitle", a.questionTitle);
                jo.put("questionDetail", a.questionDetail);
                jsonArray.put(jo);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return jsonArray;

    }


    private View.OnClickListener submitOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mProgressDialog = ProgressDialog.show(getActivity(), null,
                    "数据提交中....", true, true);
            JSONArray postJson = posttingData(getData());
            Manager.uploadTask(getActivity(), ticketID.getText().toString(), postJson, mLoginProfile.access_token, new HttpRequestHandler<Integer>() {
                @Override
                public void onSuccess(Integer data) {
                    Log.i(TAG, "**8");
                    MessageUtils.showMiddleToast(getActivity(), "提交成功");
                    mProgressDialog.dismiss();
                }

                @Override
                public void onSuccess(Integer data, int totalPages, int currentPage) {
                    Log.i(TAG, "asdsads");
                    MessageUtils.showMiddleToast(getActivity(), "提交成功");
                    mProgressDialog.dismiss();
                }

                @Override
                public void onFailure(String error) {
                    MessageUtils.showMiddleToast(getActivity(), "提交失败");
                    mProgressDialog.dismiss();
                }
            });


            String urlStr = Manager.IMAGE_URL + "?" + mLoginProfile.access_token;
            Map<String, String> params = new HashMap<String, String>();
            FormFile[] files = null;
            uploadImages(urlStr, params, files);


        }
    };


    private View.OnClickListener cameraOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i(TAG, "setCamera");
            setCamera();
        }
    };


    /**
     * 调用系统照相机*
     */
    private void setCamera() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, 1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                Log.i(TAG,
                        "SD card is not avaiable/writeable right now.");
//                return;
            }
            String name = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
            Toast.makeText(getActivity(), name, Toast.LENGTH_LONG).show();
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

            FileOutputStream b = null;
            //???????????????????????????????为什么不能直接保存在系统相册位置呢？？？？？？？？？？？？
            File file = new File("/sdcard/myImage/");
            file.mkdirs();// 创建文件夹
            String fileName = "/sdcard/myImage/" + name;

            try {
                b = new FileOutputStream(fileName);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    b.flush();
                    b.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bitmap != null) {
                notImage.setVisibility(View.GONE);
                bitmaps.add(bitmap);
                galleryAdapter.update(bitmaps, true);
            }
        }
    }

    /**
     * 提交图片
     *
     * @param urlpath 上传路径
     * @param params  请求参数 key为参数名,value为参数值
     * @param files   上传文件
     *                *
     */
    private void uploadImages(String urlpath, Map<String, String> params, FormFile[] files) {
        try {

            boolean isupload = FileUtils.FilePost(urlpath, params, files);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
