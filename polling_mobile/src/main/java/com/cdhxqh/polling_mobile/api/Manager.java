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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据信息处理类*
 */

public class Manager {
    private static final String TAG = "PollManager";
    private static final String TAG1 = "PollManager1";

    private static Application mApp = Application.getInstance();

    private static final PollDataSource mDataSource = Application.getDataSource();

    private static AsyncHttpClient sClient = null;
    /**旧**/
    private static final String HTTP_API_URL = "http://123.56.84.244:9090/hxqh/tdapi/";
    /**新**/
//    private static final String HTTP_API_URL = "http://192.168.169.103:8080/hxqh/tdapi/";

    /**
     * 登陆接口*
     */
    private static final String SIGN_IN_URL = HTTP_API_URL + "user/login";
    /**
     * 根据登陆账号获取巡检任务单*
     */
    private static final String INSPECT_TICKET_URL = HTTP_API_URL + "inspect/inspect_ticket/today/list";
    /**
     * 获取单个巡检任务单*
     */
    private static final String INSPECT_DEVICE_URL = HTTP_API_URL + "inspect/inspect_ticket";
    /**
     * 上传巡检任务的结果*
     */
    private static final String UPLOAD_DEVICE_URL = HTTP_API_URL + "inspect/inspect_ticket/tickets";
    /**
     * 列出所有资产二级分类*
     */
    private static final String ASSET_ClASS_URL = HTTP_API_URL + "asset/class2/list";
    /**
     * 列出某二级分类下的所有资产三级分类*
     */
    private static final String ASSET_CHILD_CLASS_URL = HTTP_API_URL + "asset/class2/";
    /**列出某资产三级分类下所有设备**/
    private static final String ASSET_THREE_CLASS_URL = HTTP_API_URL + "asset/class3/";

    /**
     * 图片上传的URL*
     */
    public static final String IMAGE_URL = HTTP_API_URL + "inspect/inspect_ticket/images";
    /**
     * 登出管理*
     */
    private static final String LOGOUT_URL = HTTP_API_URL + "user/logout";

    /**
     * 下载用户登陆的巡检任务单列表*
     */

    //根据access_token获取巡检任务单**/
    public static void getInspect_ticket(Context ctx, final String access_token, boolean refresh,
                                         final HttpRequestHandler<ArrayList<Ins_task_ticket>> handler) {
        getInspect_tickets(ctx, INSPECT_TICKET_URL + "?access_token=" + access_token, refresh, handler);
    }


    /**
     * 获取所以资产分类*
     */
    public static void getAsset_class(Context ctx, final String access_token, boolean refresh,
                                      final HttpRequestHandler<ArrayList<String>> handler) {
        getAssetClass(ctx, ASSET_ClASS_URL + "?access_token=" + access_token, access_token,refresh, handler);

    }

    /**
     * 列出某二级分类下的所有资产三级分类*
     */
    public static void getAsset_Child_class(Context ctx, final String className, final String access_token, boolean refresh,
                                            final HttpRequestHandler<ArrayList<String>> handler) {
        String urlStr = ASSET_CHILD_CLASS_URL + className + "/list" + "?access_token=" + access_token;
        getAssetChildClass(ctx, urlStr,access_token, refresh, handler);

    }

    /**列出某资产三级分类下所有设备**/

    public static void getAsset_Three_class(Context ctx, final String className3, final String access_token, boolean refresh,
                                            final HttpRequestHandler<ArrayList<String>> handler) {
        String urlStr = ASSET_THREE_CLASS_URL + className3 + "/list" + "?access_token=" + access_token;

        getAssetThreeClass(ctx, urlStr, refresh, handler);

    }

