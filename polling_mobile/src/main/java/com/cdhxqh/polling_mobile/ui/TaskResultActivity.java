package com.cdhxqh.polling_mobile.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.cdhxqh.polling_mobile.R;
import com.cdhxqh.polling_mobile.adapter.GalleryAdapter;
import com.cdhxqh.polling_mobile.api.HttpRequestHandler;
import com.cdhxqh.polling_mobile.api.Manager;
import com.cdhxqh.polling_mobile.model.Ins_device_log;
import com.cdhxqh.polling_mobile.model.MemberModel;
import com.cdhxqh.polling_mobile.ui.fragment.ResultFragment;
import com.cdhxqh.polling_mobile.utils.AccountUtils;
import com.cdhxqh.polling_mobile.utils.MessageUtils;
import com.cdhxqh.polling_mobile.utils.UploadFileTask;

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
import java.util.List;
import java.util.Locale;

/**
 * 巡检结果界面*
 */
public class TaskResultActivity extends BaseActivity {

    private static final String TAG = "TaskResultActivity";

    ResultFragment resultFragment;
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

    /**
     * rfid*
     */
    String rifd;
    /**
     * task_id*
     */
    String task_id;
    /**
     * insDeviceID*
     */
    String insDeviceID;


    /**
     * 状态选择*
     */
    String sposition;

    /**
     * 问题标题*
     */
    String questionTitle;

    /**
     * 问题数据源*
     */
    ArrayAdapter<String> qadapter;


    private MemberModel mLoginProfile;


    List<File> uoloadFiles=new ArrayList<File>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_result);


        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }


        getInitData();

        findViewById();

        initView();
    }

    /**
     * 初始化界面数据*
     */
    private void getInitData() {
        rifd = getIntent().getExtras().getString("rfid");
        task_id = getIntent().getExtras().getString("task_id");
        insDeviceID = getIntent().getExtras().getString("insDeviceID");

        mLoginProfile = AccountUtils.readLoginMember(this);
        Log.i(TAG, "rifd" + rifd + ",task_id=" + task_id + ",insDeviceID=" + insDeviceID);
    }


    /**
     * 初始化Id*
     */
    private void findViewById() {

        insDeviceIDtext = (TextView) findViewById(R.id.asset_device_id);
        ticketID = (TextView) findViewById(R.id.asset_task_id);
        inspectTime = (TextView) findViewById(R.id.asset_inspect_time);
        inspectUserID = (TextView) findViewById(R.id.asset_inspectuser_id);

        spinner = (Spinner) findViewById(R.id.spinnerBase);


        problemspinner = (Spinner) findViewById(R.id.spinner_problem_id);


        problemdesc = (EditText) findViewById(R.id.task_problem_desc_id);
        camera = (ImageView) findViewById(R.id.asset_image_id);
        notImage = (ImageView) findViewById(R.id.asset_not_image_id);
        recyclerView = (RecyclerView) findViewById(R.id.list_recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(TaskResultActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);


    }

    /**
     * 初始化事件监听*
     */
    private void initView() {
        insDeviceIDtext.setText(insDeviceID);  //设备ID

        ticketID.setText(task_id); //巡检ID

        inspectTime.setText(getCurrentTime()); //巡检时间


        inspectUserID.setText(mLoginProfile.display_name); //巡检用户Id

        setStatusSpinner();

        setProblemSpinner();

        problemdesc.setText("温度过高");

        camera.setOnClickListener(cameraOnClickListener);

        spinner.setOnItemSelectedListener(spinnerOnItemSelectedListener);

        problemspinner.setOnItemSelectedListener(spinnerProOnItemSelectedListener);

        galleryAdapter = new GalleryAdapter(TaskResultActivity.this);

        recyclerView.setAdapter(galleryAdapter);
    }


    private AdapterView.OnItemSelectedListener spinnerOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            sposition = position + "";
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    private AdapterView.OnItemSelectedListener spinnerProOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            questionTitle = qadapter.getItem(position);
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
                TaskResultActivity.this, R.layout.spinner_list_item_1, R.id.spinner_item_id,
                getSpinnerData());


        spinner.setAdapter(adapter);
    }

    /**
     * 设置问题数据源*
     */
    private void setProblemSpinner() {
        // 声明一个ArrayAdapter用于存放简单数据
        qadapter = new ArrayAdapter<String>(
                TaskResultActivity.this, R.layout.spinner_list_item_1, R.id.spinner_item_id,
                getProSpinnerData());


        problemspinner.setAdapter(qadapter);
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
    private Ins_device_log submitGetData() {

        Ins_device_log ins_device_log = new Ins_device_log();

        ins_device_log.setInsDeviceID(insDeviceIDtext.getText().toString()); //设备ID


        ins_device_log.setTicketID(ticketID.getText().toString()); //巡检任务单号

        ins_device_log.setInspectTime(inspectTime.getText().toString()); //巡检时间

        ins_device_log.setInspectUserID(mLoginProfile.login_id); //巡检人

        ins_device_log.setStatus(sposition); //巡检状态

        ins_device_log.setQuestionTitle(questionTitle); //问题标题

        ins_device_log.setQuestionDetail(problemdesc.getText().toString());  //问题详情
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
                jo.put("inspectTime", a.inspectTime); //巡检时间

                jo.put("insDeviceID", a.insDeviceID); //设备ID

                jo.put("inspectUserID", a.inspectUserID); //巡检人

                jo.put("status", a.status); //巡检状态

                jo.put("questionTitle", a.questionTitle); //巡检标题

                jo.put("questionDetail", a.questionDetail); //巡检详情
                jsonArray.put(jo);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return jsonArray;

    }


    private View.OnClickListener cameraOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
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
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

            FileOutputStream b = null;
            File file = new File("/sdcard/myImage/");
            file.mkdirs();// 创建文件夹
            String fileName = "/sdcard/myImage/" + name;
            File uoloadFile = new File(fileName);
            uoloadFiles.add(uoloadFile);
            Log.i(TAG, "fileName=" + fileName);
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


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_start:
                submitData();
                return true;
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
                } else {
                    upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_task_result, menu);

        return true;
    }


    /**
     * 提交封装的数据*
     */
    private void submitData() {
        mProgressDialog = ProgressDialog.show(TaskResultActivity.this, null,
                "数据提交中....", true, true);
        JSONArray postJson = posttingData(submitGetData());

        Manager.uploadTask(TaskResultActivity.this, task_id, postJson, mLoginProfile.access_token, new HttpRequestHandler<Integer>() {
            @Override
            public void onSuccess(Integer data) {
//                MessageUtils.showMiddleToast(TaskResultActivity.this, "提交成功");

                String IMAGE_URL=AccountUtils.getIp(TaskResultActivity.this)+"inspect/inspect_ticket/images";

                 String url = IMAGE_URL + "?access_token=" + mLoginProfile.access_token + "&ticketID=" + task_id + "&deviceID=" + insDeviceID;

                //提交图片
                uoloadFile(url,uoloadFiles,mProgressDialog);

                mProgressDialog.dismiss();
            }


            @Override
            public void onSuccess(Integer data, int totalPages, int currentPage) {
                MessageUtils.showMiddleToast(TaskResultActivity.this, "提交成功");
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(String error) {
                MessageUtils.showMiddleToast(TaskResultActivity.this, "提交失败");
                mProgressDialog.dismiss();
            }
        });
    }


    /**
     * 上传图片*
     */

    private void uoloadFile(String url, List<File> files,ProgressDialog mProgressDialog) {
        UploadFileTask upload = new UploadFileTask(TaskResultActivity.this, url,mProgressDialog,files);
        upload.execute();


    }


}
