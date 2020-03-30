package com.yq.news.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yq.news.MainActivity;
import com.yq.news.R;
import com.yq.news.fragment.FeedBackTabFragment;
import com.yq.news.fragment.MainFragment;
import com.yq.news.fragment.NewsTabFragment;
import com.yq.news.fragment.NoFeedBackTabFragment;
import com.yq.news.model.LoginInfo;
import com.yq.news.model.TabInfo;
import com.yq.news.model.TaskBean;
import com.yq.news.model.TaskInfoUser;
import com.yq.news.net.NetCallBack;
import com.yq.news.net.OkHttpManager;
import com.yq.util.Constant;
import com.yq.util.ManagerUtlis;
import com.yq.util.RelativeDateFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 *   任务详情
 *
 */
public class TaskInfoActivity extends BaseActivity {



    /**
     * tabLayout
     */
    private TabLayout tabLayout ;

    /**
     * 反馈状态
     */

    private TextView feed_back_state_tv ;

    private SlidingTabLayout sliding_tab ;

   private TextView title,content,time_start_tv,time_end_tv,task_create_time,task_person;


    /**
     *  反馈状态
     */

    private ImageView feed_back_img_bg ;
    /**
     * 导控数量
     */
   private TextView control_num ;

    /**
     * 接收人员
     */

    private TextView received_tv;
    /**
     * taskId
     */
    private String id ;

    private String[] titles ;


    private List<String> tabIndicator ;


    /**
     *  数据源
     */
    private List<TabInfo.DataBean> listBean ;

    /**
     * viewPager
     */
    private ViewPager viewPager ;


    private FloatingActionButton fab_add ;

    private FeedBackTabFragment feedBackTabFragment ;

    private TabFragmentAdapter tabFragmentAdapter ;


    private TaskBean taskBean ;

    /**
     * 问文员反馈按钮
     */

    private final int REQUEST_CODE = 1024;

    // private ImageView search_btn ;

