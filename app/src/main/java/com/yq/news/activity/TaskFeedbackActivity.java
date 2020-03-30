package com.yq.news.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.yq.news.MainActivity;
import com.yq.news.R;
import com.yq.news.fragment.ManagerFragment;
import com.yq.news.model.AccountInfo;
import com.yq.news.model.AccountType;
import com.yq.news.model.FeedBackInfo;
import com.yq.news.model.FeedBackUser;
import com.yq.news.net.NetCallBack;
import com.yq.news.net.OkHttpManager;
import com.yq.news.pick.CardBean;
import com.zhy.adapter.recyclerview.CommonAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * 处置反馈页面
 */
public class TaskFeedbackActivity extends  BaseActivity  {

    private static final String TAG = "TaskFeedbackActivity";

    private String taskId ;

    private ImageView  pic_add_img ;

    private int chooseMode = PictureMimeType.ofImage();

    private int themeId;

    private List<LocalMedia> selectList = new ArrayList<>();

    private List<LocalMedia> selectListCompress = new ArrayList<>();
    private static final int RC_SD_PERM = 1000;


    /**
     *  账号类型 lay
     */

    private RelativeLayout account_type_lay ;

    /**
     * 账号类型 tv
     */
    private TextView account_type_tv ;

    /**
     * 账号lay
     */
    private RelativeLayout account_id_lay ;


    private RelativeLayout account_nick_lay ;

    private TextView account_nick_tv ;
    /**
     * 账号类型
     */
   private  List<AccountType> accountTypeList ;


    private List<AccountInfo> accountInfos ;

   private  AlertDialog alertDialog ;

   private int selectAccount = 0;

    private FancyButton save_button ;

    /**
     * 编辑反馈
     */
   private  FeedBackInfo feedBackInfo ;

    /**
     *
     */
    private  String  type ;


