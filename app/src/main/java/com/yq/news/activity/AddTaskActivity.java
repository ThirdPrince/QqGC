package com.yq.news.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yq.news.MainActivity;
import com.yq.news.R;
import com.yq.news.model.ClerkInfo;
import com.yq.news.model.TaskBean;
import com.yq.news.model.TaskInfoUser;
import com.yq.news.net.NetCallBack;
import com.yq.news.net.OkHttpManager;
import com.yq.util.RelativeDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.yq.util.Constant.REFRESH_TASK_BROADCAST;
import static com.yq.util.Constant.TEXT_FONT_SIZE_BROADCAST;

/**
 * 添加任务 （管理员）
 */
public class AddTaskActivity extends BaseActivity {


    /**
     *  任务名称
     */

    private EditText task_name_et ;

    /**
     *  任务描述
     */

    private EditText task_describe_et;

    /**
     *  导控数量
     */

    private EditText task_control_num_et ;

    private RelativeLayout task_start_lay ;

    private RelativeLayout task_end_lay ;

    private RelativeLayout select_person_lay ;

    private RelativeLayout select_type_lay ;

    /**
     * 任务选择
     */
    private TextView select_type_tv ;

    private String taskType ;

    private TextView select_person_tv  ;

    private TimePickerView pvStartTime;

    private TimePickerView pvEndTime;

    private TextView task_start_tv ;

    private TextView task_end_tv ;

    private final int REQUEST_CODE_COM = 1024;

    private List<ClerkInfo> clerkInfos ;

    private StringBuilder sbClerk;

    private StringBuilder sbClerkId;

    private TaskBean taskBean ;

    private  AlertDialog alertDialog ;

    /**
     * 记录选中的人员
     */
    List<String> selectId = new ArrayList<>();


    public static  void startActivity(Activity activity , TaskBean taskBean)
    {
        Intent intent = new Intent(activity,AddTaskActivity.class);
        intent.putExtra("task",taskBean);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        initToolBar();
        initView();
        initStartTimePicker();
        initEndTimePicker();
        initData();
    }

    private void initData()
    {

        sbClerkId = new StringBuilder();
        sbClerk = new StringBuilder();
        Intent intent = getIntent();
        if(intent !=null)
        {
            taskBean = (TaskBean) intent.getSerializableExtra("task");
            if(taskBean != null)
            {
                toolbar_title.setText("编辑任务");
                List<TaskInfoUser> taskInfoUsers = taskBean.getTaskInfoUsers();
                task_name_et.setText(taskBean.getName());
                task_describe_et.setText(taskBean.getDescription());
                task_control_num_et.setText(taskBean.getPilotcount()+"");
                task_start_tv.setText(RelativeDateFormat.timeStamp2Date(taskBean.getStarttime()));
                task_end_tv.setText(RelativeDateFormat.timeStamp2Date(taskBean.getEndtime()));
                String type = "1048576".equals(taskBean.getType())? "微博":"微信";
                taskType = "1048576" ;
                select_type_tv.setText(type);
                if(!ObjectUtils.isEmpty(taskInfoUsers))
                {
                    StringBuilder stringBuilder = new StringBuilder();
                    for(int i = 0;i<taskInfoUsers.size();i++)
                    {

                        sbClerkId.append(taskInfoUsers.get(i).getId()+",");
                        if(i==taskInfoUsers.size()-1)
                        {
                            stringBuilder.append(taskInfoUsers.get(i).getRealname());
                        }else
                        {
                            stringBuilder.append(taskInfoUsers.get(i).getRealname()+",");
                        }


                    }
                    select_person_tv.setText(stringBuilder);
                }


            }else
            {
                toolbar_title.setText("添加任务");
                taskType = "1048576" ;
                select_type_tv.setText("微博");
            }
        }
    }
    private void initView()
    {
        task_name_et = findViewById(R.id.task_name_et);
        task_describe_et = findViewById(R.id.task_describe_et);
        task_control_num_et = findViewById(R.id.task_control_num_et);
        task_start_lay = findViewById(R.id.task_start_lay);
        task_end_lay = findViewById(R.id.task_end_lay);
        task_start_tv = findViewById(R.id.task_start_tv);
        task_end_tv = findViewById(R.id.task_end_tv);
        select_person_lay = findViewById(R.id.select_person_lay);
        select_type_lay = findViewById(R.id.select_type_lay);
        select_type_tv = findViewById(R.id.select_type_tv);
        select_person_tv = findViewById(R.id.select_person_tv);
        toolbar_add.setText("下发");
        toolbar_add.setVisibility(View.VISIBLE);
        toolbar_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(task_name_et.getText().toString())) {
                    ToastUtils.showLong("任务名称不能为空");
                    return;
                }

                if (TextUtils.isEmpty(task_describe_et.getText().toString())) {
                    ToastUtils.showLong("任务描述不能为空");
                    return;
                }

                if (TextUtils.isEmpty(task_control_num_et.getText().toString())) {
                    ToastUtils.showLong("导控数量不能为空");
                    return;
                }

                int controlNum = Integer.parseInt(task_control_num_et.getText().toString());
                if (controlNum == 0 || controlNum >=10000) {
                    ToastUtils.showLong("导控数量输入不合法");
                    return;
                }

                if (TextUtils.isEmpty(task_start_tv.getText().toString())) {
                    ToastUtils.showLong("开始时间不能为空");
                    return;
                }

                if (TextUtils.isEmpty(task_end_tv.getText().toString())) {
                    ToastUtils.showLong("结束时间不能为空");
                    return;
                }

