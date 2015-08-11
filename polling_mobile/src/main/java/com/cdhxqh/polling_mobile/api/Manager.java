package com.cdhxqh.polling_mobile.api;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.cdhxqh.polling_mobile.Application;
import com.cdhxqh.polling_mobile.database.PollDataSource;
import com.cdhxqh.polling_mobile.model.Asset_class;
import com.cdhxqh.polling_mobile.model.Asset_three_class;
import com.cdhxqh.polling_mobile.model.Asset_two_class;
import com.cdhxqh.polling_mobile.model.Ins_task_device;
import com.cdhxqh.polling_mobile.model.Ins_task_ticket;
import com.cdhxqh.polling_mobile.model.PersistenceHelper;
import com.cdhxqh.polling_mobile.ui.BaseActivity;
import com.cdhxqh.polling_mobile.utils.AccountUtils;
import com.cdhxqh.polling_mobile.utils.FileUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据信息处理类*
 */

public class Manager {
    private static final String TAG = "PollManager";

    private static Application mApp = Application.getInstance();

    private static final PollDataSource mDataSource = Application.getDataSource();

    private static AsyncHttpClient sClient = null;


    /**
     * 旧*
     */


    public static final String HTTP_API_URL = "http://123.56.84.244:9090/hxqh/tdapi/";
    /**新**/
//    private static final String HTTP_API_URL = "http://192.168.169.103:8080/hxqh/tdapi/";

//    /**
//     * 登陆接口*
//     */
//    private static final String SIGN_IN_URL = HTTP_API_URL + "user/login";
//    /**
//     * 根据登陆账号获取巡检任务单*
//     */
//    private static final String INSPECT_TICKET_URL = HTTP_API_URL + "inspect/inspect_ticket/today/list";
//    /**
//     * 获取单个巡检任务单*
//     */
//    private static final String INSPECT_DEVICE_URL = HTTP_API_URL + "inspect/inspect_ticket";
//    /**
//     * 上传巡检任务的结果*
//     */
//    private static final String UPLOAD_DEVICE_URL = HTTP_API_URL + "inspect/inspect_ticket/tickets";
//    /**
//     * 列出所有资产二级分类*
//     */
//    private static final String ASSET_ClASS_URL = HTTP_API_URL + "asset/class2/list";
//    /**
//     * 列出某二级分类下的所有资产三级分类*
//     */
//    private static final String ASSET_CHILD_CLASS_URL = HTTP_API_URL + "asset/class2/";
//    /**
//     * 列出某资产三级分类下所有设备*
//     */
//    private static final String ASSET_THREE_CLASS_URL = HTTP_API_URL + "asset/class3/";
//
//    /**
//     * 图片上传的URL*
//     */
//    public static final String IMAGE_URL = HTTP_API_URL + "inspect/inspect_ticket/images";
//    /**
//     * 登出管理*
//     */
//    private static final String LOGOUT_URL = HTTP_API_URL + "user/logout";

    /**
     * 下载用户登陆的巡检任务单列表*
     */

    //根据access_token获取巡检任务单**/
    public static void getInspect_ticket(Context ctx, final String access_token, boolean refresh,
                                         final HttpRequestHandler<ArrayList<Ins_task_ticket>> handler) {
        String INSPECT_TICKET_URL = AccountUtils.getIp(ctx) + "inspect/inspect_ticket/today/list";

        getInspect_tickets(ctx, INSPECT_TICKET_URL + "?access_token=" + access_token, refresh, handler);
    }


    /**
     * 获取所以资产分类*
     */
    public static void getAsset_class(Context ctx, final String access_token, boolean refresh,
                                      final HttpRequestHandler<ArrayList<Asset_class>> handler) {

        String ASSET_ClASS_URL = AccountUtils.getIp(ctx) + "asset/class2/list";
        getAssetClass(ctx, ASSET_ClASS_URL + "?access_token=" + access_token, access_token, refresh, handler);

    }

