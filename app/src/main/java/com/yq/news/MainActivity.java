package com.yq.news;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.RomUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hms.aaid.HmsInstanceId;
import com.luck.picture.lib.tools.SdkVersionUtils;
import com.yq.news.activity.AddAccountActivity;
import com.yq.news.activity.AddTaskActivity;
import com.yq.news.activity.BaseActivity;
import com.yq.news.activity.GestureActivity;
import com.yq.news.activity.GestureActivitySetting;
import com.yq.news.activity.TaskInfoActivity;
import com.yq.news.app.MyApplication;
import com.yq.news.dialog.UpdateVersionShowDialog;
import com.yq.news.fragment.GcMainFragment;
import com.yq.news.fragment.GcPlaceRoomFragment;
import com.yq.news.fragment.MainFragment;
import com.yq.news.fragment.ManagerFragment;
import com.yq.news.fragment.MineFragment;
import com.yq.news.fragment.SubScribeFragment;
import com.yq.news.mipush.DemoMessageReceiver;
import com.yq.news.model.DownloadBean;
import com.yq.news.model.LoginInfo;
import com.yq.news.net.NetCallBack;
import com.yq.news.net.OkHttpManager;
import com.yq.util.AppUtils;
import com.yq.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author dhl
 * 主页面
 */