    public static  void startActivity(Activity activity ,int requestCode,String taskId,String type ,FeedBackInfo feedBackInfo)
    {
        Intent intent = new Intent(activity,TaskFeedbackActivity.class);
        intent.putExtra("taskId",taskId);
        intent.putExtra("feedBackInfo",feedBackInfo);
        intent.putExtra("type",type);
        activity.startActivityForResult(intent,requestCode);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_feedback);
        themeId  = R.style.picture_default_style;
        initToolBar();
        initView();
        initData();
        getCardData();
    }

    private void initData()
    {
        taskId = getIntent().getStringExtra("taskId");
        feedBackInfo = (FeedBackInfo)getIntent().getSerializableExtra("feedBackInfo");
        type = getIntent().getStringExtra("type");

        account_type_tv.setText("1048576".equals(type)? "微博":"微信");
        if(feedBackInfo!= null)
        {
            toolbar_title.setText("编辑反馈");
            account_type_tv.setText(feedBackInfo.getAccountTypeName());
            account_nick_tv.setText(feedBackInfo.getAccountname());
            Glide.with(this).load(feedBackInfo.getImgPath()).into(pic_add_img);
            Drawable leftDrawable = null;
            leftDrawable = this.getResources().getDrawable(R.drawable.delete_account);
            leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(),
                    leftDrawable.getMinimumHeight());
            toolbar_add.setCompoundDrawables(leftDrawable, null,null , null);
            toolbar_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog("删除中");
                    OkHttpManager.getInstance().deleFeedBack(feedBackInfo.getId()+"", new NetCallBack() {
                        @Override
                        public void success(String response) {
                            ToastUtils.showLong("删除成功");
                            dismissDialog();
                            setResult(RESULT_OK);
                            finish();

                        }

                        @Override
                        public void failed(String msg) {
                            dismissDialog();
                            ToastUtils.showLong(msg);
                        }
                    });
                }
            });

        }else{
            toolbar_title.setText("任务反馈");
        }
        toolbar_add.setVisibility(View.VISIBLE);
       // toolbar_add.setText("确定");


    }

    private void initView()
    {

        account_type_lay = findViewById(R.id.account_type_lay);
        account_type_tv = findViewById(R.id.account_type_tv);
        account_id_lay = findViewById(R.id.account_id_lay);
        account_nick_lay = findViewById(R.id.account_nick_lay);
        account_nick_tv = findViewById(R.id.account_nick_tv);
        save_button = findViewById(R.id.save_button);
        pic_add_img = findViewById(R.id.pic_add_img);
       /* account_type_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(accountTypeList != null)
                {
                    int size = accountTypeList.size();
                    String[] items = new String[size];
                    for(int i = 0;i< size;i++)
                    {
                        items[i] = accountTypeList.get(i).getName();
                    }
                    showAlertDialog("账号类型",items,1);
                }

            }
        });*/
        account_nick_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(accountInfos != null)
                {
                    int size = accountInfos.size();
                    String[] items = new String[size];
                    for(int i = 0;i< size;i++)
                    {
                        items[i] = accountInfos.get(i).getNickname();
                    }
                    showAlertDialog("账号昵称",items,2);
                }

                           }
        });
        pic_add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyPermissions.requestPermissions(
                        TaskFeedbackActivity.this,
                        getString(R.string.rationale_sd),
                        RC_SD_PERM,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,  Manifest.permission.CAMERA);

            }
        });
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(account_type_tv.getText().toString()))
                {
                    ToastUtils.showLong("账号类型不能为空");
                    return;
                }

                if(TextUtils.isEmpty(account_nick_tv.getText().toString()))
                {
                    ToastUtils.showLong("账号昵称不能为空");
                    return;
                }
                if(feedBackInfo == null) {
                    if (ObjectUtils.isEmpty(selectListCompress)) {
                        ToastUtils.showLong("图片不能为空");
                        return;
                    }
                }

                showDialog("提交中...");
                if(feedBackInfo == null)
                {
                    OkHttpManager.getInstance().addFeedBack(taskId, accountInfos.get(selectAccount).getId()+"", selectListCompress.get(0).getCompressPath(), new NetCallBack() {
                        @Override
                        public void success(String response) {

                            ToastUtils.showLong("反馈成功");
                            dismissDialog();
                            setResult(RESULT_OK);
                            finish();
                        }

                        @Override
                        public void failed(String msg) {
                            ToastUtils.showLong(msg);
                            dismissDialog();
                        }
                    });
                }else
                {
                    String file = "";
                    if(selectListCompress.size() >0)
                    {
                        file = selectListCompress.get(0).getCompressPath();
                    }
                    OkHttpManager.getInstance().editFeedBack(feedBackInfo.getId()+"",taskId, accountInfos.get(selectAccount).getId()+"", file, new NetCallBack() {
                        @Override
                        public void success(String response) {

                            ToastUtils.showLong("反馈成功");
                            dismissDialog();
                            setResult(RESULT_OK);
                            finish();
                        }

                        @Override
                        public void failed(String msg) {
                            ToastUtils.showLong(msg);
                            dismissDialog();
                        }
                    });
                }


            }
        });
    }
    @AfterPermissionGranted(RC_SD_PERM)
    private void addPhoto()
    {
        PictureSelector.create(this)
                .openGallery(chooseMode)
                .theme(themeId)
                .maxSelectNum(1)
                .minSelectNum(1)
                .selectionMode( PictureConfig.MULTIPLE )
                .previewImage(true)
                .previewVideo(false)
                .enablePreviewAudio(false) // 是否可播放音频
                .isCamera(true)
                .enableCrop(false)
                .compress(true)
                .glideOverride(160, 160)
                .previewEggs(true)
                .hideBottomControls( true)
                .isGif(false)
                .freeStyleCropEnabled(false)
                .showCropGrid(false)
                .openClickSound(false)
                .selectionMedia(selectList)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择
                    selectListCompress = PictureSelector.obtainMultipleResult(data);
                    Glide.with(this).load(selectListCompress.get(0).getCompressPath()).into(pic_add_img);
                    break;
            }
        }
    }

    private void getCardData() {
        getAllAccount(type);
       /* OkHttpManager.getInstance().getAccountTypeList(new NetCallBack() {
            @Override
            public void success(String response) {
                JSONObject jsonObject = null;
                String rsp = "";

                try {
                    jsonObject = new JSONObject(response);
                    rsp = jsonObject.getString("data");
                    accountTypeList = new Gson().fromJson(rsp, new TypeToken<List<AccountType>>() {}.getType());
                    if(!ObjectUtils.isEmpty(accountTypeList)) {
                        if(feedBackInfo== null) {
                          //  account_type_tv.setText(accountTypeList.get(0).getName());
                        }
                        getAllAccount(accountTypeList.get(0).getId()+"");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            @Override
            public void failed(String msg) {

            }
        });*/

    }

    private void getAllAccount(String type)
    {
       OkHttpManager.getInstance().getAllAccountList(type, new NetCallBack() {
           @Override
           public void success(String response) {
               JSONObject jsonObject = null;

               try {
                   jsonObject = new JSONObject(response);
                   JSONArray jsonArray = new JSONObject(jsonObject.toString()).getJSONArray("data");
                   accountInfos = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<AccountInfo>>() {}.getType());
                   if(!ObjectUtils.isEmpty(accountInfos)) {
                       account_nick_tv.setText(accountInfos.get(0).getNickname());

                   }else
                   {
                       account_nick_tv.setText("");
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

    /**
     *  type = 1  accoutType
     *  type = 2 accoutNick
     *
     * @param title
     * @param items
     * @param type
     */
    private  void  showAlertDialog(String title,String[] items,int type)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(TaskFeedbackActivity.this);
        builder.setTitle(title);
        int checkItem = 0;
        if(type ==1) {
            checkItem = Arrays.asList(items).indexOf(account_type_tv.getText().toString());
        }else {
            checkItem = Arrays.asList(items).indexOf(account_nick_tv.getText().toString());
        }
        builder.setSingleChoiceItems(items,checkItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(type ==1) {
                    account_type_tv.setText(accountTypeList.get(which).getName());
                    getAllAccount(accountTypeList.get(which).getId()+"");
                }else
                {
                    account_nick_tv.setText(accountInfos.get(which).getNickname());
                    selectAccount = which;
                }
                alertDialog.dismiss();
            }
        });

        alertDialog =  builder.show();


    }
}
