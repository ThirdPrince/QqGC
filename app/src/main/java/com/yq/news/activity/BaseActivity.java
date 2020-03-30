package com.yq.news.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.leaf.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.yq.news.R;
import com.yq.news.net.NetCallBack;
import com.yq.news.net.OkHttpManager;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;


/**
 *   基类：activity
 * @author dhl
 */
public class BaseActivity extends AppCompatActivity implements BGASwipeBackHelper.Delegate {

    private static final String TAG = "BaseActivity";

    protected BGASwipeBackHelper mSwipeBackHelper;

    protected Toolbar mToolbar;

    private Toolbar tool_bar ;

    protected TextView toolbar_title;

    protected RelativeLayout back_lay;

    protected ImageView iv_back ;


    /**
     * 最右侧的 tv
     */
    protected TextView toolbar_add ;

    /**
     * 右边第二个 tv
     */
    protected TextView toolbar_edit ;


    protected LinearLayout empty_lay ;

    public static long lastGestureTime = 0;




    private Handler handler = new Handler(Looper.getMainLooper());
    /**
     * 登陆Dialog
     */
    private ProgressDialog loadingDialog;

    public boolean wasBackground = false;    //声明一个布尔变量,记录当前的活动背景

    public static boolean isFirstCreate = true ;

    /**
     * smartRefresh
     */
    protected RefreshLayout refreshLayout ;
    protected ClassicsHeader mClassicsHeader;
    protected Drawable mDrawableProgress;
    /**
     * rcy
     */


    protected RecyclerView recyclerView ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        /**
         * Android 6.0 以上设置状态栏颜色
         */
        //initSwipeBackFinish();
       /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 设置状态栏底色白色
           View decorView = getWindow().getDecorView();

        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
           *//* getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);*//*
           getWindow().setStatusBarColor(Color.TRANSPARENT);
            // 设置状态栏字体黑色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }*/
        initSwipeBackFinish();
        super.onCreate(savedInstanceState);
      /*  if(isFirstCreate) {
            goGesture();
            isFirstCreate = false ;
        }*/

    }


    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private void initSwipeBackFinish() {
        mSwipeBackHelper = new BGASwipeBackHelper(this, this);

        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。

        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true);
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true);
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(true);
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true);
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true);
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f);
        // 设置底部导航条是否悬浮在内容上，默认值为 false
        mSwipeBackHelper.setIsNavigationBarOverlap(false);
    }
    protected void initToolBar() {
        tool_bar = findViewById(R.id.tool_bar);
        StatusBarUtil.setGradientColor(this, tool_bar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        back_lay = (RelativeLayout) findViewById(R.id.back_lay);
        iv_back = findViewById(R.id.iv_back);
        toolbar_add = findViewById(R.id.toolbar_add);
        toolbar_edit = findViewById(R.id.toolbar_edit);

        if(back_lay != null)
        {
            back_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }


    protected void initRcy()
    {
        recyclerView = (RecyclerView) findViewById(R.id.rcy_view);
        refreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout);
        mClassicsHeader = (ClassicsHeader)refreshLayout.getRefreshHeader();
        //mClassicsHeader.setLastUpdateTime(new Date(System.currentTimeMillis()-deta));
        mClassicsHeader.setTimeFormat(new SimpleDateFormat("更新于 MM-dd HH:mm", Locale.CHINA));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        empty_lay  = findViewById(R.id.empty_lay);
    }

    @Override
    protected void onResume() {
        super.onResume();
       /* if (wasBackground) {//
            Log.e(TAG, "从后台回到前台");
            //goGesture();
        }
        wasBackground = false;*/

        validToken();

        //isUIProcess();
    }

    @Override
    protected void onPause() {
        super.onPause();

       /* if (isApplicationBroughtToBackground())
            wasBackground = true;*/

        //isUIProcess();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void validToken() {
        OkHttpManager.getInstance().validToken(new NetCallBack() {
            @Override
            public void success(String response) {

                //Log.e(TAG, "Token:" + response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    boolean data = jsonObject.getBoolean("data");
                    if (!data) {
                        getToken();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failed(String msg) {
                getToken();

            }

        });
    }

    private void getToken() {
        OkHttpManager.getInstance().getToken(new NetCallBack() {
            @Override
            public void success(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String token = jsonObject.getString("data");
                    if (!TextUtils.isEmpty(token)) {
                        SPUtils.getInstance().put("token", token);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(String msg) {
                ToastUtils.showShort(msg);
            }
        });
    }

    protected void showDialog(String tip) {
        loadingDialog = ProgressDialog.show(this, null, tip);
        loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingDialog.setCancelable(true);
        loadingDialog.show();// 设置圆形旋转进度条
    }

    protected void dismissDialog() {
        if(loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

   /* private boolean isApplicationBroughtToBackground() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        Log.e(TAG, "tasks::"+tasks.get(0).id);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            Log.e(TAG, "topActivity.getPackageName()::"+topActivity.getPackageName());
            if (!topActivity.getPackageName().equals(getPackageName())) {
                return true;
            }
        }
        return false;
    }*/


    /**
     * 跳到手势密码的逻辑
     */
    protected void goGesture()
    {
        long nowTime = Calendar.getInstance().getTimeInMillis();
        boolean openGesture = SPUtils.getInstance().getBoolean("isOpenHandLock", false);
        if (nowTime - lastGestureTime > 1000) {
            lastGestureTime = nowTime;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    boolean parm = !openGesture;
                    //GestureActivity.startActivity(BaseActivity.this,parm);
                   // overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    //Intent intent = new Intent(BaseActivity.this, GestureActivity.class);
                   // startActivity(intent);
                   // overridePendingTransition(0, 0);
                }
            }, 50);

        }
    }


    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }

    @Override
    public void onSwipeBackLayoutSlide(float slideOffset) {

    }

    @Override
    public void onSwipeBackLayoutCancel() {

    }

    @Override
    public void onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward();
    }
}