    /**
     * 退出登陆*
     */
    public static void getLoginout(Context ctx, final String access_token, boolean refresh,
                                   final HttpRequestHandler<ArrayList<String>> handler) {
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
    public static void getAssetClass(final Context ctx, String urlString, final String access_token,boolean refresh,
                                     final HttpRequestHandler<ArrayList<String>> handler) {

        requestAssetURLString(ctx, urlString, new HttpRequestHandler<String>() {
            @Override
            public void onSuccess(String data, int totalPages, int currentPage) {
            }

            @Override
            public void onSuccess(String data) {
                List<Asset_class> asset_classes = JsonUtils.parsingAssetClass(ctx, data);
                for (int i=0;i<asset_classes.size();i++) {
                    String asset_name=asset_classes.get(0).className;
                    //根据资产分类获取三级资产
                    Manager.getAsset_Child_class(ctx,asset_name,access_token,true,new HttpRequestHandler<ArrayList<String>>() {
                        @Override
                        public void onSuccess(ArrayList<String> data) {
                            Log.i(TAG,"child***1");
                        }

                        @Override
                        public void onSuccess(ArrayList<String> data, int totalPages, int currentPage) {
                        }

                        @Override
                        public void onFailure(String error) {

                        }
                    } );


                }
                SafeHandler.onSuccess(handler,null);
            }



            @Override
            public void onFailure(String error) {
                SafeHandler.onFailure(handler, error);
            }
        });
    }

    /**
     * 获取资产三级分类
     *
     * @param ctx
     * @param urlString URL地址
     * @param refresh   是否从缓存中读取
     * @param handler   结果处理
     */
    public static void getAssetChildClass(final Context ctx, String urlString, final String access_token,boolean refresh,
                                          final HttpRequestHandler<ArrayList<String>> handler) {
        requestAssetURLString(ctx, urlString, new HttpRequestHandler<String>() {
            @Override
            public void onSuccess(String data, int totalPages, int currentPage) {
            }

            @Override
            public void onSuccess(String data) {
                List<Asset_two_class> asset_two_classes=JsonUtils.parsingAssetTwoClass(ctx,data);
                for(int i=0;i<asset_two_classes.size();i++){
                    String object_name=asset_two_classes.get(i).object_name;
                    String object_name_ch=asset_two_classes.get(i).object_name_ch;
                    Manager.getAsset_Three_class(ctx,object_name,access_token,true,new HttpRequestHandler<ArrayList<String>>() {
                        @Override
                        public void onSuccess(ArrayList<String> data) {
                        }

                        @Override
                        public void onSuccess(ArrayList<String> data, int totalPages, int currentPage) {
                        }

                        @Override
                        public void onFailure(String error) {
                            Log.i(TAG,"error"+error);
                        }
                    });

                }
            }


            @Override
            public void onFailure(String error) {
                SafeHandler.onFailure(handler, error);
            }
        });
    }
    /**
     * 获取3分类下的所有设备
     *
     * @param ctx
     * @param urlString URL地址
     * @param refresh   是否从缓存中读取
     * @param handler   结果处理
     */
    public static void getAssetThreeClass(final Context ctx, String urlString, boolean refresh,
                                          final HttpRequestHandler<ArrayList<String>> handler) {
        requestAssetURLString(ctx, urlString, new HttpRequestHandler<String>() {
            @Override
            public void onSuccess(String data, int totalPages, int currentPage) {
            }

            @Override
            public void onSuccess(String data) {
                List<Asset_three_class> list=JsonUtils.parsingAssetthreeClass(ctx,data);
                for (int i=0;i<list.size();i++){
                    String  assetNo=list.get(i).assetNo;
                    Log.i(TAG,"as*****="+assetNo);
                }
                mDataSource.isInsertThreeAsset((ArrayList<Asset_three_class>) list);
                SafeHandler.onSuccess(handler,null);
            }


            @Override
            public void onFailure(String error) {
                SafeHandler.onFailure(handler, error);
            }
        });
    }


    private static void requestAssetURLString(final Context cxt, final String urlstring,
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
     * 获取巡检任务单
     *
     * @param ctx
     * @param urlString URL地址
     * @param refresh   是否从缓存中读取
     * @param handler   结果处理
     */
    public static void getInspect_tickets(Context ctx, String urlString, boolean refresh,
                                          final HttpRequestHandler<ArrayList<Ins_task_ticket>> handler) {

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

        String urlString = INSPECT_DEVICE_URL + "/" + ticketID + "?access_token=" + access_token;

        Uri uri = Uri.parse(urlString);
        String path = uri.getLastPathSegment();
        String param = uri.getEncodedQuery();
        String key = path + ticketID;
        if (param != null)
            key += param;

        if (!refresh) {
            //尝试从缓存中加载
            ArrayList<Ins_task_device> topics = PersistenceHelper.loadModelList(cxt, key);
            if (topics != null && topics.size() > 0) {
                SafeHandler.onSuccess(handler, topics);
                return;
            }
        }

        new AsyncHttpClient().get(cxt, urlString,
                new WrappedJsonHttpResponseHandler<Ins_task_device>(cxt, Ins_task_device.class, key, handler));
    }


    /**
     * 上传巡检任务的结果*
     */
    public static boolean uploadTask(final Context cxt, final String ticket, final JSONArray result, final String access_token,
                                     final HttpRequestHandler<Integer> handler) {
        requestTaskWithURLString(cxt, ticket, result, access_token, new HttpRequestHandler<String>() {
            @Override
            public void onSuccess(String data, int totalPages, int currentPage) {


            }

            @Override
            public void onSuccess(String data) {
                SafeHandler.onSuccess(handler, null);
            }


            @Override
            public void onFailure(String error) {

                SafeHandler.onFailure(handler, error);
            }
        });
        return false;
    }


    /**
     * 提交巡检结果*
     */
    private static void requestTaskWithURLString(final Context cxt, final String ticket, final JSONArray insDeviceRecords, final String access_token,
                                                 final HttpRequestHandler<String> handler) {
        String URL = UPLOAD_DEVICE_URL + "?access_token=" + access_token;
        AsyncHttpClient client = new AsyncHttpClient();


        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < 1; i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("ticketID", ticket);
                jsonObject.put("insDeviceRecords", insDeviceRecords);
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
                    if (statusCode == 200) {
                        SafeHandler.onSuccess(handler, responseString);
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
