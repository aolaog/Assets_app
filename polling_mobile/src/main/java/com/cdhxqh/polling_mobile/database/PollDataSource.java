package com.cdhxqh.polling_mobile.database;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cdhxqh.polling_mobile.model.Asset_three_class;
import com.cdhxqh.polling_mobile.model.Asset_two_class;
import com.cdhxqh.polling_mobile.model.Ins_task_device;
import com.cdhxqh.polling_mobile.model.Ins_task_ticket;

import java.util.ArrayList;
import java.util.List;

/**
 * 类说明:数据库帮助类
 * Created by yw on 2015/5/12.
 */
public class PollDataSource {

    private SQLiteDatabase database;

    final String TAG = "PollDataSource";

    public PollDataSource(DatabaseHelper dbHelper) {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * 巡检设备*
     */
    private String[] DeviceAllColumns = {"assetNo",
            "position", "relatedDevices", "rfid", "ticketID","insDeviceID"};
    /**
     * 巡检任务*
     */
    private String[] TaskAllColumns = {"id",
            "inspectDate", "inspectUser", "inspectUserID", "remark", "startTime", "endTime", "status", "taskTempletID", "taskTempletName"};


    /**
     * 查询巡检2级设备*
     */
    private String[] TWOAllColumns = {"object_name", "object_name_ch"};



    /**
     * 查询巡检3级设备*
     */
    private String[] ThreeAllColumns = {"assetNo", "attributes", "metrics", "rfid"};

    /**
     * 插入巡检列表数据*
     */
    private void insertTaskTicket(Ins_task_ticket model) {
        ContentValues values = new ContentValues();
        values.put("id", model.id);
        values.put("inspectDate", model.inspectDate);
        values.put("inspectUser", model.inspectUser);
        values.put("inspectUserID", model.inspectUserID);
        values.put("remark", model.remark);
        values.put("startTime", model.startTime);
        values.put("endTime", model.endTime);
        values.put("status", model.status);
        values.put("taskTempletID", model.taskTempletID);
        values.put("taskTempletName", model.taskTempletName);
        database.insert(DatabaseHelper.TICKET_TABLE_NAME, null, values);
    }

    /**
     * 更新巡检列表数据*
     */
    public void updateTaskTicket(Ins_task_ticket model) {
        Log.i(TAG, "****up");
        ContentValues values = new ContentValues();
//        values.put("id", model.id);
        values.put("inspectDate", model.inspectDate);
        values.put("inspectUser", model.inspectUser);
        values.put("inspectUserID", model.inspectUserID);
        values.put("remark", model.remark);
        values.put("startTime", model.startTime);
        values.put("endTime", model.endTime);
        values.put("status", model.status);
        values.put("taskTempletID", model.taskTempletID);
        values.put("taskTempletName", model.taskTempletName);
        Log.i(TAG, "****up0");
        database.update(DatabaseHelper.TICKET_TABLE_NAME, values, "id='" + model.id + "'", null);
        Log.i(TAG, "****up1");
    }


    /**
     * 插入巡检设备数据*
     */
    private void insertTaskDevice(Ins_task_device model) {
        ContentValues values = new ContentValues();
        values.put("assetNo", model.assetNo);
        values.put("position", model.position);
        values.put("relatedDevices", model.relatedDevices);
        values.put("rfid", model.rfid);
        values.put("ticketID", model.ticketID);
        values.put("insDeviceID", model.insDeviceID);
        Log.i(TAG, "assetNo=" + model.assetNo + ",position=" + model.position + ",relatedDevices=" + model.relatedDevices + ",rfid=" + model.rfid + ",ticketID1=" + model.ticketID+",insDeviceID="+model.insDeviceID);
        database.insert(DatabaseHelper.DEVICE_TABLE_NAME, null, values);
    }


    /**
     * 插入二级资产数据*
     */
    private void insertTwoAsset(Asset_two_class model) {
        ContentValues values = new ContentValues();

        values.put("object_name", model.object_name);
        values.put("object_name_ch", model.object_name_ch);
        Log.i(TAG, "object_name=" + model.object_name + ",object_name_ch=" + model.object_name_ch);
        database.insert(DatabaseHelper.ASSET_TWO_TABLE_NAME, null, values);
    }





















    /**
     * 插入三级资产数据*
     */
    private void insertThreeAsset(Asset_three_class model) {
        ContentValues values = new ContentValues();
//        values.put("id", model.assetNo);

        values.put("assetNo", model.assetNo);
        values.put("attributes", model.attributes);
        values.put("metrics", model.metrics);
        values.put("rfid", model.rfid);
        Log.i(TAG, "assetNo=" + model.assetNo + ",attributes=" + model.attributes + ",metrics=" + model.metrics + ",rfid=" + model.rfid);
        database.insert(DatabaseHelper.ASSET_THREE_TABLE_NAME, null, values);
    }


    /**
     * 判断巡检设备是否插入数据*
     */
    public void isInsertDevice(ArrayList<Ins_task_device> list) {
        Log.i(TAG, "121");
        //数据项不存在,插入
        for (int i = 0; i < list.size(); i++) {
            Ins_task_device model = list.get(i);
            String assetNo = model.assetNo;
            if (!isDeviceExisted(assetNo)) {
                insertTaskDevice(model);
            }
        }
    }

    /**
     * 判断巡检任务是否插入数据*
     */
    public void isInsertTask(ArrayList<Ins_task_ticket> list) {
        Log.i(TAG, "121");
        //数据项不存在,插入
        for (int i = 0; i < list.size(); i++) {
            Ins_task_ticket model = list.get(i);
            String id = model.id;
            Log.i(TAG, "****id=" + id);
            if (!isTaskExisted(id)) {
                Log.i(TAG, "******112");
                insertTaskTicket(model);
            }
        }
    }


    /**
     * 判断二级资产是否插入数据*
     */
    public void isInsertTwoAsset(ArrayList<Asset_two_class> list) {
        //数据项不存在,插入
        for (int i = 0; i < list.size(); i++) {

            Asset_two_class model = list.get(i);
            String object_name = model.object_name;
            Log.i(TAG,"object_name="+object_name);
            if (!isTwoDeviceExisted(object_name)) {
                insertTwoAsset(model);
            }
        }
    }



















    /**
     * 判断三级资产是否插入数据*
     */
    public void isInsertThreeAsset(ArrayList<Asset_three_class> list) {
        Log.i(TAG, "this is isinsert");
        //数据项不存在,插入
        for (int i = 0; i < list.size(); i++) {

            Asset_three_class model = list.get(i);
            String assetNo = model.assetNo;
            Log.i(TAG, "****sasdsa=" + assetNo);

            if (!isThreeDeviceExisted(assetNo)) {
                Log.i(TAG, "12312736217536721536721");
                insertThreeAsset(model);
            }
        }
    }

    /**
     * 巡检设备是否存在数据表中
     *
     * @param id
     * @return
     */
    private boolean isTaskExisted(String id) {
        Cursor cursor = database.query(DatabaseHelper.TICKET_TABLE_NAME, TaskAllColumns,
                "id" + " = '" + id + "'", null,
                null, null, null);

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            cursor.close();
            return true;
        }

        return false;
    }

