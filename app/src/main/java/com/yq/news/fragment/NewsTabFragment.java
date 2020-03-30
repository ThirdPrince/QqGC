package com.yq.news.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yq.news.R;
import com.yq.news.activity.ArticleDetailActivity;
import com.yq.news.activity.TaskInfoActivity;
import com.yq.news.adapter.ImagePageAdapter;
import com.yq.news.adapter.MainInfoPageAdapter;
import com.yq.news.adapter.MainPageAdapter;
import com.yq.news.adapter.TaskListAdapter;
import com.yq.news.adapter.TextPageAdapter;
import com.yq.news.model.ArticleInfo;
import com.yq.news.model.LoginInfo;
import com.yq.news.model.TaskBean;
import com.yq.news.net.NetCallBack;
import com.yq.news.net.OkHttpManager;
import com.yq.news.view.Watermark;
import com.yq.util.Constant;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.yq.util.Constant.REFRESH_TASK_BROADCAST;
import static com.yq.util.Constant.SUBSCRIBLE_BROADCAST;
import static com.yq.util.Constant.TEXT_FONT_SIZE_BROADCAST;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsTabFragment extends BaseFragment {

    private static final String TAG = "NewsTabFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TITLE = "title";
    private static final String ID = "ID";

    // TODO: Rename and change types of parameters
    private String title;
    private String searchType;

   /* private WxArticleAdapter wxArticleAdapter;

    private List<WxArticleBean> wxArticleBeanList;*/

    private boolean isViewCreate = false;

    private boolean isDataInited = false;

    /**
     * 数据源
     */

    List<TaskBean> list ;

    private int pageNo = 1 ;

    private int pageSize = 15 ;


    /**
     * 包含多个Item
     */
    private TaskListAdapter taskListAdapter ;


    private EmptyWrapper emptyWrapper ;

    private IntentFilter filter;
    private RefreshReceiver refreshReceiver ;

    public NewsTabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WxArticleTabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsTabFragment newInstance(String title, String id) {
        NewsTabFragment fragment = new NewsTabFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ID);
            searchType = getArguments().getString(ID);
            //Log.e(TAG,"articleId =="+articleId);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginInfo loginInfo = (LoginInfo) CacheDiskUtils.getInstance().getSerializable(Constant.LOGIN_CACHE_KEY);
        if (loginInfo != null && loginInfo.getData() != null) {
            Watermark.getInstance().show(view, loginInfo.getData().getRealname() + " " + loginInfo.getData().getTelphone());
        }
        initRcy(view);
        //ViewCompat.setNestedScrollingEnabled(recyclerView, true);
        isViewCreate = true;
        list = new ArrayList<>();
        taskListAdapter = new TaskListAdapter(getActivity(),R.layout.task_list_item,list);
        recyclerView.setAdapter(taskListAdapter);
        receiverSubscrible();


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
        refreshLayout.setEnableAutoLoadMore(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                String uIdSpu = SPUtils.getInstance().getString("uid");
                String states = "3";
                pageNo = 1;
                OkHttpManager.getInstance().getTaskList(pageNo+"", pageSize+"", searchType, "", states, new NetCallBack() {
                    @Override
                    public void success(String response) {
                        JSONObject jsonObject = null;
                        String rsp = "";
                        try {
                            jsonObject = new JSONObject(response);
                            rsp = jsonObject.getString("data");
                            JSONArray jsonArray = new JSONObject(rsp).getJSONArray("list");
                            List<TaskBean> articleInfos = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<TaskBean>>() {
                            }.getType());
                            list.clear();
                            list.addAll(articleInfos);
                            if(articleInfos.size()<15)
                            {
                                refreshLayout.setEnableLoadMore(false);
                            }else
                            {
                                refreshLayout.setEnableLoadMore(true);
                            }

                            if(list.size()>0)
                            {
                                empty_lay.setVisibility(View.GONE);
                            }else
                            {
                                empty_lay.setVisibility(View.VISIBLE);
                            }

                            taskListAdapter.notifyDataSetChanged();
                           // mainPageAdapter.notifyDataSetChanged();
                            taskListAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                                    TaskInfoActivity.startActivity(getActivity(),list.get(position).getId()+"");
                                }

                                @Override
                                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                                    return false;
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
                OkHttpManager.getInstance().getTaskList(pageNo+"", pageSize+"", searchType, "", states, new NetCallBack() {
                    @Override
                    public void success(String response) {
                        JSONObject jsonObject = null;
                        String rsp = "";
                        try {
                            jsonObject = new JSONObject(response);
                            rsp = jsonObject.getString("data");
                            JSONArray jsonArray = new JSONObject(rsp).getJSONArray("list");
                            List<TaskBean> articleInfos = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<TaskBean>>() {
                            }.getType());
                            list.addAll(articleInfos);
                            if(articleInfos.size()<15)
                            {
                                refreshLayout.setEnableLoadMore(false);
                            }
                            refreshLayout.finishLoadMore();
                            taskListAdapter.notifyDataSetChanged();
                            // mainPageAdapter.notifyDataSetChanged();
                            taskListAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                                    TaskInfoActivity.startActivity(getActivity(),list.get(position).getId()+"");
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
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(refreshReceiver);//注册
    }
    private void receiverSubscrible()
    {
        filter = new IntentFilter();
        filter.addAction(REFRESH_TASK_BROADCAST);
        refreshReceiver = new RefreshReceiver();//创建广播接受者对象
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(refreshReceiver, filter);//注册
    }

    /**
     * 字体更新
     */
    class TextSizeReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.e(TAG,"isHiden="+isHidden());

        }
    }

    /**
     * 刷新任务的地方
     */
    class RefreshReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(isViewCreate && isDataInited) {
                onLoadData();
            }
        }
    }
}