    public static  void startActivity(Activity activity,String id )
    {
        Intent intent = new Intent(activity,TaskInfoActivity.class);
        intent.putExtra("id",id);
        activity.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);
        initToolBar();
        initView();
        initData();
    }


    private void initView()
    {
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        feed_back_state_tv = findViewById(R.id.feed_back_state_tv);
        feed_back_img_bg  = findViewById(R.id.feed_back_img_bg);
        time_start_tv = findViewById(R.id.time_start_tv);
        time_end_tv = findViewById(R.id.time_end_tv);
        task_create_time = findViewById(R.id.task_create_time);
        control_num = findViewById(R.id.control_num);
        received_tv = findViewById(R.id.received_tv);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.content_vp);
        sliding_tab = findViewById(R.id.sliding_tab);
        fab_add = findViewById(R.id.fab_add);
        tabIndicator = new ArrayList<>();
        back_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

    }
    private void initData()
    {

        toolbar_title.setText("任务详情");
        tabIndicator.add("已反馈");
        if(ManagerUtlis.isManager())
        {
            tabIndicator.add("未反馈");
            fab_add.hide();
            toolbar_add.setVisibility(View.VISIBLE);
            toolbar_add.setText("删除");
            toolbar_edit.setText("编辑");
            toolbar_edit.setVisibility(View.VISIBLE);
            toolbar_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(taskBean == null)
                        return ;
                    AlertDialog.Builder builder = new AlertDialog.Builder(TaskInfoActivity.this);
                    builder.setMessage("确定删除该任务吗？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            OkHttpManager.getInstance().deleteTask(taskBean.getId()+"", new NetCallBack() {
                                @Override
                                public void success(String response) {
                                    ToastUtils.showLong("删除成功");
                                    finish();

                                }

                                @Override
                                public void failed(String msg) {
                                    ToastUtils.showLong(msg);
                                }
                            });
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    builder.show();

                }
            });
            toolbar_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddTaskActivity.startActivity(TaskInfoActivity.this,taskBean);
                }
            });

        }else
        {

        }

        List<String> titleTab = new ArrayList<>();
        titleTab.addAll(tabIndicator);
        titles = new String[titleTab.size()];
        for(int i = 0;i<titleTab.size();i++)
        {
            titles[i] = titleTab.get(i);
        }

        if(getIntent()!=null)
        {
            id = getIntent().getStringExtra("id");
            loadData();
        }
    }

    private void loadData()
    {
        if(TextUtils.isEmpty(id))
            return ;
        OkHttpManager.getInstance().getTaskDetail(id, new NetCallBack() {
            @Override
            public void success(String response) {
                JSONObject jsonObject = null;
                String rsp = "";
                try {
                    jsonObject = new JSONObject(response);
                    rsp = jsonObject.getString("data");
                    JSONArray jsonArray = new JSONObject(rsp).getJSONArray("userlist");
                    List<TaskInfoUser> taskInfoUsers = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<TaskInfoUser>>() {
                    }.getType());
                    StringBuilder stringBuilder = new StringBuilder();
                    if(taskInfoUsers.size()>0)
                    {
                        for(int i = 0;i<taskInfoUsers.size();i++)
                        {
                            if(i== taskInfoUsers.size()-1)
                            {
                                stringBuilder.append(taskInfoUsers.get(i).getRealname());

                            }else
                            {
                                stringBuilder.append(taskInfoUsers.get(i).getRealname()+"，");
                            }

                        }

                    }
                    received_tv.setText(stringBuilder.toString());
                    taskBean = new Gson().fromJson(rsp,TaskBean.class);
                    taskBean.setTaskInfoUsers(taskInfoUsers);
                    title.setText(taskBean.getName());
                    content.setText(taskBean.getDescription());
                    time_start_tv.setText(RelativeDateFormat.timeStamp2Date(taskBean.getStarttime()));
                    time_end_tv.setText(RelativeDateFormat.timeStamp2Date(taskBean.getEndtime()));
                    task_create_time.setText(RelativeDateFormat.timeStamp2Date(taskBean.getCreatetime()));
                    control_num.setText(taskBean.getPilotcount()+"");

                    if(ManagerUtlis.isManager())
                    {
                        if(taskBean.getFinished() ==1) {
                            //toolbar_edit.setVisibility(View.GONE);
                            if (taskBean.getRecordcount() < taskBean.getPilotcount() * taskInfoUsers.size()) {
                                //feed_back_state_tv.setText("未达标");
                                feed_back_img_bg.setImageDrawable(getResources().getDrawable(R.drawable.wdb));
                                setFeedBackState(R.drawable.wdb2);
                            }else
                            {
                                //feed_back_state_tv.setText("已达标");
                                feed_back_img_bg.setImageDrawable(getResources().getDrawable(R.drawable.ydb));
                                setFeedBackState(R.drawable.ydb2);
                            }
                        }else
                        {
                            if (taskBean.getRecordcount() < taskBean.getPilotcount() * taskInfoUsers.size()) {
                                //feed_back_state_tv.setText("反馈中");
                                feed_back_img_bg.setImageDrawable(getResources().getDrawable(R.drawable.fkz));
                                setFeedBackState(R.drawable.fkz2);
                            }else
                            {
                                //feed_back_state_tv.setText("已达标");
                                feed_back_img_bg.setImageDrawable(getResources().getDrawable(R.drawable.ydb));
                                setFeedBackState(R.drawable.ydb2);
                            }
                        }
                    }else
                    {

                        if(  taskBean.getFinished() ==1)
                        {
                            fab_add.hide();
                            if(taskBean.getRecordcount()<taskBean.getPilotcount())
                            {
                                //feed_back_state_tv.setText("未达标");
                                feed_back_img_bg.setImageDrawable(getResources().getDrawable(R.drawable.wdb));
                                setFeedBackState(R.drawable.wdb2);
                            }else
                            {
                                //feed_back_state_tv.setText("已达标");
                                feed_back_img_bg.setImageDrawable(getResources().getDrawable(R.drawable.ydb));
                                setFeedBackState(R.drawable.ydb2);
                            }
                        }else{
                            if(taskBean.getRecordcount()<taskBean.getPilotcount())
                            {
                                //feed_back_state_tv.setText("待反馈");
                                feed_back_img_bg.setImageDrawable(getResources().getDrawable(R.drawable.dfk));
                                setFeedBackState(R.drawable.dfk2);

                            }else{
                               // feed_back_state_tv.setText("已反馈");
                                feed_back_img_bg.setImageDrawable(getResources().getDrawable(R.drawable.yfk));
                                setFeedBackState(R.drawable.yfk2);
                            }
                        }

                    }

                    if(tabFragmentAdapter  == null)
                    {
                        tabFragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager(),titles);
                        viewPager.setAdapter(tabFragmentAdapter);
                        //  tabFragmentAdapter.setTitles(titles);
                        viewPager.setOffscreenPageLimit(titles.length);
                        // setTabWidth(tabLayout,10);
                        sliding_tab.setViewPager(viewPager);
                        //tabLayout.setupWithViewPager(viewPager);
                        //setTabWidth(tabLayout,0);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(String msg) {

            }
        });
    }

    private void setFeedBackState(int drawable)
    {
        Drawable leftDrawable = null;
        leftDrawable = TaskInfoActivity.this.getResources().getDrawable(drawable);
        leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(),
                leftDrawable.getMinimumHeight());
        feed_back_state_tv.setCompoundDrawables(leftDrawable, null,null , null);
    }

    public void onClickFab(View v){
        if(taskBean != null) {
            TaskFeedbackActivity.startActivity(TaskInfoActivity.this, REQUEST_CODE, id, taskBean.getType(), null);
        }
    }
    class TabFragmentAdapter extends FragmentStatePagerAdapter {

        private String[] titles;

        public TabFragmentAdapter(FragmentManager fm, String[] titles) {
            super(fm);
            this.titles = titles ;
        }

        @Override
        public Fragment getItem(int i) {
            if(i==0)
            {
                feedBackTabFragment = FeedBackTabFragment.newInstance("",taskBean.getId()+"",taskBean);
                return feedBackTabFragment;
            }else
            {
                return NoFeedBackTabFragment.newInstance("",taskBean.getId()+"");
            }

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
        public void setTitles(String[] titles) {
            this.titles = titles;
            sliding_tab.notifyDataSetChanged();
            //sliding_tab.scrollToCurrentTab();
            notifyDataSetChanged();
            viewPager.setCurrentItem(0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_CODE:
                if(resultCode == Activity.RESULT_OK && feedBackTabFragment != null)
                {
                    feedBackTabFragment.refreshData();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //moveTaskToBack(true);
        back();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }

    private void back()
    {
        if(ActivityUtils.isActivityExistsInStack(MainActivity.class))
        {
            finish();
        }else {
            ActivityUtils.finishAllActivities();
            Intent intent = new Intent(); //创建Intent对象
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName componentName1 = new ComponentName("com.yq.dk","com.yq.news.activity.SplashActivity");
            intent.setComponent(componentName1);//调用Intent的setComponent()方法实现传递
            startActivity(intent);//显示启动Activity
            finish();
        }
    }
}
