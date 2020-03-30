package com.yq.news.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yq.news.R;
import com.yq.news.adapter.MsgListAdapter;
import com.yq.news.itf.OnEditClick;
import com.yq.news.model.AccountInfo;
import com.yq.news.model.LoginInfo;
import com.yq.news.model.MsgInfo;
import com.yq.news.net.NetCallBack;
import com.yq.news.net.OkHttpManager;
import com.yq.news.view.Watermark;
import com.yq.util.Constant;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 消息列表
 */
public class MsgListActivity extends BaseActivity {




    private MsgListAdapter msgListAdapter;

    List<MsgInfo> list;

    private int pageNo = 1 ;

    private int pageSize = 15 ;

    private  int unReadNum ;

    public static  void startActivity(Activity activity ,int unReadNum)
    {
        Intent intent = new Intent(activity, MsgListActivity.class);
        intent.putExtra("unReadeNum",unReadNum);
        activity.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_list);
        initToolBar();
        initRcy();
        initData();

    }
    private void initData()
    {
        Intent intent = getIntent();
        if(intent != null)
        {
            unReadNum = intent.getIntExtra("unReadeNum",0);
            toolbar_title.setText("未读"+"("+unReadNum+")");
            toolbar_add.setVisibility(View.VISIBLE);
            toolbar_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddAccountActivity.startActivity(MsgListActivity.this,null);
                }
            });
            List<String> labels = new ArrayList<>();
            LoginInfo loginInfo = (LoginInfo) CacheDiskUtils.getInstance().getSerializable(Constant.LOGIN_CACHE_KEY);
            if(loginInfo != null && loginInfo.getData()!= null) {
                labels.add(loginInfo.getData().getRealname() + " " + loginInfo.getData().getTelphone());
                Watermark.getInstance().show(this, loginInfo.getData().getRealname() + " " + loginInfo.getData().getTelphone());
            }

            list = new ArrayList<>();
            msgListAdapter = new MsgListAdapter(this,R.layout.msg_list_item, list);
            msgListAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    MsgInfo msgInfo = list.get(position);
                    TaskInfoActivity.startActivity(MsgListActivity.this,msgInfo.getRelateid()+"");
                    OkHttpManager.getInstance().postMsgRead(msgInfo.getId()+"","1", new NetCallBack() {
                        @Override
                        public void success(String response) {

                        }

                        @Override
                        public void failed(String msg) {

                        }
                    });
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
            recyclerView.setAdapter(msgListAdapter);
            onLoadData();
        }
    }

    private void onLoadData() {
        refreshLayout.autoRefresh();
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {

                getData(false);
            }
        });



    }

    private void reloadData()
    {
        getData(false);
    }
    private void getData(boolean  isOnLoaderMore)
    {
        OkHttpManager.getInstance().getMessageList("0",new NetCallBack() {
            @Override
            public void success(String response) {
                JSONObject jsonObject = null;
                String rsp = "";
                try {
                    jsonObject = new JSONObject(response);
                    JSONArray jsonArray = new JSONObject(jsonObject.toString()).getJSONArray("data");
                    List<MsgInfo> msgInfos = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<MsgInfo>>() {
                    }.getType());
                    list.clear();
                    list.addAll(msgInfos);
                    toolbar_title.setText("未读"+"("+list.size()+")");
                    msgListAdapter.notifyDataSetChanged();
                    refreshLayout.finishRefresh();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(String msg) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        pageNo = 1 ;
        list.clear();
        reloadData();
    }
}
