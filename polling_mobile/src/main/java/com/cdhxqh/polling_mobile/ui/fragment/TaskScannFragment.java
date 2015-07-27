package com.cdhxqh.polling_mobile.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cdhxqh.polling_mobile.Application;
import com.cdhxqh.polling_mobile.R;
import com.cdhxqh.polling_mobile.adapter.TaskScannAdapter;
import com.cdhxqh.polling_mobile.database.PollDataSource;
import com.cdhxqh.polling_mobile.model.Asset_three_class;
import com.cdhxqh.polling_mobile.model.Ins_task_device;
import com.cdhxqh.polling_mobile.utils.Configuration;
import com.cdhxqh.polling_mobile.utils.ConfigurationSettings;
import com.cdhxqh.polling_mobile.utils.DataTransfer;
import com.senter.support.openapi.StUhf;

import java.util.ArrayList;
import java.util.List;

/**
 * 巡检扫描的Fragment*
 */
public class TaskScannFragment extends BaseFragment {

    static final String TAG = "TaskScannFragment";

    /**
     * 名称*
     */
    TextView ass_no_text;
    /**
     * 编码*
     */
    TextView rfid_text;
    /**
     * 位置*
     */
    TextView device_position_text;

    /**
     * 巡检设备对象*
     */
    Ins_task_device ins_task_device;


    /**
     * 扫描按钮*
     */

    TextView scannText;

    /**
     * RecyclerView*
     */
    RecyclerView recyclerView;


    /**
     * TaskScannAdapter*
     */
    TaskScannAdapter taskScannAdapter;

    private ProgressDialog mProgressDialog;

    private Configuration configuration;

    /**
     * 判断开始与停止*
     */
    private boolean isEnabled = true;

    PollDataSource mDataSource = Application.getDataSource();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_scann, container, false);
        ass_no_text = (TextView) view.findViewById(R.id.item_d_name);
        rfid_text = (TextView) view.findViewById(R.id.item_d_num);
        device_position_text = (TextView) view.findViewById(R.id.item_d_location);
        scannText = (TextView) view.findViewById(R.id.start_scann_id);
        recyclerView = (RecyclerView) view.findViewById(R.id.list_recyclerView);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments().containsKey("ins_task_device")) {
            ins_task_device = getArguments().getParcelable("ins_task_device");
            Log.i(TAG, "22222");
        }

        configuration = new Configuration(getActivity(), "Settings", getActivity().MODE_PRIVATE);
        ass_no_text.setText(ins_task_device.assetNo);
        rfid_text.setText(ins_task_device.rfid);
        device_position_text.setText(ins_task_device.position);
        scannText.setOnClickListener(scannTextOnClickListener);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        setAdapter();
    }


    /**
     * setAdapter*
     */
    private void setAdapter() {
        taskScannAdapter = new TaskScannAdapter(getActivity());
        recyclerView.setAdapter(taskScannAdapter);
//        addNewUiiMassageToListview("e20030981818006114807e20");
    }

    private View.OnClickListener scannTextOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mProgressDialog = ProgressDialog.show(getActivity(), null,"正在努力查询...", true, true);
            uiOnInverntryButton();
        }
    };

    /**
     * 监听事件的方法*
     */
    private void uiOnInverntryButton() {

        Log.i(TAG, "*****5");
        if (isEnabled) {
            if (startInventory()) {
                Log.i(TAG, "****6" + startInventory());
                setStateStoped();
            }
        } else {
            Log.i(TAG, "*****7");
            setStateStarted();

        }

    }


    protected boolean stopInventory() {
        if (Application.stop()) {
            return true;
        }
        return false;
    }


    private final boolean startInventoryAntiCollision() {
        boolean results = Application.rfid().getInterrogatorAs(StUhf.InterrogatorModelB.class).startInventoryWithAntiCollision(getQ(), new StUhf.OnNewUiiInventoried() {
            @Override
            public void onNewUiiReceived(StUhf.UII uii) {
                if (uii != null) {
                    String epc = DataTransfer.xGetString(uii.getEpc().getBytes());
                    String epcnew = epc.replaceAll(" ", "");

                    Log.i(TAG, "epc＝" + epcnew);
                    String testepc="e20030981818006113508dd4";
                    if (!epcnew.equals("")) {
                        Application.stop();
                        List<Asset_three_class> list=getAllDevices(testepc);
                        mProgressDialog.dismiss();
                        Message mess=new Message();
                        Bundle b=new Bundle();
                        b.putParcelableArrayList("list", (ArrayList<? extends android.os.Parcelable>) list);
                        mess.setData(b);
                        mess.what=1000;
                        mHandler.sendMessage(mess);

                    }
                    ;
                }
            }


        });

        Log.i(TAG, "results=" + results);
        return results;
    }


    private void addNewUiiMassageToListview(ArrayList<Asset_three_class> array) {
        taskScannAdapter.update(array, true);
    }

    private StUhf.Q getQ() {
        return StUhf.Q.values()[configuration.getInt(ConfigurationSettings.key_Q, 3)];
    }

    private boolean setQ(StUhf.Q q) {
        return configuration.setInt(ConfigurationSettings.key_Q, q.ordinal());
    }


    public final void setStateStoped() {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                isEnabled = false;
                Log.i(TAG, "设置结束");
                scannText.setText(R.string.stop_scan);
            }
        });
    }

    public final void setStateStarted() {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                isEnabled = true;
                Log.i(TAG, "设置开始");
                scannText.setText(R.string.start_scann);
                Application.stop();

            }
        });
    }

    protected final boolean startInventory() {
        boolean ret = false;


        ret = startInventoryAntiCollision();
        if (ret == false) {
            Application.stop();
        }
        return ret;
    }


    //设置Hander

    /**
     * 自定义handler1
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1000: // 操作成功
                    Bundle b = msg.getData();
                    ArrayList<Asset_three_class> list=b.getParcelableArrayList("list");
                    for (int i=0;i<list.size();i++){
                        Log.i(TAG,"*****assetNo="+list.get(i).assetNo);
                    }
                    addNewUiiMassageToListview(list);
                    isEnabled = true;
                    scannText.setText(R.string.start_scann);
                    Application.stop();
                    break;
                case 1001: // 操作失败
                    Log.i(TAG,"1001");
                    break;
                case 1002: // 操作失败
                    Log.i(TAG,"1002");
                    break;

            }
        }
    };


     /**查询Rfid相应的设备信息**/
    private List<Asset_three_class> getAllDevices(String epc){
        Log.i(TAG,"rfid="+epc);
        List<Asset_three_class> three_list=mDataSource.getAllThreeDevice(epc);
        if(three_list!=null){
            mProgressDialog.dismiss();


        }
            return three_list;
    }

}
