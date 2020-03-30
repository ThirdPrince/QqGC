package com.yq.news.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yq.news.R;
import com.yq.news.activity.ArticleDetailActivity;
import com.yq.news.activity.TaskFeedbackActivity;
import com.yq.news.activity.TaskInfoActivity;
import com.yq.news.adapter.FeedbackAdapter;
import com.yq.news.adapter.MainInfoPageAdapter;
import com.yq.news.adapter.MainPageAdapter;
import com.yq.news.itf.OnEditClick;
import com.yq.news.itf.OnImageClick;
import com.yq.news.model.ArticleInfo;
import com.yq.news.model.FeedBackAccount;
import com.yq.news.model.FeedBackInfo;
import com.yq.news.model.FeedBackUser;
import com.yq.news.model.LoginInfo;
import com.yq.news.model.MsgInfo;
import com.yq.news.model.TaskBean;
import com.yq.news.net.NetCallBack;
import com.yq.news.net.OkHttpManager;
import com.yq.news.view.Watermark;
import com.yq.util.Constant;
import com.yq.util.ManagerUtlis;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import q.rorbin.badgeview.QBadgeView;

import static com.yq.util.Constant.TEXT_FONT_SIZE_BROADCAST;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedBackTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedBackTabFragment extends BaseFragment {

    private static final String TAG = "NewsTabFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TITLE = "title";
    private static final String ID = "ID";

    // TODO: Rename and change types of parameters
    private String title;
    private String taskId;

    private TaskBean taskBean ;

   /* private WxArticleAdapter wxArticleAdapter;

    private List<WxArticleBean> wxArticleBeanList;*/

    private boolean isViewCreate = false;

    private boolean isDataInited = false;

    /**
     * 数据源
     */

    List<FeedBackInfo> list ;

    private int pageNo = 1 ;

    private int pageSize = 15 ;

    private MainPageAdapter mainPageAdapter ;

    /**
     * 包含多个Item
     */
    private MainInfoPageAdapter mainInfoPageAdapter ;


    private EmptyWrapper emptyWrapper ;

    private IntentFilter filter;
    private TextSizeReciver textSizeReciver ;

    private FeedbackAdapter feedbackAdapter ;

    public FeedBackTabFragment() {
        // Required empty public constructor
    }

    private final  int REQUEST_ACCOUNT = 1024;

    private final  int REQUEST_USER = 1025;

    private int themeId;

    private List<LocalMedia> selectList = new ArrayList<>();


    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_ACCOUNT:
                    if(list != null  && list.size()>0)
                    {
                        StringBuilder ids = new StringBuilder();
                        for(FeedBackInfo  feedBackInfo :list)
                        {
                            ids.append(feedBackInfo.getAccountid()+",");
                        }
                        OkHttpManager.getInstance().getRecordAccount(ids.toString(), new NetCallBack() {
                            @Override
                            public void success(String response) {

                                JSONObject jsonObject = null;

                                try {
                                    jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = new JSONObject(jsonObject.toString()).getJSONArray("data");
                                    List<FeedBackAccount> feedBackAccounts = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<FeedBackAccount>>() {
                                    }.getType());

                                  for(int i= 0;i<feedBackAccounts.size();i++)
                                  {
                                      FeedBackInfo feedBackInfo = list.get(i);
                                      feedBackInfo.setAccountname(feedBackAccounts.get(i).getNickname());
                                      feedBackInfo.setUserAccountId(feedBackAccounts.get(i).getAccountid());
                                      feedBackInfo.setAccountTypeName(feedBackAccounts.get(i).getAccountTypeName());
                                  }

                                    feedbackAdapter.notifyDataSetChanged();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void failed(String msg) {
                                ToastUtils.showLong(msg);

                            }
                        });
                    }

                    break;
                case REQUEST_USER:

                    if(list != null  && list.size()>0)
                    {
                        StringBuilder ids = new StringBuilder();
                        for(FeedBackInfo  feedBackInfo :list)
                        {
                            ids.append(feedBackInfo.getUserid()+",");
                        }
                        OkHttpManager.getInstance().getRecordUser(ids.toString(), new NetCallBack() {
                            @Override
                            public void success(String response) {

                                JSONObject jsonObject = null;

                                try {
                                    jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = new JSONObject(jsonObject.toString()).getJSONArray("data");
                                    List<FeedBackUser> feedBackUsers = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<FeedBackUser>>() {
                                    }.getType());

                                    for(int i= 0;i<feedBackUsers.size();i++)
                                    {
                                        FeedBackInfo feedBackInfo = list.get(i);
                                        if(feedBackUsers.get(i) != null) {
                                            feedBackInfo.setUsername(feedBackUsers.get(i).getAccount());
                                        }

                                    }

                                    feedbackAdapter.notifyDataSetChanged();

                                   // feedbackAdapter.notifyDataSetChanged();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void failed(String msg) {
                                ToastUtils.showLong(msg);

                            }
                        });
                    }
                    break;

            }

        }
    };

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WxArticleTabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedBackTabFragment newInstance(String title, String id, TaskBean taskBean ) {
        FeedBackTabFragment fragment = new FeedBackTabFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(ID, id);
        args.putSerializable("task",taskBean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ID);
            taskId = getArguments().getString(ID);
            taskBean = (TaskBean)getArguments().getSerializable("task");
            //Log.e(TAG,"taskId =="+taskId);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        themeId = R.style.picture_default_style;
        return inflater.inflate(R.layout.fragment_feed_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRcy(view);
        isViewCreate = true;
        list = new ArrayList<>();
        feedbackAdapter = new FeedbackAdapter(getActivity(),R.layout.feed_back_list_item,list,taskBean);
        recyclerView.setAdapter(feedbackAdapter);
       // receiverSubscrible();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // Log.e(TAG, "onActivityCreated ::isViewCreate ==" + isViewCreate + "::getUserVisibleHint()==" + getUserVisibleHint());
        if (isViewCreate && getUserVisibleHint() && !isDataInited) {
            onLoadData();
        }
    }

    /**
     * 加载数据
     */
    private void onLoadData() {

        refreshLayout.autoRefresh();
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {

                pageNo = 1;
                OkHttpManager.getInstance().getRecordList(taskId, new NetCallBack() {
                    @Override
                    public void success(String response) {
                        JSONObject jsonObject = null;
                        String rsp = "";
                        try {
                            jsonObject = new JSONObject(response);
                            rsp = jsonObject.getString("data");
                            JSONArray jsonArray = new JSONObject(rsp).getJSONArray("list");
                            List<FeedBackInfo> articleInfos = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<FeedBackInfo>>() {
                            }.getType());
                            list.clear();
                            list.addAll(articleInfos);
                            if(!ManagerUtlis.isManager())
                            {
                                LoginInfo loginInfo = (LoginInfo) CacheDiskUtils.getInstance().getSerializable(Constant.LOGIN_CACHE_KEY);
                                String feedName =  null;
                                if(loginInfo != null)
                                {
                                    feedName = loginInfo.getData().getAccount();
                                }
                                for(FeedBackInfo feedBackInfo :articleInfos)
                                {
                                    feedBackInfo.setUsername(feedName);
                                }
                            }else
                            {
                                handler.sendEmptyMessage(REQUEST_USER);
                            }

                            handler.sendEmptyMessage(REQUEST_ACCOUNT);

                            if(articleInfos.size()<15)
                            {
                                refreshLayout.setEnableLoadMore(false);
                            }else
                            {
                                refreshLayout.setEnableLoadMore(true);
                            }


                            if (list.size() > 0) {
                                empty_lay.setVisibility(View.GONE);
                            } else {
                                empty_lay.setVisibility(View.VISIBLE);
                            }
                           /* if(ManagerUtlis.isManager()) {

                                if (list.size() > 0) {
                                    empty_lay.setVisibility(View.GONE);
                                } else {
                                    empty_lay.setVisibility(View.VISIBLE);
                                }
                            }else
                            {

                            }*/


                            feedbackAdapter.notifyDataSetChanged();
                            feedbackAdapter.setOnEditClick(new OnEditClick() {
                                @Override
                                public void onClick(Object object, View view) {
                                    FeedBackInfo feedBackInfo = (FeedBackInfo)object;
                                    TaskFeedbackActivity.startActivity(getActivity(),REQUEST_CODE,feedBackInfo.getTaskid()+"",taskBean.getType(),feedBackInfo);
                                }
                            });
                            feedbackAdapter.setOnImageClick(new OnImageClick() {
                                @Override
                                public void onClick(int pos, View view) {
                                    selectList.clear();
                                    for(FeedBackInfo feedBackInfo:list) {
                                        LocalMedia localMedia = new LocalMedia(feedBackInfo.getImgPath());
                                        selectList.add(localMedia);
                                    }

                                    PictureSelector.create(getActivity()).themeStyle(themeId).openExternalPreview(pos, selectList);
                                }
                            });

                            refreshLayout.finishRefresh();
                            isDataInited = true ;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            refreshLayout.finishRefresh();
                        }
                    }

                    @Override
                    public void failed(String msg) {
                        refreshLayout.finishRefresh();
                    }
                });
            }
        });

        isDataInited = true;

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                String states = "3";
                pageNo ++ ;
                OkHttpManager.getInstance().getTaskList(pageNo+"", pageSize+"", taskId, "", states, new NetCallBack() {
                    @Override
                    public void success(String response) {
                        JSONObject jsonObject = null;
                        String rsp = "";
                        try {
                            jsonObject = new JSONObject(response);
                            rsp = jsonObject.getString("data");
                            JSONArray jsonArray = new JSONObject(rsp).getJSONArray("list");
                            List<FeedBackInfo> articleInfos = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<FeedBackInfo>>() {
                            }.getType());
                            list.addAll(articleInfos);
                            if(articleInfos.size()<15)
                            {
                                refreshLayout.setEnableLoadMore(false);
                            }
                            refreshLayout.finishLoadMore();
                            mainInfoPageAdapter.notifyDataSetChanged();
                            // mainPageAdapter.notifyDataSetChanged();
                            mainInfoPageAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                                }

                                @Override
                                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                                    return false;
                                }
                            });
                            //refreshLayout.finishRefresh();
                           // isDataInited = true ;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failed(String msg) {

                    }
                });
            }
        });

    }


    public String getTitle() {
        return title;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e(TAG,"isVisibleToUser:"+isVisibleToUser);
        if (isVisibleToUser && isViewCreate && !isDataInited) {
            onLoadData();
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e(TAG,"hidden::"+hidden);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(textSizeReciver);//注册
    }
    private void receiverSubscrible()
    {
        filter = new IntentFilter();
        filter.addAction(TEXT_FONT_SIZE_BROADCAST);
        textSizeReciver = new TextSizeReciver();//创建广播接受者对象
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(textSizeReciver, filter);//注册
    }

    /**
     * 字体更新
     */
    class TextSizeReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.e(TAG,"isHiden="+isHidden());
            if (isVisible() && isDataInited && isViewCreate &&  mainInfoPageAdapter != null) {
                Log.e(TAG,"TextSizeReciver");
                mainInfoPageAdapter.refreshTextSize();
                mainInfoPageAdapter.notifyDataSetChanged();
            }
        }
    }

    public  void refreshData()
    {
        if(isViewCreate && isDataInited)
        {
            onLoadData();
        }
    }

}
