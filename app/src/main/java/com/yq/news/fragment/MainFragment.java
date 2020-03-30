package com.yq.news.fragment;


import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allenliu.badgeview.BadgeFactory;
import com.allenliu.badgeview.BadgeView;
import com.blankj.utilcode.util.CacheDiskUtils;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yq.news.R;
import com.yq.news.activity.MsgListActivity;
import com.yq.news.activity.SearchActivity;
import com.yq.news.model.LoginInfo;
import com.yq.news.model.MsgInfo;
import com.yq.news.model.TabInfo;
import com.yq.news.model.TaskBean;
import com.yq.news.net.NetCallBack;
import com.yq.news.net.OkHttpManager;
import com.yq.util.Constant;
import com.yq.util.ManagerUtlis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import q.rorbin.badgeview.QBadgeView;

import static com.yq.util.Constant.SUBSCRIBLE_BROADCAST;

/**
 * @author 首页Fragment 普通员工
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends BaseFragment {

    private static final String TAG = "MainFragment";

    private static final String ARG_PARAM1 = "param1";


    private   int badgeNum ;

    /**
     * tabLayout
     */
    private TabLayout tabLayout ;

    private TextView search_tv ;

    private TextView msg_tv ;

    private ObjectAnimator objectAnimator;

    private SlidingTabLayout sliding_tab ;

   private String [] titles ;


    private List<String> tabIndicator ;


    private QBadgeView qBadgeView ;
    //private ImageView search_btn;

    /**
     *  数据源
     */
    private List<TabInfo.DataBean> listBean ;

    /**
     * viewPager
     */
    private ViewPager viewPager ;

    private List<NewsTabFragment> newsTabFragments ;

    private TabFragmentAdapter  tabFragmentAdapter ;

    private IntentFilter filter;
    private SubscribleReciver subscribleReciver ;

    public MainFragment() {
        // Required empty public constructor
    }
    public static MainFragment newInstance(String param1) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        receiverSubscrible();
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar(view);
        initView(view);
        loadData();
    }

    private void initView(View view)
    {
        // toolbar_title = view.findViewById(R.id.toolbar_title);
        search_tv = view.findViewById(R.id.search_tv);
        msg_tv = view.findViewById(R.id.msg_tv);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.content_vp);
        sliding_tab = view.findViewById(R.id.sliding_tab);
        tabIndicator = new ArrayList<>();
        newsTabFragments = new ArrayList<>();

        if(ManagerUtlis.isManager())
        {
            msg_tv.setVisibility(View.GONE);
        }
        search_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.startActivity(getActivity());
         }
        });
        msg_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MsgListActivity.startActivity(getActivity(),badgeNum);
            }
        });


    }
    private void loadData()
    {
        titles = new String[2];
        titles[0] = "进行中";
        titles[1] = "已结束";

        if(tabFragmentAdapter  == null)
        {
            tabFragmentAdapter = new TabFragmentAdapter(getChildFragmentManager(),titles);
            viewPager.setAdapter(tabFragmentAdapter);
            newsTabFragments.add(NewsTabFragment.newInstance("",1+""));
            newsTabFragments.add(NewsTabFragment.newInstance("",2+""));
            viewPager.setOffscreenPageLimit(newsTabFragments.size());
            // setTabWidth(tabLayout,10);
            sliding_tab.setViewPager(viewPager);
            //tabLayout.setupWithViewPager(viewPager);
            //setTabWidth(tabLayout,0);
        }


    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e(TAG,"onHiddenChanged:"+hidden);
        if(!hidden )
        {
            //loadData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!ManagerUtlis.isManager()) {
            getMessageList();
        }
    }

    private void getMessageList()
    {
        OkHttpManager.getInstance().getMessageList("0",new NetCallBack() {
            @Override
            public void success(String response) {
                JSONObject jsonObject = null;
                String rsp = "";
                try {
                    jsonObject = new JSONObject(response);
                    rsp = jsonObject.getString("data");
                    JSONArray jsonArray = new JSONObject(jsonObject.toString()).getJSONArray("data");
                    List<MsgInfo> msgInfos = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<MsgInfo>>() {
                    }.getType());
                    badgeNum = msgInfos.size();
                   // if(badgeNum != 0) {
                    if(qBadgeView == null) {
                        qBadgeView = new QBadgeView(getActivity());
                        qBadgeView.bindTarget(msg_tv).setBadgeNumber(badgeNum);
                    }else
                    {
                        qBadgeView.setBadgeNumber(badgeNum);
                    }
                        if(badgeNum != 0) {
                            qBadgeView.setBadgeTextSize(9.0f,true);
                           // qBadgeView.setBadgePadding(2.0f, true);
                            qBadgeView.setGravityOffset(3, -3, true);
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

    class TabFragmentAdapter extends FragmentStatePagerAdapter {

        private String[] titles;

        public TabFragmentAdapter(FragmentManager fm,String[] titles) {
            super(fm);
            this.titles = titles ;
        }

        @Override
        public Fragment getItem(int i) {
            return
                    newsTabFragments.get(i);
                    //newsTabFragments.get(i);
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

    private void receiverSubscrible()
    {
        filter = new IntentFilter();
        filter.addAction(SUBSCRIBLE_BROADCAST);
        subscribleReciver = new SubscribleReciver();//创建广播接受者对象
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(subscribleReciver, filter);//注册
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(subscribleReciver);//注册
    }

    /**
     * 订阅号更新
     */
    class SubscribleReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadData();
        }
    }



}
