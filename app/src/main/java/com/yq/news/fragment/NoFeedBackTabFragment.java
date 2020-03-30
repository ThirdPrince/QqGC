package com.yq.news.fragment;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yq.news.R;
import com.yq.news.activity.TaskFeedbackActivity;
import com.yq.news.adapter.FeedbackAdapter;
import com.yq.news.adapter.MainInfoPageAdapter;
import com.yq.news.adapter.MainPageAdapter;
import com.yq.news.adapter.NoFeedbackAdapter;
import com.yq.news.model.FeedBackAccount;
import com.yq.news.model.FeedBackInfo;
import com.yq.news.model.NoFeedBackInfo;
import com.yq.news.net.NetCallBack;
import com.yq.news.net.OkHttpManager;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.yq.util.Constant.TEXT_FONT_SIZE_BROADCAST;


/**
 未反馈
 */
public class NoFeedBackTabFragment extends BaseFragment {

    private static final String TAG = "NewsTabFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TITLE = "title";
    private static final String ID = "ID";

    // TODO: Rename and change types of parameters
    private String title;
    private String taskId;

   /* private WxArticleAdapter wxArticleAdapter;

    private List<WxArticleBean> wxArticleBeanList;*/

    private boolean isViewCreate = false;

    private boolean isDataInited = false;

    private String num ;

    /**
     * 数据源
     */

    List<NoFeedBackInfo> list;

    private int pageNo = 1;

    private int pageSize = 15;

    private MainPageAdapter mainPageAdapter;

    /**
     * 包含多个Item
     */
    private MainInfoPageAdapter mainInfoPageAdapter;


    private EmptyWrapper emptyWrapper;

    private IntentFilter filter;
    private TextSizeReciver textSizeReciver;

    private NoFeedbackAdapter noFeedbackAdapter;

    private static final int RC_CALL_PERM = 1000;


    public NoFeedBackTabFragment() {
        // Required empty public constructor
    }

    private final int REQUEST_ACCOUNT = 1024;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WxArticleTabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoFeedBackTabFragment newInstance(String title, String id) {
        NoFeedBackTabFragment fragment = new NoFeedBackTabFragment();
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
            taskId = getArguments().getString(ID);
            //Log.e(TAG,"taskId =="+taskId);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRcy(view);
        isViewCreate = true;
        list = new ArrayList<>();
        noFeedbackAdapter = new NoFeedbackAdapter(getActivity(), R.layout.no_feed_back_list_item, list);
        recyclerView.setAdapter(noFeedbackAdapter);
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
                OkHttpManager.getInstance().getUserList(taskId, new NetCallBack() {
                    @Override
                    public void success(String response) {
                        JSONObject jsonObject = null;
                        String rsp = "";
                        try {
                            jsonObject = new JSONObject(response);

                            JSONArray jsonArray = new JSONObject(jsonObject.toString()).getJSONArray("data");
                            List<NoFeedBackInfo> feedBackAccounts = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<NoFeedBackInfo>>() {
                            }.getType());

                            list.clear();
                            list.addAll(feedBackAccounts);
                            if (feedBackAccounts.size() < 15) {
                                refreshLayout.setEnableLoadMore(false);
                            } else {
                                refreshLayout.setEnableLoadMore(true);
                            }

                            if (list.size() > 0) {
                                empty_lay.setVisibility(View.GONE);
                            } else {
                                empty_lay.setVisibility(View.VISIBLE);
                            }


                            noFeedbackAdapter.notifyDataSetChanged();
                            noFeedbackAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                                     num = list.get(position).getTelphone();
                                    callPhone();


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



    }


    public String getTitle() {
        return title;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e(TAG,"isVisibleToUser:"+isVisibleToUser);
        if (isVisibleToUser && isViewCreate &&!isDataInited) {
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
    @AfterPermissionGranted(RC_CALL_PERM)
    private void callPhone()
    {
        String[] perms = {Manifest.permission.CALL_PHONE};
        if (EasyPermissions.hasPermissions(getActivity(), perms)) {
            // Already have permission, do the thing
            PhoneUtils.call(num);
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                    getActivity(),
                    getString(R.string.rationale_sd),
                    RC_CALL_PERM,
                    Manifest.permission.CALL_PHONE);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


}
