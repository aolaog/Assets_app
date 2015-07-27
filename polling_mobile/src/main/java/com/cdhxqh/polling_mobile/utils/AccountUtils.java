package com.cdhxqh.polling_mobile.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.cdhxqh.polling_mobile.R;
import com.cdhxqh.polling_mobile.model.MemberModel;
import com.cdhxqh.polling_mobile.model.PersistenceHelper;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;

/**
 * 登录帐号管理Created by yw on 2015/5/5.
 */
public class AccountUtils {

    public static final int REQUEST_LOGIN = 0;

    private static final String key_login_member = "logined@member";
    private static final String key_fav_nodes = "logined@fav_nodes";

    /**
     * 帐号登陆登出监听接口
     */
    public static interface OnAccountListener{
        abstract public void onLogout();

        abstract public void onLogin(MemberModel member);
    }

    private static HashSet<OnAccountListener> listeners = new HashSet<OnAccountListener>();

    /**
     * 注册登录接口
     * @param listener
     */
    public static void registerAccountListener(OnAccountListener listener){
        listeners.add(listener);
    }

    /**
     * 取消登录接口的注册
     * @param listener
     */
    public static void unregisterAccountListener(OnAccountListener listener){
        listeners.remove(listener);
    }
//
    /**
     * 用户是否已经登录
     * @param cxt
     * @return
     */
    public static boolean isLogined(Context cxt) {
        return FileUtils.isExistDataCache(cxt, key_login_member);
    }

    /**
     * 保存登录用户资料
     * @param cxt
     * @param profile
     */
    public static void writeLoginMember(Context cxt, MemberModel profile) {
        PersistenceHelper.saveModel(cxt, profile, key_login_member);

        //通知所有页面,登录成功,更新用户信息
        Iterator<OnAccountListener> iterator = listeners.iterator();
        while(iterator.hasNext()){
            OnAccountListener listener = iterator.next();
            listener.onLogin(profile);
        }
    }

    /**
     * 获取登录用户信息
     * @param cxt
     * @return
     */
    public static MemberModel readLoginMember(Context cxt) {
        return PersistenceHelper.loadModel(cxt, key_login_member);
    }

    /**
     * 删除登录用户资料
     * @param cxt
     */
    public static void removeLoginMember(Context cxt) {
        File data = cxt.getFileStreamPath(key_login_member);
        data.delete();
    }

//    /**
//     * 保存节点收藏信息
//     * @param cxt
//     * @param nodes
//     */
//    public static void writeFavoriteNodes(Context cxt, ArrayList<NodeModel> nodes) {
//        PersistenceHelper.saveObject(cxt, nodes, key_fav_nodes);
//        for(NodeModel node : nodes){
//            Application.getDataSource().favoriteNode(node.name, true);
//        }
//    }
//
//    /**
//     * 获取收藏节点信息
//     * @param cxt
//     * @return
//     */
//    public static ArrayList<NodeModel> readFavoriteNodes(Context cxt) {
//        return (ArrayList<NodeModel>) PersistenceHelper.loadObject(cxt, key_fav_nodes);
//    }
//
//
//    /**
//     * 删除节点信息
//     * @param cxt
//     */
//    public static void removeFavNodes(Context cxt) {
//        File data = cxt.getFileStreamPath(key_fav_nodes);
//        data.delete();
//    }
//
    /**
     * 清除所有用户相关资料
     * @param cxt
     */
    public static void removeAll(Context cxt) {
        removeLoginMember(cxt);
//        removeFavNodes(cxt);

        //通知所有页面退出登录了,清除登录痕迹
        Iterator<OnAccountListener> iterator = listeners.iterator();
        while(iterator.hasNext()){
            OnAccountListener listener = iterator.next();
            listener.onLogout();
        }
    }
    /**
     * 记录是否记住密码
     *
     * @param cxt
     * @param isChecked *
     */

    public static void setChecked(Context cxt, boolean isChecked) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(cxt);
        sharedPreferences.edit().putBoolean(cxt.getString(R.string.logined_member_ischeck), isChecked).commit();

    }

    ;


    /**
     * 读取记住状态*
     */
    public static boolean getIsChecked(Context cxt) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(cxt);
        return sharedPreferences.getBoolean(cxt.getString(R.string.logined_member_ischeck), false);
    }


    /**
     * 记录用户名与密码
     *
     * @param cxt
     * @param userName
     * @param password
     */

    public static void setUserNameAndPassWord(Context cxt, String userName, String password) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(cxt);
        sharedPreferences.edit().putString(cxt.getString(R.string.logined_member_username), userName).putString(cxt.getString(R.string.logined_member_password), password).commit();
    }


    /**
     * 获取记住的用户名
     */


    public static String getUserName(Context cxt) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(cxt);
        return sharedPreferences.getString(cxt.getString(R.string.logined_member_username), "");
    }

    /**
     * 获取记住的密码
     */


    public static String getUserPassword(Context cxt) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(cxt);
        return sharedPreferences.getString(cxt.getString(R.string.logined_member_password), "");
    }

}
