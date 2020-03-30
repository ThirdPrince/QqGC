package com.yq.news.net;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.luck.picture.lib.tools.SdkVersionUtils;
import com.yq.news.model.SubscribleInfo;
import com.yq.util.ManagerUtlis;
import com.yq.util.Md5Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * OKhttp 请求，需求简单
 */
public class OkHttpManager  implements INetManager{
   /* public static final String HOST="http://localhost/health/phone";
    public static final String URL_LOGIN="/login.action";
    public static final String URL_LIST="/list.action";*/

    private static final String TAG = "OkHttpManager";

    private static OkHttpManager mInstance;
    private OkHttpClient mHttpClient;
    private Gson mGson;
    private MediaType mime;
    private static Handler mHandler ;

    private OkHttpManager() {
        mHttpClient = new okhttp3.OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS).build();
        mGson = new Gson();
        mime = MediaType.parse("text/json;charset=utf-8");
        mHandler = new Handler(Looper.getMainLooper());
        //cookie enabled
    }

    /**
     * 单例
     *
     * @return
     */
    public static OkHttpManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpManager();
                }
            }
        }
        return mInstance;
    }


    /**
     * 获取token
     * @param url
     * @param netCallBack
     */
    public  void getToken( final NetCallBack netCallBack)
    {
        String apiUrl = ApiUtils.BASE_URL + "/api/auth/gettoken?apikey=reportapikey";
        Log.e(TAG,"apiUrl::"+apiUrl);
        get(apiUrl,netCallBack);

    }

    /**
     * 验证Token
     * @param netCallBack
     */
    public  void validToken( final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String apiUrl = ApiUtils.BASE_URL + "/api/auth/validtoken?token="+tokenSpu;

        Log.e(TAG,"apiUrl::"+apiUrl);
        get(apiUrl,netCallBack);

    }

    /**
     * 获取部门列表
     * @param token
     * @param account
     * @param password
     * @param netCallBack
     */
    public  void getDeptList( final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String deptId =   SPUtils.getInstance().getString("deptId");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        //String apiUrl = ApiUtils.BASE_URL + "/api/dept/list?token="+tokenSpu;
        String apiUrl = ApiUtils.BASE_URL + "/api/task/userlist?token="+tokenSpu+"&userId="+uIdSpu+"&deptId="+deptId;
        Log.e(TAG,"apiUrl::"+apiUrl);
        get(apiUrl,netCallBack);

    }

    public  void getIndustry(final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        String apiUrl = ApiUtils.BASE_URL + "/api/industry/list?token="+tokenSpu;
        get(apiUrl,netCallBack);
    }

    public  void getArea(final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String apiUrl = ApiUtils.BASE_URL + "/api/area/list?token="+tokenSpu;
        Log.e(TAG,"apiUrl::"+apiUrl);
        get(apiUrl,netCallBack);
    }

    public  void register(String account,String password,String realname,String telphone,String email,String deptId,String industryId,String areacode,String remark, final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String md5Password = Md5Utils.toMd5(password);
        String apiUrl = ApiUtils.BASE_URL +
                "/api/registeruser/add_registeruser?token="+tokenSpu+"&account="+account+"&password="+md5Password+"&realname="+realname+"&telphone="+telphone+
                "&email="+email+"&deptid="+deptId+"&remark="+remark+"&industryId="+industryId+"&areacode="+areacode;
        Log.e(TAG,"apiUrl::"+apiUrl);
        get(apiUrl,netCallBack);

    }


    /**
     * 登錄
     * @param token
     * @param account
     * @param password
     * @param netCallBack
     */
    public void login(String token, final String account, final String password,final NetCallBack netCallBack ) {
        String md5Password = Md5Utils.toMd5(password);
        String equipment  = "";

        if(SdkVersionUtils.checkedAndroid_Q())
        {
            String esn =   SPUtils.getInstance().getString("esn");
            equipment =  DeviceUtils.getUniqueDeviceId();
            if(!TextUtils.isEmpty(esn))
            {
                equipment = esn ;
            }

        }else
        {
            equipment = PhoneUtils.getDeviceId();
            SPUtils.getInstance().put("esn",equipment);
        }
        String apiUrl = ApiUtils.BASE_URL + "/api/auth/login?token="+token+"&account="+account+"&password="+md5Password+"&equipment="+equipment;
        Log.e(TAG,"apiUrl:"+apiUrl);
        get(apiUrl,netCallBack);

    }

    /**
     * 忘记密码参数
     */
    public  void sysConfig(final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        //String uIdSpu = SPUtils.getInstance().getString("uid");
        String apiUrl = ApiUtils.BASE_URL + "/api/system/config?token="+tokenSpu;
        Log.e(TAG,"apiUrl::"+apiUrl);
        get(apiUrl,netCallBack);
    }

    /**
     * 登錄詳情
     * @param netCallBack
     */
    public void loginInfo( final NetCallBack netCallBack ) {
       // String md5Password = Md5Utils.toMd5(password);
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        String apiUrl = ApiUtils.BASE_URL + "/api/user/info?token="+tokenSpu+"&userid="+uIdSpu;

        get(apiUrl,netCallBack);

    }


    /**
     * 获取收藏文章列表
     */

    public  void getCollectionDataList(String pageNo,String pageSize,final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        String apiUrl = ApiUtils.BASE_URL + "/api/article/collection_data_list?token="+tokenSpu+"&userid="+uIdSpu+"&pageNum="+pageNo+"&pageSize="+pageSize;
        Log.e(TAG,"apiUrl::"+apiUrl);
        get(apiUrl,netCallBack);

    }

    /**
     * 获取点赞文章列表
     */

    public  void getPointDataList(String pageNo,String pageSize,final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        String apiUrl = ApiUtils.BASE_URL + "/api/article/point_data_list?token="+tokenSpu+"&userid="+uIdSpu+"&pageNum="+pageNo+"&pageSize="+pageSize;
        Log.e(TAG,"apiUrl::"+apiUrl);
        get(apiUrl,netCallBack);

    }

    /**
     * 获取分类
     */

    public  void getClassDataList(String token,String uid, final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        String apiUrl = ApiUtils.BASE_URL + "/api/class/data_list?token="+tokenSpu+"&userid="+uIdSpu+"&id=1";
        Log.e(TAG,"apiUrl::"+apiUrl);
        get(apiUrl,netCallBack);

    }


    /**
     * 订阅
     * subscribe = true 订阅 false 取消订阅
     */

    public  void subscribeClass(boolean subscribe,String classId, final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        String parm = "/api/class/subscribe_class?";
        if(!subscribe)
        {
            parm = "/api/class/cancle_subscribe_class?";
        }
        String apiUrl = ApiUtils.BASE_URL + parm+"token="+tokenSpu+"&userid="+uIdSpu+"&classid="+classId;

        Log.e(TAG,"apiUrl::"+apiUrl);
        get(apiUrl,netCallBack);

    }

    /**
     * 首页
     * @param netCallBack
     */
    public  void getSubscribleDataList(final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        String apiUrl = ApiUtils.BASE_URL + "/api/class/class_subscribed_list?token="+tokenSpu+"&userid="+uIdSpu;
        Log.e(TAG,"apiUrl::"+apiUrl);
        get(apiUrl,netCallBack);

    }

    /**
     * 详情
     * @param netCallBack
     */
    public  void getArticleDetailList(String articleId,final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        String apiUrl = ApiUtils.BASE_URL + "/api/article/article_detail?token="+tokenSpu+"&userid="+uIdSpu+"&id="+articleId;

        Log.e(TAG,"apiUrl::"+apiUrl);
        get(apiUrl,netCallBack);

    }

    /**
     * 操作文章接口
     */
    public  void operateArticle(String articleId,String type,final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        String apiUrl = ApiUtils.BASE_URL + "/api/article/operate_article?token="+tokenSpu+"&userid="+uIdSpu+"&articleid="+articleId+"&type="+type;
        Log.e(TAG,"apiUrl::"+apiUrl);
        get(apiUrl,netCallBack);

    }

    /**
     * 获取H5 里的图片
     * @param pageNo
     * @param pageSize
     * @param classId
     * @param keyWord
     * @param states
     * @param netCallBack
     */
    public  void getArticleDatailPic(String classId, final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        String apiUrl = ApiUtils.BASE_URL + "/api/mobile/article/picture/list?token="+tokenSpu+"&articleid="+classId+"&userid="+uIdSpu;
        //  "class/data_list?token="+tokenSpu+"&userid="+uIdSpu;
        Log.e(TAG,"apiUrl::"+apiUrl);
        get(apiUrl,netCallBack);

    }

    /**
     * 获取分类
     */

    public  void getTaskList(String pageNo,String pageSize,String searchType,String keyWord,String states, final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        String roleId = SPUtils.getInstance().getString("roleid");
        String apiUrl = null;
        if(ManagerUtlis.isManager())
        {
             apiUrl = ApiUtils.BASE_URL + "/api/task/list?token="+tokenSpu+"&pageNum="+pageNo+"&pageSize="+pageSize+"&searchType="+searchType+"&keyword="+keyWord+"&userid="+uIdSpu
                    +"&states="+states +"&roleid="+roleId;
        }else
        {
            apiUrl = ApiUtils.BASE_URL + "/api/task/list?token="+tokenSpu+"&pageNum="+pageNo+"&pageSize="+pageSize+"&searchType="+searchType+"&keyword="+keyWord+"&userid="+uIdSpu
                    +"&states="+states +"&roleid="+roleId+"&cLoginUserId="+uIdSpu;
        }

              //  "class/data_list?token="+tokenSpu+"&userid="+uIdSpu;

        Log.e(TAG,"apiUrl::"+apiUrl);
        get(apiUrl,netCallBack);

    }
    /**
     * 获取分类
     */

    public  void getTaskDetail(String id, final NetCallBack netCallBack)
    {
        if(TextUtils.isEmpty(id))
        {
            return;
        }
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        String apiUrl = null ;
        if(ManagerUtlis.isManager())
        {
            apiUrl = ApiUtils.BASE_URL + "/api/task/detail?token="+tokenSpu +"&id="+id;
        }else
        {
            apiUrl = ApiUtils.BASE_URL + "/api/task/detail?token="+tokenSpu+"&userId="+uIdSpu +"&id="+id;
        }

        //  "class/data_list?token="+tokenSpu+"&userid="+uIdSpu;
        Log.e(TAG,"apiUrl::"+apiUrl);
        get(apiUrl,netCallBack);

    }

    public  void updateClassSort(List<SubscribleInfo.ListBean> listBean ,  final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        JSONArray jsonArray = new JSONArray();
        if(listBean != null)
        {
          for(int i = 0;i< listBean.size();i++)
          {
              SubscribleInfo.ListBean bean = listBean.get(i);
              JSONObject json = new JSONObject();

              try {
                  json.put("id", bean.getId());
                  json.put("sort", (i+2));
                  json.put("userid", uIdSpu);
                  jsonArray.put(json);
              } catch (JSONException e) {
                  e.printStackTrace();
              }
          }
        }
        String apiUrl = null;
        try {
            apiUrl = ApiUtils.BASE_URL + "/api/class/update_class_sort?token="+tokenSpu+"&classlistjson="+ URLEncoder.encode(jsonArray.toString(),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.e(TAG,"apiUrl="+apiUrl);
        FormBody.Builder builder = new FormBody.Builder();
        if(listBean != null)
        {
            for(int i = 0;i< listBean.size();i++)
            {
                SubscribleInfo.ListBean bean = listBean.get(i);
                builder.add("id", bean.getId()+"");
                builder.add("sort", (i+2)+"");
                builder.add("userid", uIdSpu);
            }
        }
        FormBody formBody = builder.build();

        Request.Builder builder1 = new Request.Builder();
        builder1.url(apiUrl);
        builder1.put(formBody);
        Log.e(TAG,"apiUrl::"+apiUrl);
       //get(apiUrl,netCallBack);
        post(builder1,netCallBack);
    }


    /**
     *
     * 获总任务
     */
    public  void getTotalTask(final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        String deptId =   SPUtils.getInstance().getString("deptId");
        String apiUrl = null;
        if(ManagerUtlis.isManager())
        {
            apiUrl = ApiUtils.BASE_URL + "/api/task/total?token="+tokenSpu+"&userid="+uIdSpu+"&deptId="+deptId;
        }else
        {
            apiUrl = ApiUtils.BASE_URL + "/api/task/total?token="+tokenSpu+"&userid="+uIdSpu+"&cLoginUserId="+uIdSpu;
        }

        Log.e(TAG,"apiUrl::"+apiUrl);
        get(apiUrl,netCallBack);
    }

    /**
     * 未结束任务总数接口
     */
    public  void unFinishTotal(final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        String deptId =   SPUtils.getInstance().getString("deptId");

        String apiUrl = null;
        if(ManagerUtlis.isManager())
        {
             apiUrl = ApiUtils.BASE_URL + "/api/task/unFinishedCount?token="+tokenSpu+"&deptId="+deptId;
        }else
        {
            apiUrl = ApiUtils.BASE_URL + "/api/task/unFinishedCount?token="+tokenSpu+"&userId="+uIdSpu+"&cLoginUserId="+uIdSpu;
        }

        Log.e(TAG,"apiUrl::"+apiUrl);
        get(apiUrl,netCallBack);
    }

    /**
     * 超时任务总数接口
     * @param netCallBack
     */
    public  void outTimeTotal(final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        String apiUrl = ApiUtils.BASE_URL + "/api/task/outTimeTotal?token="+tokenSpu+"&userid="+uIdSpu;
        Log.e(TAG,"apiUrl::"+apiUrl);
        get(apiUrl,netCallBack);
    }


    /**
     * 綁定手機 解除綁定
     * @param bind
     * @param netCallBack
     */
    public  void bindOrRebindPhone(boolean bind, final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        String bindParm ="";
        String apiUrl = "" ;
        if(bind)
        {
            bindParm ="bind";
            apiUrl = ApiUtils.BASE_URL + "/api/user/"+bindParm+"?token="+tokenSpu;
        }else
        {
            bindParm ="unbind";
            apiUrl = ApiUtils.BASE_URL + "/api/user/"+bindParm+"?token="+tokenSpu+"&userid="+uIdSpu+"&equipment="+PhoneUtils.getDeviceId();
        }

        Log.e(TAG,"apiUrl::"+apiUrl);
        FormBody formBody = new FormBody.Builder()
                .add("userid",uIdSpu)
                .add("equipment", PhoneUtils.getDeviceId())
                .build();
        Request.Builder builder = new Request.Builder();
        builder.url(apiUrl);
        if(bind)
        {
            builder.post(formBody);
        }else
        {
            builder.delete();
        }

        Request request =  builder.build();
        Call call = mHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                e.printStackTrace();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (netCallBack != null) {
                            rspFail(netCallBack);
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String rsp = response.body().string();
                rspCallBack(rsp, netCallBack);
            }
        });
    }

    /**
     * 修改密码
     * @param bind
     * @param netCallBack
     */
    public  void modifyPwd(String oldPwd,String newPwd, final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        String oldPwdMd5 = Md5Utils.toMd5(oldPwd);
        String newPwdMd5 = Md5Utils.toMd5(newPwd);
        String apiUrl = ApiUtils.BASE_URL + "/api/user/password?token="+tokenSpu+"&userid="+uIdSpu+"&oldpassword="
                +oldPwdMd5+"&newpassword="+newPwdMd5;
                //+"&userid="+uIdSpu;
        Log.e(TAG,"url:"+apiUrl);


        FormBody formBody = new FormBody.Builder()
              /*  .add("userid",uIdSpu)
                .add("oldpassword",oldPwdMd5)
                .add("newpassword",newPwdMd5)*/
                .build();
        Request.Builder builder = new Request.Builder();
        builder.url(apiUrl);
        builder.put(formBody);
        Request request =  builder.build();
        Call call = mHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                e.printStackTrace();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (netCallBack != null) {
                            rspFail(netCallBack);
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String rsp = response.body().string();

                Log.e(TAG,"rsp::"+rsp);
                try {
                    final JSONObject jsonObject   = new JSONObject(rsp);
                    final int status = jsonObject.getInt("status");
                    final boolean dataIsNull =  jsonObject.isNull("data");
                    final  String msg = jsonObject.getString("msg");
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (status == 200   ) {

                                if(netCallBack != null &&!dataIsNull ) {

                                    try {
                                        final boolean data = jsonObject.getBoolean("data");
                                        if(data)
                                        {
                                            netCallBack.success(rsp);
                                        }else
                                        {
                                            netCallBack.failed(msg);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        netCallBack.failed(e.getMessage());
                                    }


                                }

                            } else {
                                if(netCallBack != null) {
                                    netCallBack.failed(msg);
                                }
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    if(netCallBack != null) {
                        netCallBack.failed(e.getMessage());
                    }
                }
               // rspCallBack(rsp, netCallBack);
            }
        });
    }



    /**
     * 上传 regID
     * @param
     * @param netCallBack
     */
    public  void putRegID(String regid,String mobiletype, final NetCallBack netCallBack)
    {
        if(TextUtils.isEmpty(regid))
        {
            return;
        }
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");

        String apiUrl = ApiUtils.BASE_URL + "/api/user/mobile?token="+tokenSpu+"&userid="+uIdSpu+"&regid="
                +regid+"&mobiletype="+mobiletype;
        //+"&userid="+uIdSpu;
        Log.e(TAG,"url:"+apiUrl);
        FormBody formBody = new FormBody.Builder()
                .build();
        Request.Builder builder = new Request.Builder();
        builder.url(apiUrl);
        builder.put(formBody);
        Request request =  builder.build();
        Call call = mHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                e.printStackTrace();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (netCallBack != null) {
                            rspFail(netCallBack);
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String rsp = response.body().string();

                Log.e(TAG,"rsp::"+rsp);
                try {
                    final JSONObject jsonObject   = new JSONObject(rsp);
                    final int status = jsonObject.getInt("status");
                    final boolean dataIsNull =  jsonObject.isNull("data");
                    final  String msg = jsonObject.getString("msg");
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (status == 200   ) {

                                if(netCallBack != null &&!dataIsNull ) {

                                    try {
                                        final boolean data = jsonObject.getBoolean("data");
                                        if(data)
                                        {
                                            netCallBack.success(rsp);
                                        }else
                                        {
                                            netCallBack.failed(msg);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        netCallBack.failed(e.getMessage());
                                    }


                                }

                            } else {
                                if(netCallBack != null) {
                                    netCallBack.failed(msg);
                                }
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    if(netCallBack != null) {
                        netCallBack.failed(e.getMessage());
                    }
                }
                // rspCallBack(rsp, netCallBack);
            }
        });
    }

    public  void updateApp(final NetCallBack netCallBack)
    {

        String tokenSpu = SPUtils.getInstance().getString("token");
        //String apiUrl = "http://59.110.162.30/app_updater_version.json";
        String apiUrl = ApiUtils.BASE_URL + "/api/system/app-info?token="+tokenSpu;
        Log.e(TAG,"url:"+apiUrl);
        get(apiUrl,netCallBack);

    }

    /**
     *  获取账号列表
     * @param pageNo
     * @param pageSize
     * @param netCallBack
     */
    public  void getAccountDataList(String pageNo,String pageSize,final NetCallBack netCallBack)
    {
        String deptId =   SPUtils.getInstance().getString("deptId");
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        String apiUrl = ApiUtils.BASE_URL + "/api/account/list?token="+tokenSpu+"&userId="+uIdSpu+"&pageNum="+pageNo+"&pageSize="+pageSize+"&deptId="+deptId;
        Log.e(TAG,"apiUrl::"+apiUrl);
        get(apiUrl,netCallBack);

    }

    /**
     * 全部账号接口
     */
    public  void getAllAccountList(String accountType,final NetCallBack netCallBack)
    {
        String deptId =   SPUtils.getInstance().getString("deptId");
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        String apiUrl = ApiUtils.BASE_URL + "/api/account/alllist?token="+tokenSpu+"&userId="+uIdSpu+"&accountType="+accountType+"&deptId="+deptId;
        Log.e(TAG,"apiUrl::"+apiUrl);
        get(apiUrl,netCallBack);

    }
    /**
     *  获取账号列表
     * @param pageNo
     * @param pageSize
     * @param netCallBack
     */
    public  void getAllList(String pageNo,String pageSize,final NetCallBack netCallBack)
    {
        String deptId =   SPUtils.getInstance().getString("deptId");
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        String apiUrl = ApiUtils.BASE_URL + "/api/account/alllist?token="+tokenSpu+"&userId="+uIdSpu+"&pageNum="+pageNo+"&pageSize="+pageSize+"&deptId="+deptId;
        Log.e(TAG,"apiUrl::"+apiUrl);
        get(apiUrl,netCallBack);

    }

    /**
     * 账号类型字典接口
     * @param pageNo
     * @param pageSize
     * @param netCallBack
     */
    public  void getAccountTypeList(final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        String apiUrl = ApiUtils.BASE_URL + "/api/account/accounttype/list?token="+tokenSpu+"&userId="+uIdSpu;
        Log.e(TAG,"apiUrl::"+apiUrl);
        get(apiUrl,netCallBack);

    }

    /**
     * 任务已反馈列表接口
     * @param netCallBack
     */
    public  void getRecordList(String taskId ,final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        String apiUrl = null;
        if(ManagerUtlis.isManager())
        {
             apiUrl = ApiUtils.BASE_URL + "/api/task/record/list?token="+tokenSpu+"&taskId="+taskId+"&pageNum=1"+"&pageSize=10000"+"&orderBy="+"createtime desc";
        }else
        {
             apiUrl = ApiUtils.BASE_URL + "/api/task/record/list?token="+tokenSpu+"&userId="+uIdSpu+"&taskId="+taskId+"&pageNum=1"+"&pageSize=10000"+"&orderBy="+"createtime desc";
        }

        Log.e(TAG,"apiUrl::"+apiUrl);
        get(apiUrl,netCallBack);

    }

    /**
     * 反馈记录用户信息接口
     * @param netCallBack
     */
    public  void getRecordUser(String ids ,final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
       // String uIdSpu = SPUtils.getInstance().getString("uid");
        String apiUrl = null;
        apiUrl = ApiUtils.BASE_URL + "/api/task/record/user?token="+tokenSpu+"&ids="+ids;
        Log.e(TAG,"apiUrl::"+apiUrl);
        get(apiUrl,netCallBack);

    }

    /**
     * 反馈记录账号信息接口
     * @param netCallBack
     */
    public  void getRecordAccount(String ids ,final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        String apiUrl = null;

        apiUrl = ApiUtils.BASE_URL + "/api/task/record/account?token="+tokenSpu+"&userId="+uIdSpu+"&ids="+ids;
        Log.e(TAG,"apiUrl::"+apiUrl);
        get(apiUrl,netCallBack);

    }

    /**
     * 任务未反馈列表接口
     * @param netCallBack
     */
    public  void getUserList(String taskId ,final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        String deptId =   SPUtils.getInstance().getString("deptId");
        String apiUrl = null;
        if(ManagerUtlis.isManager())
        {
            apiUrl = ApiUtils.BASE_URL + "/api/task/record/userlist?token="+tokenSpu+"&taskid="+taskId+"&deptid="+deptId;
        }

        Log.e(TAG,"apiUrl::"+apiUrl);
        get(apiUrl,netCallBack);

    }

    /**
     * 消息列表接口
     */

    public  void getMessageList(String isRead,final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        String apiUrl = ApiUtils.BASE_URL + "/api/message/list?token="+tokenSpu+"&userId="+uIdSpu+"&receiveid="+uIdSpu+"&isread="+isRead;
        Log.e(TAG,"apiUrl::"+apiUrl);
        get(apiUrl,netCallBack);

    }

    /**
     * 消息置为已读接口
     */

    public  void postMsgRead(String id,String read, final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        String apiUrl = ApiUtils.BASE_URL + "/api/message/update?token="+tokenSpu+"&ids="+id+"&isread="+
                read+"&userId="+uIdSpu;
        //+"&userid="+uIdSpu;
        Log.e(TAG,"url:"+apiUrl);

        get(apiUrl,netCallBack);

       /* FormBody.Builder builder = new FormBody.Builder();
        FormBody formBody = builder
                .build();
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(apiUrl);
        requestBuilder.post(formBody);
        //Request request =  requestBuilder.build();
        post(requestBuilder,netCallBack);*/
    }

    /**
     * 添加反馈接口
     */

    public void addFeedBack(String taskId, String accountId
                                          ,String  uploadImage, final NetCallBack netCallBack) {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");

        String apiUrl = ApiUtils.BASE_URL + "/api/task/record/add" ;
               // "?token="+tokenSpu+"taskId"+taskId+"&userId="+uIdSpu+"&accountId="+accountId;
        MediaType mime = MediaType.parse("application/octet-stream");
        MultipartBody.Builder multipartBodyBuilder =
                new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);
        multipartBodyBuilder .addFormDataPart("token", tokenSpu);
        multipartBodyBuilder .addFormDataPart("taskId", taskId);
        multipartBodyBuilder .addFormDataPart("userId", uIdSpu);
        multipartBodyBuilder .addFormDataPart("accountId", accountId);
        multipartBodyBuilder .addFormDataPart("name", System.currentTimeMillis() + ".jpg");


        if (uploadImage != null) {
            byte[] data  = file2byte(uploadImage) ;
            RequestBody imageDataBody = RequestBody.create(mime, data);
            multipartBodyBuilder.addFormDataPart("file", System.currentTimeMillis() + ".jpg", imageDataBody);
        }

        RequestBody requestBody = multipartBodyBuilder.build();
        Request request = new Request.Builder()
                .url(apiUrl)
                .post(requestBody)
                .build();
        Call call = mHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                e.printStackTrace();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (netCallBack != null) {
                            netCallBack.failed(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String rsp = response.body().string();
                rspCallBack(rsp, netCallBack);
            }
        });
    }


    /**
     * 编辑反馈接口
     */

    public void editFeedBack(String id,String taskId, String accountId
            ,String  uploadImage, final NetCallBack netCallBack) {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");

        String apiUrl = ApiUtils.BASE_URL + "/api/task/record/edit" ;
        // "?token="+tokenSpu+"taskId"+taskId+"&userId="+uIdSpu+"&accountId="+accountId;
        MediaType mime = MediaType.parse("application/octet-stream");
        MultipartBody.Builder multipartBodyBuilder =
                new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);
        multipartBodyBuilder .addFormDataPart("id", id);
        multipartBodyBuilder .addFormDataPart("token", tokenSpu);
        multipartBodyBuilder .addFormDataPart("taskId", taskId);
        multipartBodyBuilder .addFormDataPart("userId", uIdSpu);
        multipartBodyBuilder .addFormDataPart("accountId", accountId);
        if(!TextUtils.isEmpty(uploadImage)) {
            multipartBodyBuilder.addFormDataPart("name", System.currentTimeMillis() + ".jpg");
                byte[] data  = file2byte(uploadImage) ;
                RequestBody imageDataBody = RequestBody.create(mime, data);
                multipartBodyBuilder.addFormDataPart("file", System.currentTimeMillis() + ".jpg", imageDataBody);
        }




        RequestBody requestBody = multipartBodyBuilder.build();
        Request request = new Request.Builder()
                .url(apiUrl)
                .post(requestBody)
                .build();
        Call call = mHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                e.printStackTrace();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (netCallBack != null) {
                            netCallBack.failed(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String rsp = response.body().string();
                rspCallBack(rsp, netCallBack);
            }
        });
    }

    /**
     * 删除账号
     * @param id
     * @param netCallBack
     */
    public  void deleFeedBack(String id ,final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        String apiUrl = ApiUtils.BASE_URL + "/api/task/record/delete?token="+tokenSpu+"&userId="+uIdSpu+"&id="+id;
        Log.e(TAG,"apiUrl::"+apiUrl);
        get(apiUrl,netCallBack);

    }

    /**
     *
     */

    public void get(String url,final NetCallBack netCallBack)
    {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                e.printStackTrace();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(netCallBack != null) {
                            rspFail(netCallBack);
                        }
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String rsp = response.body().string();
                rspCallBack(rsp,netCallBack);
            }
        });
    }

    @Override
    public void download(String url, File targetFile, INetDownloadCallBack callBack, Object tag) {

        if(!targetFile.exists())
        {
            targetFile.getParentFile().mkdirs();
        }
        Request request = new Request.Builder().url(url).tag(tag).build();

        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(callBack != null)
                        {
                            callBack.failed(e);
                        }
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response)throws IOException  {


                InputStream inputStream = null;
                OutputStream outputStream = null;
                try {
                    inputStream = response.body().byteStream();
                    outputStream = new FileOutputStream(targetFile);

                    final long contentLength = response.body().contentLength();
                    int len = 0;
                    int sum = 0;
                    byte[] bytes = new byte[1024*12];
                    while ((len = inputStream.read(bytes))!= -1 )
                    {
                        sum += len;
                        outputStream.write(bytes,0,len);
                        outputStream.flush();
                        final int sumLen = sum ;
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.progress((int) ((sumLen * 1.0f/contentLength*100)) +"%" );
                            }
                        });

                    }
                    targetFile.setExecutable(true,false);
                    targetFile.setReadable(true,false);
                    targetFile.setWritable(true,false);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.success(targetFile);
                        }
                    });
                } catch (final Exception e) {
                    e.printStackTrace();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.failed(e);
                        }
                    });
                }finally {

                    if(inputStream != null)
                    {
                        inputStream.close();
                    }
                    if(outputStream != null)
                    {
                        outputStream.close();
                    }
                }


            }
        });
    }

    @Override
    public void cancel(Object tag) {

    }


    /**
     *
     */
    /**
     * 账号管理新增
     * @param
     * @param netCallBack
     */
    public  void addAccount(String accountType,String accountId,String nickName,String realName, final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        String apiUrl = ApiUtils.BASE_URL + "/api/account/add?token="+tokenSpu+"&accountType="+accountType+"&accountId="+
                accountId+"&creator="+uIdSpu+"&nickname="+nickName+"&realName="+realName;
        //+"&userid="+uIdSpu;
        Log.e(TAG,"url:"+apiUrl);

        FormBody.Builder builder = new FormBody.Builder();
        FormBody formBody = builder
                .build();
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(apiUrl);
        requestBuilder.post(formBody);
        //Request request =  requestBuilder.build();
        post(requestBuilder,netCallBack);
    }


    /**
     * 删除 Task
     * @param
     * @param netCallBack
     */
    public  void deleteTask(String id, final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String apiUrl = ApiUtils.BASE_URL + "/api/task/delete?token="+tokenSpu+"&id="+id;
        Log.e(TAG,"url:"+apiUrl);

        FormBody.Builder builder = new FormBody.Builder();
        FormBody formBody = builder
                .build();
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(apiUrl);
        requestBuilder.post(formBody);
        post(requestBuilder,netCallBack);
    }

    /**
     * 全部账号接口
     */



    /**
     * 添加任务
     * @param
     * @param netCallBack
     */
    public  void addTaskApi(String userIds ,String name,String description,String taskType ,String startTimeStr,String endTimeStr,String pilotCount, final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        String deptId =   SPUtils.getInstance().getString("deptId");
        String apiUrl = ApiUtils.BASE_URL + "/api/task/add?token="+tokenSpu+"&name="+name+"&description="+
                description+"&userId="+uIdSpu+"&type="+taskType+"&startTimeStr="+startTimeStr+"&endTimeStr="+endTimeStr+"&deptId="+deptId+"&userIds="+userIds+"&pilotCount="+pilotCount;
        //+"&userid="+uIdSpu;
        Log.e(TAG,"url:"+apiUrl);
        FormBody.Builder builder = new FormBody.Builder();
        FormBody formBody = builder
                .build();
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(apiUrl);
        requestBuilder.post(formBody);
        //Request request =  requestBuilder.build();
        post(requestBuilder,netCallBack);
    }

    /**
     * 编辑任务
     * @param userIds
     * @param name
     * @param description
     * @param startTimeStr
     * @param endTimeStr
     * @param pilotCount
     * @param netCallBack
     */
    public  void editTaskApi(String userIds ,String name,String description,String type,String startTimeStr,String endTimeStr,String pilotCount,String id, final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        String deptId =   SPUtils.getInstance().getString("deptId");
        String apiUrl = ApiUtils.BASE_URL + "/api/task/edit?token="+tokenSpu+"&name="+name+"&description="+
                description+"&userId="+uIdSpu+"&type="+type+"&startTimeStr="+startTimeStr+"&endTimeStr="+endTimeStr+"&deptId="+deptId+"&userIds="+userIds+"&pilotCount="+pilotCount+"&id="+id;
        //+"&userid="+uIdSpu;
        Log.e(TAG,"url:"+apiUrl);
        FormBody.Builder builder = new FormBody.Builder();
        FormBody formBody = builder
                .build();
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(apiUrl);
        requestBuilder.post(formBody);
        //Request request =  requestBuilder.build();
        post(requestBuilder,netCallBack);
    }

    public  void editAccount(String id,String accountType,String accountId,String nickName,String realName, final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        String apiUrl = ApiUtils.BASE_URL + "/api/account/edit?token="+tokenSpu+"&accountType="+accountType+"&accountId="+
                accountId+"&creator="+uIdSpu+"&nickname="+nickName+"&realName="+realName+"&id="+id;
        //+"&userid="+uIdSpu;
        Log.e(TAG,"url:"+apiUrl);

        Map map = new HashMap<>();
        map.put("accountType", accountType);
        map.put("accountId", accountId);
        map.put("creator", uIdSpu);
        map.put("nickname", nickName);
        map.put("realName", realName);
        map.put("id", id);
        String param= mGson.toJson(map);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), param);


        FormBody.Builder builder = new FormBody.Builder();
        FormBody formBody = builder
                .build();
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(apiUrl);
        requestBuilder.post(formBody);
        post(requestBuilder,netCallBack);
    }

    /**
     * 删除账号
     * @param id
     * @param netCallBack
     */
    public  void deleAccount(String id ,final NetCallBack netCallBack)
    {
        String tokenSpu = SPUtils.getInstance().getString("token");
        String uIdSpu = SPUtils.getInstance().getString("uid");
        String apiUrl = ApiUtils.BASE_URL + "/api/account/delete?token="+tokenSpu+"&userId="+uIdSpu+"&id="+id;
        Log.e(TAG,"apiUrl::"+apiUrl);
        get(apiUrl,netCallBack);

    }
    private void post(Request.Builder builder,final NetCallBack netCallBack)
    {
        Request request =  builder.build();

        Call call = mHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                e.printStackTrace();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(netCallBack != null) {
                            rspFail(netCallBack);
                        }
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String rsp = response.body().string();
                rspCallBack(rsp,netCallBack);
            }
        });
    }

    private void rspFail(NetCallBack netCallBack)
    {
        netCallBack.failed("连接服务器失败，请检查网络");
    }

    /**
     *  提取  公共 response
     * @param rsp
     * @param netCallBack
     */
    public void rspCallBack(final String rsp, final NetCallBack netCallBack)
    {
        JSONObject jsonObject = null;
        Log.e(TAG,"rsp::"+rsp);
        try {
            jsonObject   = new JSONObject(rsp);
            final int status = jsonObject.getInt("status");
            final boolean dataIsNull =  jsonObject.isNull("data");

            final  String msg = jsonObject.getString("msg");
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (status == 200   ) {

                        if(netCallBack != null &&!dataIsNull ) {
                            netCallBack.success(rsp);
                        }

                        if(netCallBack != null &&dataIsNull ) {
                            netCallBack.failed(msg);
                        }

                    } else {
                        if(netCallBack != null) {
                            netCallBack.failed(msg);
                        }
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            if(netCallBack != null) {
                netCallBack.failed(e.getMessage());
            }
        }
    }

    public static byte[] file2byte(String filePath)
    {
        byte[] buffer = null;
        try
        {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return buffer;
    }

}
