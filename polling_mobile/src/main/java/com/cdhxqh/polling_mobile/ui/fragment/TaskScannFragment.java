package com.cdhxqh.polling_mobile.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.cdhxqh.polling_mobile.Application;
import com.cdhxqh.polling_mobile.R;
import com.cdhxqh.polling_mobile.adapter.TaskScannAdapter;
import com.cdhxqh.polling_mobile.database.PollDataSource;
import com.cdhxqh.polling_mobile.model.Asset_three_class;
import com.cdhxqh.polling_mobile.model.Ins_task_device;
import com.cdhxqh.polling_mobile.ui.widget.MyRecyclerView;
import com.cdhxqh.polling_mobile.utils.Configuration;
import com.cdhxqh.polling_mobile.utils.ConfigurationSettings;
import com.cdhxqh.polling_mobile.utils.DataTransfer;
import com.senter.support.openapi.StUhf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    /**
     * 暂无信息的布局*
     */
    LinearLayout notLinearLayout;


    /**
     * 判断是否有相同的元素*
     */
    String EPC = "";

    /**
     * 巡检ID*
     */
    String task_id;


    /**
     * 声音 *
     */
    private SoundPool sp;

    private Map<Integer, Integer> suondMap;

    /**rfid**/
    private static StUhf rfid;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_scann, container, false);
        ass_no_text = (TextView) view.findViewById(R.id.item_d_name);
        rfid_text = (TextView) view.findViewById(R.id.item_d_num);
        device_position_text = (TextView) view.findViewById(R.id.item_d_location);
        scannText = (TextView) view.findViewById(R.id.start_scann_id);
        recyclerView = (RecyclerView) view.findViewById(R.id.list_recyclerView);

        notLinearLayout = (LinearLayout) view.findViewById(R.id.not_linear_layout_id);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments().containsKey("ins_task_device")) {
            ins_task_device = getArguments().getParcelable("ins_task_device");

        }
        if (getArguments().containsKey("task_id")) {
            task_id = getArguments().getString("task_id");
        }
        rfid=Application.getRfid();
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
        initSoundPool();
        setAdapter();
    }


    /**
     * setAdapter*
     */
    private void setAdapter() {
        taskScannAdapter = new TaskScannAdapter(getActivity(), task_id, ins_task_device.insDeviceID);
        recyclerView.setAdapter(taskScannAdapter);

        addNewUiiMassageToListview(null);
    }

    private View.OnClickListener scannTextOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mProgressDialog = ProgressDialog.show(getActivity(), null, "正在努力扫描...", true, true);
            uiOnInverntryButton();
        }
    };

    /**
     * 开始扫描的方法*
     */
    private void uiOnInverntryButton() {

        startInventorya();
    }




















    private void startInventorya() {
        Application.getRfid().getInterrogatorAs(StUhf.InterrogatorModelDs.InterrogatorModelD2.class).iso18k6cRealTimeInventory(1, new StUhf.InterrogatorModelDs.UmdOnIso18k6cRealTimeInventory() {

            @Override
            public void onFinishedWithError(StUhf.InterrogatorModelDs.UmdErrorCode error) {
               Log.i(TAG,"onf");
                Message mess = new Message();
                Bundle b = new Bundle();
                b.putParcelableArrayList("list", null);
                mess.what = 1001;
                mess.setData(b);
                mHandler.sendMessage(mess);
            }

            @Override
            public void onFinishedSuccessfully(Integer antennaId, int readRate, int totalRead) {
                Log.i(TAG,"readRate="+readRate+",totalRead="+totalRead);
//                play(1, 0);
                mProgressDialog.dismiss();

            }

            @Override
            public void onTagInventory(StUhf.UII uii, StUhf.InterrogatorModelDs.UmdFrequencyPoint frequencyPoint, Integer antennaId, StUhf.InterrogatorModelDs.UmdRssi rssi) {
                if (uii != null) {

                    String epc = DataTransfer.xGetString(uii.getEpc().getBytes());
                    String epcnew = epc.replaceAll(" ", "");

                    Log.i(TAG, "epcnew=" + epcnew);
                    if (!epcnew.equals("") && EPC.equals("")) {
                        Application.stop();
                        ArrayList<Asset_three_class> list = getAllDevices(epcnew);

                        Message mess = new Message();
                        Bundle b = new Bundle();
                        b.putParcelableArrayList("list", list);
                        mess.setData(b);
                        mess.what = 1000;
                        mHandler.sendMessage(mess);
                        EPC = epcnew;
                    } else if (epcnew.equals("") && EPC.equals("")) {
                        Message mess = new Message();
                        mess.what = 1001;
                        mHandler.sendMessage(mess);
                    }


                }


            }
        });
    }


    private void addNewUiiMassageToListview(ArrayList<Asset_three_class> array) {
        if (array == null || array.size() == 0) {
            notLinearLayout.setVisibility(View.VISIBLE);
        } else {
            notLinearLayout.setVisibility(View.GONE);
            taskScannAdapter.update(array, true);
        }
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
                    play(1, 0);
                    mProgressDialog.dismiss();
                    Bundle b = msg.getData();
                    ArrayList<Asset_three_class> list = b.getParcelableArrayList("list");
                    addNewUiiMassageToListview(list);
                    isEnabled = true;
                    scannText.setText(R.string.start_scann);
                    break;
                case 1001: // 操作失败
                    play(1, 0);
                    mProgressDialog.dismiss();
                    addNewUiiMassageToListview(null);
                    break;
                case 1002: // 操作失败
                    break;

            }
        }
    };


    /**
     * 查询Rfid相应的设备信息*
     */
    private ArrayList<Asset_three_class> getAllDevices(String epc) {
        ArrayList<Asset_three_class> three_list = mDataSource.getAllThreeDevice(epc);
        return three_list;
    }


    /**
     * 初始化声音 *
     */
    private void initSoundPool() {
        this.sp = new SoundPool(1, 3, 1);
        this.suondMap = new HashMap();
        this.suondMap.put(Integer.valueOf(1),
                Integer.valueOf(this.sp.load(getActivity(), R.raw.msg, 1)));
    }

    /**
     * 播放声音 *
     */
    private void play(int paramInt1, int paramInt2) {
        AudioManager localAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        // float f1 = localAudioManager.getStreamMaxVolume(3);
        float f2 = localAudioManager.getStreamVolume(3);
        this.sp.play(((Integer) this.suondMap.get(Integer.valueOf(paramInt1)))
                .intValue(), f2, f2, 1, paramInt2, 1.0F);
    }





}
