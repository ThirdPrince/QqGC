package com.yq.news.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yq.news.R;
import com.yq.news.adapter.AccountListAdapter;
import com.yq.news.adapter.MainInfoPageAdapter;
import com.yq.news.itf.OnEditClick;
import com.yq.news.model.AccountInfo;
import com.yq.news.model.ArticleInfo;
import com.yq.news.model.LoginInfo;
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
 * 账号管理列表
 */
public class AccountListActivity extends BaseActivity {


    List<AccountInfo> list ;

    private AccountListAdapter accountListAdapter;
  /*  private RefreshLayout refreshLayout ;
    protected RecyclerView recyclerView ;
    protected ClassicsHeader mClassicsHeader;*/

    private int pageNo = 1 ;

    private int pageSize = 15 ;

    public static  void startActivity(Activity activity )
    {
        Intent intent = new Intent(activity,AccountListActivity.class);
        activity.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_list);
        initToolBar();
        initRcy();

        toolbar_title.setText("账号管理");
        toolbar_add.setVisibility(View.VISIBLE);
        toolbar_add.setText("新增");
        toolbar_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAccountActivity.startActivity(AccountListActivity.this,null);
            }
        });
        List<String> labels = new ArrayList<>();
        LoginInfo loginInfo = (LoginInfo) CacheDiskUtils.getInstance().getSerializable(Constant.LOGIN_CACHE_KEY);
        if(loginInfo != null && loginInfo.getData()!= null) {
            labels.add(loginInfo.getData().getRealname() + " " + loginInfo.getData().getTelphone());
            Watermark.getInstance().show(this, loginInfo.getData().getRealname() + " " + loginInfo.getData().getTelphone());
        }

        list = new ArrayList<>();
        accountListAdapter = new AccountListAdapter(this,R.layout.account_list_item,list);
        accountListAdapter.setOnEditClick(new OnEditClick() {
            @Override
            public void onClick(Object accountInfo, View view) {
                AddAccountActivity.startActivity(AccountListActivity.this,(AccountInfo) accountInfo);
            }
        });
        recyclerView.setAdapter(accountListAdapter);
        onLoadData();
    }

    private void onLoadData() {
        refreshLayout.autoRefresh();
        refreshLayout.setEnableAutoLoadMore(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                pageNo = 1 ;
                list.clear();
                getData(false);
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNo ++ ;
                getData(true);
            }
        });

    }

    private void reloadData()
    {
        getData(false);
    }
    private void getData(boolean  isOnLoaderMore)
    {
        OkHttpManager.getInstance().getAccountDataList(pageNo+"", pageSize+"", new NetCallBack() {
            @Override
            public void success(String response) {
                JSONObject jsonObject = null;
                String rsp = "";
                try {
                    jsonObject = new JSONObject(response);
                    rsp = jsonObject.getString("data");
                    JSONArray jsonArray = new JSONObject(rsp).getJSONArray("list");
                    List<AccountInfo> favoriteList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<AccountInfo>>() {}.getType());
                    //list.clear();
                    list.addAll(favoriteList);
                    if(favoriteList.size()<15)
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
                    accountListAdapter.notifyDataSetChanged();

                    accountListAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                        }

                        @Override
                        public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                            return false;
                        }
                    });
                    if(isOnLoaderMore)
                    {
                        refreshLayout.finishLoadMore();

                    }else
                    {
                        refreshLayout.finishRefresh();
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
