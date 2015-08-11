package com.cdhxqh.polling_mobile.database;

/**
 * Created by yw on 2015/5/12.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public final class DatabaseHelper extends SQLiteOpenHelper {

    //db
    public static final String DB_NAME = "ins_task.db";
    public static final int DB_VERSION = 1;


    /**
     * 巡检任务表*
     */


    public static final String TICKET_TABLE_NAME = "ins_task_ticket";


    private static final String TICKET_TABLE_CREATE = "CREATE TABLE " + TICKET_TABLE_NAME
            + "(" + "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "id" + " CHAR(256)  NOT NULL, "
            + "inspectDate" + " CHAR(256)  NOT NULL, "
            + "inspectUser" + " CHAR(256) NOT NULL, "
            + "inspectUserID" + " CHAR(256) NOT NULL,"
            + "remark" + " CHAR(256) NOT NULL,"
            + "startTime" + " CHAR(256) NOT NULL,"
            + "endTime" + " CHAR(256) NOT NULL,"
            + "status" + " CHAR(256) NOT NULL,"
            + "taskTempletID" + " CHAR(256) NOT NULL,"
            + "taskTempletName" + " CHAR(256) NOT NULL)";


    /**
     * 巡检设备表*
     */
    public static final String DEVICE_TABLE_NAME = "ins_task_device";

    private static final String DEVICE_TABLE_CREATE = "CREATE TABLE " + DEVICE_TABLE_NAME
            + "(" + "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "assetNo" + " CHAR(256)  NOT NULL, "
            + "position" + " CHAR(256) NOT NULL, "
            + "relatedDevices" + " CHAR(256) NOT NULL,"
            + "rfid" + " CHAR(256) NOT NULL,"
            + "insDeviceID" + " CHAR(256) NOT NULL,"
            + "ticketID" + " CHAR(256) NOT NULL)";


    /**资产二级分类表**/

    public static final String ASSET_TWO_TABLE_NAME="asset_two_class";

    private static final String ASSET_TWO_TABLE_CREATE="CREATE TABLE " + ASSET_TWO_TABLE_NAME
            + "(" + "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "object_name" + " CHAR(256)  NOT NULL, "
            + "object_name_ch" + " CHAR(1024) NOT NULL)";



    /**资产三级分类表**/
    public static final String ASSET_THREE_TABLE_NAME="asset_three_class";

    private static final String ASSET_THREE_TABLE_CREATE="CREATE TABLE " + ASSET_THREE_TABLE_NAME
            + "(" + "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "assetNo" + " CHAR(256)  NOT NULL, "
            + "attributes" + " CHAR(1024) NOT NULL, "
            + "metrics" + " CHAR(256) NOT NULL,"
            + "rfid" + " CHAR(256) NOT NULL)";

    private volatile static DatabaseHelper mDBHelper;

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (mDBHelper == null) {
            synchronized (DatabaseHelper.class) {
                if (mDBHelper == null) {
                    mDBHelper = new DatabaseHelper(context);
                }
            }
        }

        return mDBHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TICKET_TABLE_CREATE);
        db.execSQL(DEVICE_TABLE_CREATE);
        db.execSQL(ASSET_TWO_TABLE_CREATE);
        db.execSQL(ASSET_THREE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TICKET_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DEVICE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ASSET_TWO_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ASSET_THREE_TABLE_NAME);

        onCreate(db);
    }
}