                if (TextUtils.isEmpty(sbClerkId.toString())) {
                    ToastUtils.showLong("文员不能为空");
                    return;
                }

                if (TextUtils.isEmpty(taskType)) {
                    ToastUtils.showLong("任务类型不能为空");
                    return;
                }

                if (taskBean == null) {
                    showDialog("下发任务...");

                    OkHttpManager.getInstance().addTaskApi(sbClerkId.toString(), task_name_et.getText().toString(), task_describe_et.getText().toString(),taskType,
                            task_start_tv.getText().toString()+":00", task_end_tv.getText().toString()+":00", task_control_num_et.getText().toString(), new NetCallBack() {
                                @Override
                                public void success(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        boolean modify = jsonObject.getBoolean("data");
                                        refreshTask();
                                        if (modify) {
                                            ToastUtils.showShort("下发任务成功");
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
                                    ToastUtils.showLong(msg);
                                    dismissDialog();
                                }
                            });


                }else
                {
                    showDialog("下发任务...");

                    OkHttpManager.getInstance().editTaskApi(sbClerkId.toString(), task_name_et.getText().toString(), task_describe_et.getText().toString(),
                            taskType,task_start_tv.getText().toString()+":00", task_end_tv.getText().toString()+":00", task_control_num_et.getText().toString(),taskBean.getId()+"", new NetCallBack() {
                                @Override
                                public void success(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        boolean modify = jsonObject.getBoolean("data");
                                        refreshTask();
                                        if (modify) {
                                            ToastUtils.showShort("下发任务成功");
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
                                    ToastUtils.showLong(msg);
                                    dismissDialog();
                                }
                            });

                }
            }
        });
        select_type_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog("任务类型",new String[]{"微博","微信"});
            }
        });
        task_start_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvStartTime.show(v);

            }
        });
        task_end_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvEndTime.show(v);
            }
        });

        select_person_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectCompanyActivity.startActivity(AddTaskActivity.this,REQUEST_CODE_COM,taskBean,clerkInfos);
            }
        });

    }

    private void initStartTimePicker() {//Dialog 模式下，在底部弹出
        pvStartTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                task_start_tv.setText(getTime(date)+":00");

               // Toast.makeText(AddTaskActivity.this, getTime(date), Toast.LENGTH_SHORT).show();
                Log.i("pvTime", "onTimeSelect");

            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        Log.i("pvTime", "onTimeSelectChanged");
                    }
                })
                .setType(new boolean[]{true, true, true, true, false, false})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .addOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("pvTime", "onCancelClickListener");
                    }
                })
                .setItemVisibleCount(5) //若设置偶数，实际值会加1（比如设置6，则最大可见条目为7）
                .setLineSpacingMultiplier(2.0f)
                .isAlphaGradient(true)
                .build();

        Dialog mDialog = pvStartTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvStartTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.3f);
            }
        }
    }

    private void initEndTimePicker() {//Dialog 模式下，在底部弹出
        pvEndTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                task_end_tv.setText(getTime(date)+":00");

                // Toast.makeText(AddTaskActivity.this, getTime(date), Toast.LENGTH_SHORT).show();
                Log.i("pvTime", "onTimeSelect");

            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        Log.i("pvTime", "onTimeSelectChanged");
                    }
                })
                .setType(new boolean[]{true, true, true, true, false, false})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .addOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("pvTime", "onCancelClickListener");
                    }
                })
                .setItemVisibleCount(5) //若设置偶数，实际值会加1（比如设置6，则最大可见条目为7）
                .setLineSpacingMultiplier(2.0f)
                .isAlphaGradient(true)
                .build();

        Dialog mDialog = pvEndTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvEndTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.3f);
            }
        }
    }
    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH");//"yyyy-MM-dd HH:mm"
        return format.format(date);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_CODE_COM:
                if(resultCode == Activity.RESULT_OK)
                {
                    selectId.clear();
                    clerkInfos = (ArrayList<ClerkInfo>) data.getSerializableExtra("selectClerk");
                    sbClerk = new StringBuilder();
                    sbClerkId = new StringBuilder();
                    for(int i = 0;i< clerkInfos.size();i++)
                    {
                        ClerkInfo clerkInfo = clerkInfos.get(i);
                        String id = clerkInfo.id.substring(4,clerkInfo.id.length());
                        sbClerkId.append(id+",");
                        selectId.add(clerkInfo.id);
                        if(i== clerkInfos.size()-1)
                        {
                            sbClerk.append(clerkInfo.name);

                        }else
                        {
                            sbClerk.append(clerkInfo.name+",");
                        }

                    }

                    select_person_tv.setText(sbClerk.toString());

                }
                break;
        }
    }

    private  void  showAlertDialog(String title,String[] items)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddTaskActivity.this);
        builder.setTitle(title);
        int checkItem = 0;

         checkItem = Arrays.asList(items).indexOf(select_type_tv.getText().toString());
         if(checkItem < 0 )
         {
             checkItem =  0;
         }

        builder.setSingleChoiceItems(items,checkItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                select_type_tv.setText(items[which]);
                taskType = which == 0 ? "1048576" :"268435456";
                alertDialog.dismiss();
            }
        });

        alertDialog =  builder.show();


    }

    private void refreshTask()
    {
        Intent intent = new Intent(REFRESH_TASK_BROADCAST);
        LocalBroadcastManager.getInstance(AddTaskActivity.this).sendBroadcast(intent);
    }
}