    /**
     * 列出某二级分类下的所有资产二级分类*
     */
    public static void getAsset_Child_class(Context ctx, final ArrayList<Asset_class> asset_class, final String access_token, boolean refresh,
                                            final HttpRequestHandler<Boolean> handler) {
        String ASSET_CHILD_CLASS_URL = AccountUtils.getIp(ctx) + "asset/class2/";
        if (asset_class != null || asset_class.size() != 0) {
            for (int i = 0; i < asset_class.size(); i++) {
                String urlStr = ASSET_CHILD_CLASS_URL + asset_class.get(i).className + "/list" + "?access_token=" + access_token;
                boolean b = getAssetChildClass(ctx, urlStr);
                if (!b) {
                    SafeHandler.onSuccess(handler, false);
                }
            }

            SafeHandler.onSuccess(handler, true);
        }


    }

    /**
     * 列出某资产三级分类下所有设备*
     */

    public static void getAsset_Three_class(Context ctx, final ArrayList<Asset_two_class> two_classes, final String access_token, boolean refresh,
                                            final HttpRequestHandler<Boolean> handler) {
        String ASSET_THREE_CLASS_URL = AccountUtils.getIp(ctx) + "asset/class3/";
        if (two_classes != null) {
            for (int i = 0; i < two_classes.size(); i++) {
                String urlStr = ASSET_THREE_CLASS_URL + two_classes.get(i).object_name + "/list" + "?access_token=" + access_token;

                boolean b = getAssetThreeClass(ctx, urlStr);
                if (!b) {
                    SafeHandler.onSuccess(handler, false);
                }
            }

            SafeHandler.onSuccess(handler, true);
        }

    }

    /**
     * 退出登陆*
     */
    public static void getLoginout(Context ctx, final String access_token, boolean refresh,
                                   final HttpRequestHandler<ArrayList<String>> handler) {
        String LOGOUT_URL = AccountUtils.getIp(ctx) + "user/logout";

        Loginout(ctx, LOGOUT_URL + "?access_token=" + access_token, refresh, handler);

    }

    /**
     * 使用用户名密码登录
     *
     * @param cxt
     * @param username 用户名
     * @param password 密码
     * @param handler  返回结果处理
     */
    public static void loginWithUsername(final Context cxt, final String username, final String password,
                                         final HttpRequestHandler<Integer> handler) {
        requestOnceWithURLString(cxt, username, password, new HttpRequestHandler<String>() {
            @Override
            public void onSuccess(String data, int totalPages, int currentPage) {

            }

            @Override
            public void onSuccess(String data) {
                //解析返回的Json数据
                boolean code = JsonUtils.parsingAuthStr(cxt, data);

                if (code == true) {
                    SafeHandler.onSuccess(handler, 200);
                } else {
                    SafeHandler.onFailure(handler, "登陆失败");
                }

            }


            @Override
            public void onFailure(String error) {
                SafeHandler.onFailure(handler, error);
            }
        });
    }