    /**
     * 巡检设备是否存在数据表中
     *
     * @param assetNo
     * @return
     */
    private boolean isDeviceExisted(String assetNo) {
        Cursor cursor = database.query(DatabaseHelper.DEVICE_TABLE_NAME, DeviceAllColumns,
                "assetNo" + " = '" + assetNo + "'", null,
                null, null, null);

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            cursor.close();
            return true;
        }

        return false;
    }


    /**
     * 二级资产是否存在数据表中
     *
     * @param object_name
     * @return
     */
    private boolean isTwoDeviceExisted(String object_name) {
        Cursor cursor = database.query(DatabaseHelper.ASSET_TWO_TABLE_NAME, TWOAllColumns,
                "object_name" + " = '" + object_name + "'", null,
                null, null, null);
        Log.i(TAG, "this is sasdsadsas******");
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            cursor.close();
            Log.i(TAG, "this is true");
            return true;
        }
        Log.i(TAG, "this is false");
        return false;
    }






















    /**
     * 三级资产是否存在数据表中
     *
     * @param assetNo
     * @return
     */
    private boolean isThreeDeviceExisted(String assetNo) {
        Cursor cursor = database.query(DatabaseHelper.ASSET_THREE_TABLE_NAME, ThreeAllColumns,
                "assetNo" + " = '" + assetNo + "'", null,
                null, null, null);
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        return false;
    }

    /**
     * 根据巡检单号查询巡检设备*
     */
    public ArrayList<Ins_task_ticket> getAllDevice() {
        ArrayList<Ins_task_ticket> nodes = new ArrayList<Ins_task_ticket>();
        Cursor cursor = database.query(DatabaseHelper.TICKET_TABLE_NAME, TaskAllColumns,
                null, null, null, null,
                null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Log.i(TAG, "***76");
                Ins_task_ticket model = new Ins_task_ticket();
                model.setId(cursor.getString(0)); //id
                model.setInspectDate(cursor.getString(1)); //计划巡检日期
                model.setInspectUser(cursor.getString(2)); //巡检人
                model.setInspectUserID(cursor.getString(3));//巡检人Id
                model.setRemark(cursor.getString(4)); //备注
                model.setStartTime(cursor.getString(5)); //开始时间
                model.setEndTime(cursor.getString(6)); //结束时间
                model.setStatus(Integer.valueOf(cursor.getString(7))); //状态
                model.setTaskTempletID(cursor.getString(8)); //模版Id*
                model.setTaskTempletName(cursor.getString(9)); //模版名称*
                Log.i(TAG, "id=" + cursor.getString(0) + "/n" + "inspectDate=" + cursor.getString(1) + "/n" + "inspectUser=" + cursor.getString(2) + "/n" + "inspectUserID=" + cursor.getString(3) + "/n" + "remark=" + cursor.getString(4));
                nodes.add(model);
            }
            cursor.close();
        }

        return nodes;
    }

    /**
     * 根据巡检单号查询巡检设备*
     */
    public ArrayList<Ins_task_device> getAllDevice(String ticketID) {
        ArrayList<Ins_task_device> nodes = new ArrayList<Ins_task_device>();
        Cursor cursor = database.query(DatabaseHelper.DEVICE_TABLE_NAME, DeviceAllColumns,
                "ticketID" + " ='" + ticketID + "'", null, null, null,
                null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Ins_task_device model = new Ins_task_device();
                model.setAssetNo(cursor.getString(0)); //资产名称
                model.setPosition(cursor.getString(1)); //资产位置
                model.setRelatedDevices(cursor.getString(2)); //相关设备
                model.setRfid(cursor.getString(3));//rfid
                model.setTicketID(cursor.getString(4)); //巡检单ID
                model.setInsDeviceID(cursor.getString(5)); //巡检设备ID
                Log.i(TAG, "AssetNo=" + cursor.getString(0) + "/n" + "Position=" + cursor.getString(1) + "/n" + "RelatedDevices=" + cursor.getString(2) + "/n" + "Rfid=" + cursor.getString(3) + "/n" + "TicketID=" + cursor.getString(4)+",InsDeviceID"+cursor.getString(5));
                nodes.add(model);
            }
            cursor.close();
        }

        return nodes;
    }


    /**查询二级分类的信息**/

    public ArrayList<Asset_two_class> getAllTwoDevice() {
        ArrayList<Asset_two_class> nodes = new ArrayList<Asset_two_class>();
        Cursor cursor = database.query(DatabaseHelper.ASSET_TWO_TABLE_NAME, TWOAllColumns,
                null, null, null, null,
                null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Asset_two_class model = new Asset_two_class();

                model.setObject_name(cursor.getString(0)); //资产名称
                model.setObject_name_ch(cursor.getString(1)); //资产中文名称

                nodes.add(model);
            }
            cursor.close();
        }

        return nodes;
    }











    /**
     * 根据RFID查询对应资产*
     */
    public ArrayList<Asset_three_class> getAllThreeDevice(String rfid) {
        ArrayList<Asset_three_class> nodes = new ArrayList<Asset_three_class>();
        Cursor cursor = database.query(DatabaseHelper.ASSET_THREE_TABLE_NAME, ThreeAllColumns,
                "rfid" + " ='" + rfid + "'", null, null, null,
                null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Asset_three_class model = new Asset_three_class();
                Log.i(TAG, "*****AssetNo=" + cursor.getString(0) + "," + "Attributes=" + cursor.getString(1) + "," + "Metrics=" + cursor.getString(2) + "," + "Rfid=" + cursor.getString(3));

                model.setAssetNo(cursor.getString(0)); //资产名称
                model.setAttributes(cursor.getString(1)); //设备信息
                model.setMetrics(cursor.getString(2)); //相关设备
                model.setRfid(cursor.getString(3));//rfid

                nodes.add(model);
            }
            cursor.close();
        }

        return nodes;
    }

}
