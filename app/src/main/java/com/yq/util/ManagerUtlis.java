package com.yq.util;

import android.util.Log;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.yq.news.model.LoginInfo;

public class ManagerUtlis {


    public static boolean isManager()
    {
       LoginInfo loginInfo = (LoginInfo) CacheDiskUtils.getInstance().getSerializable(Constant.LOGIN_CACHE_KEY);

       if(loginInfo != null)
       {
           return  "1".equals(loginInfo.getData().getIsadmin());
       }
        return false ;
    }
}