    private static void requestOnceWithURLString(final Context cxt, final String username, final String password,
                                                 final HttpRequestHandler<String> handler) {
        String SIGN_IN_URL = AccountUtils.getIp(cxt) + "user/login";
        Log.i(TAG, "SIGN_IN_URL=" + SIGN_IN_URL);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username", username);
        params.put("password", password);
        client.post(SIGN_IN_URL, params, new TextHttpResponseHandler() {


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                SafeHandler.onFailure(handler, ErrorType.errorMessage(cxt, ErrorType.ErrorLoginFailure));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (statusCode == 200) {
                    SafeHandler.onSuccess(handler, responseString);
                }
            }
        });
    }


    /**
     * 获取资产分类
     *
     * @param ctx
     * @param urlString URL地址
     * @param refresh   是否从缓存中读取
     * @param handler   结果处理
     */
    public static void getAssetClass(final Context ctx, String urlString, final String access_token, boolean refresh,
                                     final HttpRequestHandler<ArrayList<Asset_class>> handler) {
        Log.i(TAG, "AurlString=" + urlString);

        Uri uri = Uri.parse(urlString);
        String path = uri.getLastPathSegment();
        String param = uri.getEncodedQuery();
        String key = path;
        if (param != null)
            key += param;
        if (!refresh) {
            //尝试从缓存中加载
            ArrayList<Asset_class> asset_classes = PersistenceHelper.loadModelList(ctx, key);
            if (asset_classes != null && asset_classes.size() > 0) {
                SafeHandler.onSuccess(handler, asset_classes);
                return;
            }
        }


        final String finalKey = key;

        requestAssetURLString(ctx, urlString, new HttpRequestHandler<String>() {
            @Override
            public void onSuccess(String data, int totalPages, int currentPage) {
            }

            @Override
            public void onSuccess(String data) {


                ArrayList<Asset_class> asset_classes = JsonUtils.parsingAssetClass(ctx, data, finalKey);
                SafeHandler.onSuccess(handler, asset_classes);
            }


            @Override
            public void onFailure(String error) {
                SafeHandler.onFailure(handler, error);
            }
        });
    }

    /**
     * 获取资产二级分类
     *
     * @param ctx
     * @param urlString URL地址
     */
    public static boolean getAssetChildClass(final Context ctx, String urlString) {
        Log.i(TAG, "BurlString=" + urlString);

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60 * 10000);
        client.get(ctx, urlString, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (statusCode == 200) {
                    ArrayList<Asset_two_class> asset_two_classes = JsonUtils.parsingAssetTwoClass(ctx, responseString);
                    mDataSource.isInsertTwoAsset(asset_two_classes);
                }
            }
        });
        return true;
    }


    /**
     * 获取3分类下的所有设备
     *
     * @param ctx
     * @param urlString URL地址
     */
    public static boolean getAssetThreeClass(final Context ctx, String urlString) {
        Log.i(TAG, "CurlString=" + urlString);


        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60 * 10000);
        client.get(ctx, urlString, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (statusCode == 200) {
                    List<Asset_three_class> list = JsonUtils.parsingAssetthreeClass(ctx, responseString);
                    mDataSource.isInsertThreeAsset((ArrayList<Asset_three_class>) list);
                }
            }
        });
        return true;
    }


    private static void requestAssetURLString(final Context cxt, final String urlstring,
                                              final HttpRequestHandler<String> handler) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60 * 10000);
        client.get(cxt, urlstring, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                SafeHandler.onFailure(handler, responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (statusCode == 200) {
                    SafeHandler.onSuccess(handler, responseString);
                }
            }
        });
    }


    /**
     * 获取巡检任务单
     *
     * @param ctx
     * @param urlString URL地址
     * @param refresh   是否从缓存中读取
     * @param handler   结果处理
     */
    public static void getInspect_tickets(Context ctx, String urlString, boolean refresh,
                                          final HttpRequestHandler<ArrayList<Ins_task_ticket>> handler) {
        Log.i(TAG, "urlString=" + urlString);
        Uri uri = Uri.parse(urlString);
        String path = uri.getLastPathSegment();
        String param = uri.getEncodedQuery();
        String key = path;
        if (param != null)
            key += param;

        if (!refresh) {
            //尝试从缓存中加载
            ArrayList<Ins_task_ticket> topics = PersistenceHelper.loadModelList(ctx, key);
            if (topics != null && topics.size() > 0) {
                SafeHandler.onSuccess(handler, topics);
                return;
            }
        }

        new AsyncHttpClient().get(ctx, urlString,
                new WrappedJsonHttpResponseHandler<Ins_task_ticket>(ctx, Ins_task_ticket.class, key, handler));
    }


    /**
     * 获取单个巡检任务单
     *
     * @param cxt
     * @param ticketID     用户名
     * @param access_token 密码
     * @param handler      返回结果处理
     */
    public static void getIns_task_device(final Context cxt, final String ticketID, final String access_token, final boolean refresh,
                                          final HttpRequestHandler<ArrayList<Ins_task_device>> handler) {
        requestIns_task_device(cxt, ticketID, access_token, refresh, handler);

    }


    private static void requestIns_task_device(final Context cxt, final String ticketID, final String access_token, final boolean refresh,
                                               final HttpRequestHandler<ArrayList<Ins_task_device>> handler) {
        String INSPECT_DEVICE_URL = AccountUtils.getIp(cxt) + "inspect/inspect_ticket";
        String urlString = INSPECT_DEVICE_URL + "/" + ticketID + "?access_token=" + access_token;
        Log.i(TAG, "DurlString=" + urlString);
        Uri uri = Uri.parse(urlString);
        String path = uri.getLastPathSegment();
        String param = uri.getEncodedQuery();
        String key = path + ticketID;
        if (param != null)
            key += param;

//        if (!refresh) {
//            //尝试从缓存中加载
//            ArrayList<Ins_task_device> topics = PersistenceHelper.loadModelList(cxt, key);
//            if (topics != null && topics.size() > 0) {
//                SafeHandler.onSuccess(handler, topics);
//                return;
//            }
//        }

        new AsyncHttpClient().get(cxt, urlString,
                new WrappedJsonHttpResponseHandler<Ins_task_device>(cxt, Ins_task_device.class, key, handler));
    }


    /**
     * 上传巡检任务的结果*
     */
    public static void uploadTask(final Context cxt, final String ticket, final JSONArray result, final String access_token,
                                  final HttpRequestHandler<Integer> handler) {
        String UPLOAD_DEVICE_URL = AccountUtils.getIp(cxt) + "inspect/inspect_ticket/tickets";

        String URL = UPLOAD_DEVICE_URL + "?access_token=" + access_token;

        AsyncHttpClient client = new AsyncHttpClient();


        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < 1; i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("ticketID", ticket);
                jsonObject.put("insDeviceRecords", result);
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        String s = jsonArray.toString().replaceAll("\\\\", "");
        try {
            StringEntity entity = new StringEntity(s, "UTF-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            client.post(cxt, URL, entity, "application/json", new TextHttpResponseHandler() {

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    SafeHandler.onFailure(handler, ErrorType.errorMessage(cxt, ErrorType.ErrorLoginFailure));
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    Log.i(TAG, "statusCode=" + statusCode);
                    if (statusCode == 200) {

                        SafeHandler.onSuccess(handler, null);


                    }
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }


    /**退出给登陆**/

    /**
     * 获取资产分类
     *
     * @param ctx
     * @param urlString URL地址
     * @param refresh   是否从缓存中读取
     * @param handler   结果处理
     */

    public static void Loginout(final Context ctx, String urlString, boolean refresh,
                                final HttpRequestHandler<ArrayList<String>> handler) {
        requestLoginoutURLString(ctx, urlString, new HttpRequestHandler<String>() {
            @Override
            public void onSuccess(String data, int totalPages, int currentPage) {
            }

            @Override
            public void onSuccess(String data) {
                boolean isSuccess = false;
                String message = null;
                try {
                    JSONObject json = new JSONObject(data);
                    isSuccess = json.getBoolean("isSuccess");
                    if (isSuccess) {
                        SafeHandler.onSuccess(handler, null);
                    } else {
                        SafeHandler.onFailure(handler, message);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onFailure(String error) {
                SafeHandler.onFailure(handler, error);
            }
        });
    }


    private static void requestLoginoutURLString(final Context cxt, final String urlstring,
                                                 final HttpRequestHandler<String> handler) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(cxt, urlstring, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                SafeHandler.onFailure(handler, responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (statusCode == 200) {
                    SafeHandler.onSuccess(handler, responseString);
                }
            }
        });
    }


    /**
     * 退出登录
     *
     * @param context
     */
    public static void logout(Context context) {
        PersistentCookieStore cookieStore = new PersistentCookieStore(context);
        cookieStore.clear();
    }
}
