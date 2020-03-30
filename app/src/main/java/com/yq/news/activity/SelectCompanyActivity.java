package com.yq.news.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yq.news.R;
import com.yq.news.itf.OnCheckBtnClick;
import com.yq.news.model.ClerkInfo;
import com.yq.news.model.DepartmentBean;
import com.yq.news.model.Node;
import com.yq.news.model.TaskBean;
import com.yq.news.model.TaskInfoUser;
import com.yq.news.net.NetCallBack;
import com.yq.news.net.OkHttpManager;
import com.yq.news.treeview.bean.Dir;
import com.yq.news.treeview.viewbinder.DirectoryNodeBinder;
import com.yq.news.treeview.viewbinder.FileNodeBinder;
import com.yq.util.NodeUtils;
import com.yq.util.RelativeDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tellh.com.recyclertreeview_lib.TreeNode;
import tellh.com.recyclertreeview_lib.TreeViewAdapter;

/**
 * 选择单位
 */
public class SelectCompanyActivity extends BaseActivity {

    private static final String TAG = "SelectCompanyActivity";

    private RecyclerView rv;

    private TreeViewAdapter adapter;

    private String deptId ;

    private SparseArray<Boolean> nodeSelect ;

    List<String> selectId = new ArrayList<>();

    List<ClerkInfo> clerkInfoList = new ArrayList<>();
    private List<Dir> selectTree ;

    private TaskBean taskBean ;


    public static  void startActivity(Activity activity ,int requestCode, TaskBean taskBean,List<ClerkInfo> clerkInfoList)
    {
        Intent intent = new Intent(activity,SelectCompanyActivity.class);
        intent.putExtra("task",taskBean);
        intent.putExtra("clerkInfo",(Serializable) clerkInfoList);
        activity.startActivityForResult(intent,requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_company);
        initView();
        initData();
    }

    private void initView()
    {
         initToolBar();
         toolbar_title.setText("选择单位");
         deptId =   SPUtils.getInstance().getString("deptId");
         rv = (RecyclerView) findViewById(R.id.rv);
         toolbar_add.setText("确定");
        toolbar_add.setVisibility(View.VISIBLE);
        toolbar_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ArrayList<ClerkInfo> clerkInfos = new ArrayList<ClerkInfo>();
                if(selectTree.size()==0)
                {
                    ToastUtils.showLong("请选择人员");
                    return;
                }
                for(Dir dir:selectTree)
                {
                    ClerkInfo clerkInfo = new ClerkInfo();
                    clerkInfo.id = dir.id;
                    clerkInfo.name = dir.dirName;
                    clerkInfos.add(clerkInfo);
                }
                intent.putExtra("selectClerk",clerkInfos);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
    }