public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    /**
     * 华为
     */
    private String pushtoken;
    /**
     * 底部导航栏
     */
    private   BottomNavigationView navigation ;

    private View  add_task ;

    /**
     * 首页Fragment 普通账户
     *
     */

    private MainFragment mainFragment ;

    private GcMainFragment gcMainFragment ;

    private GcPlaceRoomFragment gcPlaceRoomFragment ;

    /**
     * 管理员MainFragment
     */

    private ManagerFragment managerFragment ;

    /**
     * 订阅Fragment
     */

    private SubScribeFragment subScribeFragment ;

    private FragmentManager fm ;

    private FragmentTransaction fragmentTransaction ;

    private  FragmentTransaction ft  ;

    /**
     * 我的Fragment
     */
    private MineFragment mineFragment ;


    public  LoginInfo loginInfo ;
     private  String mRegId = "";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            ft = fm.beginTransaction() ;
            hideFragments(ft);
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if(mainFragment == null)
                    {
                        mainFragment = MainFragment.newInstance("");
                        ft.add(R.id.content,mainFragment,MainFragment.class.getSimpleName());
                    }else
                    {
                        ft.show(mainFragment);
                    }
                    ft.commit();

                    return true;
                case R.id.navigation_dashboard:

                    AddTaskActivity.startActivity(MainActivity.this,null);
                    return true;
                case R.id.navigation_notifications:
                    if(mineFragment == null)
                    {
                        mineFragment = MineFragment.newInstance("");
                        ft.add(R.id.content,mineFragment,MineFragment.class.getSimpleName());
                    }else
                    {
                        ft.show(mineFragment);
                       // mineFragment.onResume();
                    }
                    ft.commit();
                    return true;
            }
            return false;
        }
    };
    /**
     * 退出应用
     */
    private static final  int EXIT_APP = 1025 ;

    private static final int REQUEST_INSTALL_PACKAGES = 1026;

    private static class MyHandler extends Handler
    {
        private final WeakReference<MainActivity> mActivity ;
        public MyHandler(MainActivity context)
        {
            mActivity = new WeakReference<MainActivity>(context) ;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity mainActivity = mActivity.get();
            if(mainActivity != null)
            {

            }

        }
    }

    private MyHandler myHandler ;

    private IntentFilter filter;
    private GestureReceiver gestureReceiver ;

    public static List<String> logList = new CopyOnWriteArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        MyApplication.setMainActivity(this);
        initData();
        receiverGesture();
        if(RomUtils.isHuawei())
        {
            getToken();
        }
       //

        myHandler = new MyHandler(this);
        fm = getSupportFragmentManager();
        mainFragment = MainFragment.newInstance("");
        gcMainFragment = GcMainFragment.newInstance("","");
        gcPlaceRoomFragment = GcPlaceRoomFragment.newInstance("","");
        fragmentTransaction = fm.beginTransaction();
        if(savedInstanceState == null)
        {
            //if(loginInfo.getData().getRoleid() == 0)
           // {
                fragmentTransaction.add(R.id.content, gcPlaceRoomFragment, GcMainFragment.class.getSimpleName()).commit();
           // }else {
           //     navigation.setVisibility(View.GONE);
           //     managerFragment = ManagerFragment.newInstance("");
                //fragmentTransaction.add(R.id.content, managerFragment, ManagerFragment.class.getSimpleName()).commit();
          //  }

        }else
        {
           // if(loginInfo.getData().getRoleid() == 0) {
                mainFragment = (MainFragment) fm.findFragmentByTag(MainFragment.class.getSimpleName());
                subScribeFragment = (SubScribeFragment) fm.findFragmentByTag(SubScribeFragment.class.getSimpleName());
                mineFragment = (MineFragment) fm.findFragmentByTag(MineFragment.class.getSimpleName());
          /*  }else{
                navigation.setVisibility(View.GONE);
                managerFragment = (ManagerFragment) fm.findFragmentByTag(ManagerFragment.class.getSimpleName());
            }*/
        }
        setGesture();
        goGesture();
        mRegId = DemoMessageReceiver.mRegId;
        mRegId = DemoMessageReceiver.mRegId;
        OkHttpManager.getInstance().putRegID(mRegId, RomUtils.getRomInfo().getName(), new NetCallBack() {
            @Override
            public void success(String response) {

            }

            @Override
            public void failed(String msg) {

            }
        });

       // updateApp();
    }

    @Override
    public boolean isSupportSwipeBack() {
        return false ;
    }

    private void initView()
    {
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setItemIconTintList(null);
        add_task = findViewById(R.id.add_task);
    }
    private void receiverGesture()
    {
        filter = new IntentFilter();
        filter.addAction("GESTURE_BROADCAST");
        gestureReceiver = new GestureReceiver();//创建广播接受者对象
        LocalBroadcastManager.getInstance(this).registerReceiver(gestureReceiver, filter);//注册
    }

    private void initData()
    {
        Intent intent = getIntent();
        if(intent != null)
        {
            loginInfo = (LoginInfo) intent.getSerializableExtra("loginInfo");
           // setLoginInfo(loginInfo);
            CacheDiskUtils.getInstance().put(Constant.LOGIN_CACHE_KEY,loginInfo);
            if(loginInfo == null)
            {
                return;
            }
            if("1".equals(loginInfo.getData().getIsadmin()))
            {

            }else {
                BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);

       //这里就是获取所添加的每一个Tab(或者叫menu)，
            View tab = menuView.getChildAt(1);
            BottomNavigationItemView itemView = (BottomNavigationItemView) tab;
            itemView.setVisibility(View.INVISIBLE);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
               // menuView.removeView(itemView);
            add_task.setVisibility(View.GONE);
            }
        }


    }

    /**
     * 隐藏fragment
     * @param fragmentTransaction
     */
    private void hideFragments(FragmentTransaction fragmentTransaction)
    {
        if(mainFragment != null)
        {
            fragmentTransaction.hide(mainFragment);
        }
        if(subScribeFragment != null)
        {
            fragmentTransaction.hide(subScribeFragment);
        }
        if(mineFragment != null)
        {
            fragmentTransaction.hide(mineFragment);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshLogInfo();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(gestureReceiver);//注册
        MyApplication.setMainActivity(null);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //moveTaskToBack(true);
        if(myHandler.hasMessages(EXIT_APP))
        {
            finish();
            //LocalBroadcastManager.getInstance(this).registerReceiver(gestureReceiver, filter);//注册
            //System.exit(0);
        }else {
            ToastUtils.showShort("再按一次退出程序");
            myHandler.sendEmptyMessageDelayed(EXIT_APP, 2000);
        }
    }

    /**
     * 首次进来也要设置手势密码
     */
    private void setGesture()
    {
        //boolean openGesture = SPUtils.getInstance().getBoolean("isOpenHandLock", false);
       /* if(!openGesture)
        {
            Intent intent = new Intent(MainActivity.this, GestureActivity.class);
            intent.putExtra("openHandLock",true);
            startActivity(intent);
        }*/
    }
    /**
     * 手势密码
     */
    class GestureReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG,"GestureReceiver");
            goGesture();
        }
    }

    public void refreshLogInfo() {
        String AllLog = "";
        for (String log : logList) {
            AllLog = AllLog + log + "\n\n";
        }
        //mLogView.setText(AllLog);
        Log.e(TAG,AllLog);
    }

    /**
     * get token
     */
    private void getToken() {
        Log.i(TAG, "get token: begin");

        // get token
        new Thread() {
            @Override
            public void run() {
                try {

                    // read from agconnect-services.json
                    String appId = AGConnectServicesConfig.fromContext(MainActivity.this).getString("client/app_id");
                    pushtoken = HmsInstanceId.getInstance(MainActivity.this).getToken(appId, "HCM");
                    Log.i(TAG, "appId:" + appId);
                    if (SdkVersionUtils.checkedAndroid_Q())
                    {
                        OkHttpManager.getInstance().putRegID(pushtoken, RomUtils.getRomInfo().getName(), new NetCallBack() {
                            @Override
                            public void success(String response) {

                            }

                            @Override
                            public void failed(String msg) {

                            }
                        });
                    }
                    if(!TextUtils.isEmpty(pushtoken)) {
                        Log.i(TAG, "get token:" + pushtoken);
                    }
                } catch (Exception e) {
                    Log.i(TAG,"getToken failed, " + e);

                }
            }
        }.start();
    }

    private void updateApp()
    {
       /* String apkPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File targetFile = new File(apkPath,"update.apk");
        AppUtils.installApk(this,targetFile.getPath());*/

        OkHttpManager.getInstance().updateApp(new NetCallBack() {
            @Override
            public void success(String response) {
                Log.e(TAG, "rsp==" + response);
                JSONObject jsonObject = null;
                String rsp = "";
                try {
                    jsonObject = new JSONObject(response);
                    rsp = jsonObject.getString("data");
                    jsonObject = new JSONObject(rsp);
                    DownloadBean downLoadBean = DownloadBean.parse(jsonObject);
                    if (downLoadBean == null) {
                        ToastUtils.showShort( "接口返回数据异常");
                        return;
                    }
                    // 检测是否需要弹窗
                    long versionCode = Long.parseLong(downLoadBean.versionCode);
                    if (versionCode > AppUtils.getVersionCode(MainActivity.this)) {
                        UpdateVersionShowDialog.show(MainActivity.this, downLoadBean);
                        return;
                    }
                }catch (JSONException e)
                {
                   e.printStackTrace();
                    ToastUtils.showShort(e.getMessage());
                }
            }

            @Override
            public void failed(String msg) {

            }
        });
    }

}
