package com.yq.news.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yq.news.R;
import com.yq.news.model.AccountInfo;
import com.yq.news.model.AccountType;
import com.yq.news.net.NetCallBack;
import com.yq.news.net.OkHttpManager;
import com.yq.news.pick.CardBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;


/**
 *  新增账号，编辑账号
 */
public class AddAccountActivity extends BaseActivity {


    /**
     * 账号类型布局
     */
    private RelativeLayout account_type_lay ;

    /**
     * 账号类型
     */
    private TextView account_tv ;

    /**
     * 账号ID
     */
    private String accountType ;

    /**
     * 编辑对象
     */

    private AccountInfo accountInfo ;

    private EditText account_id_et ,account_nick_et,account_owner_et;
    private OptionsPickerView accountPick;
    private ArrayList<CardBean> cardItem = new ArrayList<>();

    private FancyButton save_button ;
    public static  void startActivity(Activity activity,AccountInfo accountInfo )
    {
        Intent intent = new Intent(activity,AddAccountActivity.class);
        if(accountInfo != null) {
            intent.putExtra("accountInfo", accountInfo);
        }
        activity.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        initToolBar();
        initView();
        initPick();
        initData();
        getCardData();
    }

    private void initData()
    {
        Intent intent = getIntent();
        if(intent != null)
        {
            accountInfo =(AccountInfo) intent.getSerializableExtra("accountInfo");
            if(accountInfo != null)
            {
                toolbar_title.setText("编辑账号");
                account_id_et.setText(accountInfo.getAccountid());
                account_nick_et.setText(accountInfo.getNickname());
                account_owner_et.setText(accountInfo.getRealname());
                toolbar_add.setVisibility(View.VISIBLE);
                //toolbar_add.setText("删除");
                Drawable leftDrawable = null;
                leftDrawable = this.getResources().getDrawable(R.drawable.delete_account);
                leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(),
                        leftDrawable.getMinimumHeight());
                toolbar_add.setCompoundDrawables(leftDrawable, null,null , null);
                accountType = accountInfo.getAccounttype()+"";
                toolbar_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog("删除中...");
                       OkHttpManager.getInstance().deleAccount(accountInfo.getId()+"", new NetCallBack() {
                           @Override
                           public void success(String response) {
                               try {
                                   JSONObject jsonObject = new JSONObject(response);
                                   boolean modify = jsonObject.getBoolean("data");
                                   if(modify)
                                   {
                                       ToastUtils.showShort("删除账号成功");
                                       finish();
                                   }
                               } catch (JSONException e) {
                                   e.printStackTrace();
                                   ToastUtils.showShort(e.getMessage());
                               }
                               dismissDialog();
                           }

                           @Override
                           public void failed(String msg) {
                               ToastUtils.showShort(msg);
                           }
                       });
                    }
                });
            }else
            {
                toolbar_title.setText("新增账号");
            }
        }

    }
    private void initView()
    {
        //toolbar_title.setText("新增账号");
        account_type_lay = findViewById(R.id.account_type_lay);
        account_tv = findViewById(R.id.account_tv);
        save_button = findViewById(R.id.save_button);
        account_id_et = findViewById(R.id.account_id_et);
        account_nick_et = findViewById(R.id.account_nick_et);
        account_owner_et = findViewById(R.id.account_owner_et);
        account_type_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountPick.show();
            }
        });
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(accountType)) {
                    ToastUtils.showShort("账号类型不能为空");
                    return;
                }
                if (TextUtils.isEmpty(account_id_et.getText().toString())) {
                    ToastUtils.showShort("账号id不能为空");
                    return;
                }

                if (TextUtils.isEmpty(account_nick_et.getText().toString())) {
                    ToastUtils.showShort("账号昵称不能为空");
                    return;
                }

                if (TextUtils.isEmpty(account_owner_et.getText().toString())) {
                    ToastUtils.showShort("账号所属人不能为空");
                    return;
                }
                showDialog("保存中...");

                if (accountInfo != null) {
                    OkHttpManager.getInstance().editAccount(accountInfo.getId()+"",accountType, account_id_et.getText().toString(), account_nick_et.getText().toString(),
                            account_owner_et.getText().toString(), new NetCallBack() {

                                @Override
                                public void success(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        boolean modify = jsonObject.getBoolean("data");
                                        if (modify) {
                                            ToastUtils.showShort("编辑账号成功");
                                            finish();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        ToastUtils.showShort(e.getMessage());
                                    }
                                    dismissDialog();
                                }

                                @Override
                                public void failed(String msg) {
                                    dismissDialog();
                                    ToastUtils.showShort(msg);
                                }
                            });

                } else {
                    OkHttpManager.getInstance().addAccount(accountType, account_id_et.getText().toString(), account_nick_et.getText().toString(),
                            account_owner_et.getText().toString(), new NetCallBack() {

                                @Override
                                public void success(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        boolean modify = jsonObject.getBoolean("data");
                                        if (modify) {
                                            ToastUtils.showShort("新增账号成功");
                                            finish();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        ToastUtils.showShort(e.getMessage());
                                    }
                                    dismissDialog();
                                }

                                @Override
                                public void failed(String msg) {
                                    dismissDialog();
                                    ToastUtils.showShort(msg);
                                }
                            });
                }
            }
        });
    }

    private void initPick()
    {
        accountPick = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = cardItem.get(options1).getPickerViewText();
                accountType  = cardItem.get(options1).getId()+"";
                account_tv.setText(tx);
                accountPick.dismiss();
            }
        })
                .setLayoutRes(R.layout.pickerview_account_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                accountPick.returnData();
                                accountPick.dismiss();
                            }
                        });

                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //pvCustomOptions.dismiss();
                            }
                        });

                    }
                })
                .isDialog(true)
                .setOutSideCancelable(false)
                .build();

        accountPick.setPicker(cardItem);//添加数据
    }

    private void getCardData() {

        OkHttpManager.getInstance().getAccountTypeList(new NetCallBack() {
            @Override
            public void success(String response) {
                JSONObject jsonObject = null;
                String rsp = "";

                try {
                    jsonObject = new JSONObject(response);
                    rsp = jsonObject.getString("data");
                    List<AccountType> accountTypeList = new Gson().fromJson(rsp, new TypeToken<List<AccountType>>() {}.getType());

                   for(AccountType accountType :accountTypeList)
                   {
                       cardItem.add(new CardBean(accountType.getId(),accountType.getName()));
                       if(accountInfo != null)
                       {
                           if(accountInfo.getAccounttype() == accountType.getId())
                           {
                               runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       account_tv.setText(accountType.getName());
                                   }
                               });
                           }
                       }
                   }
                    for (int i = 0; i < cardItem.size(); i++) {
                        if (cardItem.get(i).getCardNo().length() > 6) {
                            String str_item = cardItem.get(i).getCardNo().substring(0, 6) + "...";
                            cardItem.get(i).setCardNo(str_item);
                        }
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
}