    private void initData() {
        nodeSelect = new SparseArray();
        selectTree = new ArrayList<>();
        Intent intent = getIntent();
        if(intent !=null) {
            taskBean = (TaskBean) intent.getSerializableExtra("task");
            clerkInfoList = (List<ClerkInfo>) intent.getSerializableExtra("clerkInfo");
            if (taskBean != null) {
                List<TaskInfoUser> taskInfoUsers = taskBean.getTaskInfoUsers();

                if(!ObjectUtils.isEmpty(taskInfoUsers))
                {
                   for(TaskInfoUser taskInfoUser :taskInfoUsers)
                   {
                       selectTree.add(new Dir(taskInfoUser.getRealname(),5000+(taskInfoUser.getId()+"")));
                       selectId.add(5000+(taskInfoUser.getId()+""));
                   }
                }
            }
            if(!ObjectUtils.isEmpty(clerkInfoList))
            {
                selectId.clear();
                selectTree.clear();
                for(ClerkInfo  clerkInfo :clerkInfoList)
                {
                    selectTree.add(new Dir(clerkInfo.name,clerkInfo.id));
                    selectId.add(clerkInfo.id);
                }
            }
        }
        OkHttpManager.getInstance().getDeptList( new NetCallBack() {
            @Override
            public void success(String response) {
                JSONObject jsonObject = null;
                String data = "";
                try {
                     jsonObject = new JSONObject(response);
                      data = jsonObject.getString("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                     List<DepartmentBean> departmentBeans = new Gson().fromJson(data, new TypeToken<List<DepartmentBean>>() {
                     }.getType());
                for(int i = 0;i<departmentBeans.size();i++)
                {
                    nodeSelect.put(i,false);

                }
                    List<DepartmentBean> trees = new ArrayList<>();

                    for (DepartmentBean departmentBean : departmentBeans) {

                              if (departmentBean.getpId()==Integer.parseInt(1000+deptId)) {
                                      trees.add(departmentBean);
                                 }
                                  for (DepartmentBean child : departmentBeans) {
                                            if (child.getpId() == departmentBean.getId()) {
                                                     if (departmentBean.getChildren().size() == 0) {
                                                        }
                                                      departmentBean.add(child);
                                                 }
                                        }
                    }
                    Log.e(TAG,"tree:"+trees);
                    List<TreeNode> rootNodes = new ArrayList<>();
                    rootNodes = NodeUtils.buildTreeByRecursive(trees);

                    rv.setLayoutManager(new LinearLayoutManager(SelectCompanyActivity.this));
                    DirectoryNodeBinder directoryNodeBinder = new DirectoryNodeBinder();
                    directoryNodeBinder.setSelectTree(selectTree);
                    directoryNodeBinder.setSelectId(selectId);
                   for (int i = 0;i<rootNodes.size();i++)
                  {
                    if(selectId.size() == rootNodes.get(i).getChildList().size())
                    {
                        Dir rootDir = (Dir) rootNodes.get(i).getContent();
                        selectId.add(rootDir.id);
                    }
                  }
                    adapter = new TreeViewAdapter(rootNodes, Arrays.asList(new FileNodeBinder(), directoryNodeBinder));


                   directoryNodeBinder.setOnRadioBtnClick(new OnCheckBtnClick() {
                    @Override
                    public void onClick(TreeNode node,boolean isCheck) {
                       // Dir dirNode = (Dir) node.getContent();


                        List<TreeNode> child = node.getChildList();
                        Dir rootDir = (Dir) node.getContent();

                        if(isCheck)
                        {
                            selectId.add(rootDir.id);
                            for(int i = 0; i < child.size();i++)
                            {

                                TreeNode childNode = child.get(i);
                                Dir dir = (Dir) childNode.getContent();
                                    selectId.add(dir.id);
                                    selectTree.add(dir);

                            }
                        }else
                        {

                            selectId.clear();
                            selectTree.clear();

                        }
                       /* if(!selectId.contains(rootDir.id))
                        {

                        }else
                        {

                        }*/
                       /* for(int i = 0; i < child.size();i++)
                        {

                            TreeNode childNode = child.get(i);
                            Dir dir = (Dir) childNode.getContent();
                            if(isCheck) //!selectId.contains(dir.id)
                            {
                                selectId.add(dir.id);
                                selectTree.add(dir);
                            }else
                            {
                                selectId.clear();
                                selectTree.clear();
                            }


                           *//* if(isCheck)
                            {
                               // Dir dir = (Dir) childNode.getContent();
                                nodeSelect.put(i,true);
                                selectTree.add(dir);
                            }else
                            {
                               // Dir dir = (Dir) childNode.getContent();
                                nodeSelect.put(i,false);
                                selectTree.remove(dir);
                            }*//*


                        }*/
                        directoryNodeBinder.setSelectId(selectId);
                       // directoryNodeBinder.setSelectTree(selectTree);
                        adapter.notifyDataSetChanged();
                    }
                });
                    adapter.setOnTreeNodeListener(new TreeViewAdapter.OnTreeNodeListener() {
                        @Override
                        public boolean onClick(TreeNode node, RecyclerView.ViewHolder holder) {
                            if (!node.isLeaf()) {
                                //Update and toggle the node.
                                onToggle(!node.isExpand(), holder);
//                    if (!node.isExpand())
//                        adapter.collapseBrotherNode(node);
                            }else
                            {
                                boolean isSelect = nodeSelect.get(holder.getAdapterPosition());
                                Dir dir = (Dir) node.getContent();
                                if(!selectId.contains(dir.id))
                                {
                                    selectId.add(dir.id);
                                    selectTree.add(dir);
                                }else
                                {
                                    selectId.remove(dir.id);
                                    boolean remove = selectTree.remove(dir);
                                    Log.e(TAG,"remove=="+remove);
                                }
                                directoryNodeBinder.setSelectId(selectId);

                               /* if(!isSelect)
                                {
                                    nodeSelect.put(holder.getAdapterPosition(),true);

                                    selectTree.add(dir);
                                }else
                                {

                                    nodeSelect.put(holder.getAdapterPosition(),false);
                                    selectTree.remove(dir);
                                }

                                directoryNodeBinder.setSelectTree(selectTree);*/
                                adapter.notifyDataSetChanged();
                                 /*Dir dir = (Dir) node.getContent();

                                 Intent intent = new Intent();
                                intent.putExtra("dept",dir.dirName);
                                intent.putExtra("id",dir.id);
                                setResult(RESULT_OK,intent);
                                finish();*/
                            }
                            return false;
                        }

                        @Override
                        public void onToggle(boolean isExpand, RecyclerView.ViewHolder holder) {
                            DirectoryNodeBinder.ViewHolder dirViewHolder = (DirectoryNodeBinder.ViewHolder) holder;
                            final ImageView ivArrow = dirViewHolder.getIvArrow();
                            int rotateDegree = isExpand ? 90 : -90;
                            ivArrow.animate().rotationBy(rotateDegree)
                                    .start();
                        }
                    });
                    rv.setAdapter(adapter);

            }

            @Override
            public void failed(String msg) {

            }
        });

    }
}
