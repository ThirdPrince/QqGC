package com.yq.news.model;


import java.io.Serializable;

/**
 * 已反馈信息
 */
public class FeedBackInfo implements Serializable {


    /**
     * id : 8
     * state : null
     * uid : null
     * deptName : null
     * accountid : 45
     * imgurl : 5573_20200108_093207268_1275.jpg
     * taskid : 76
     * userid : 70
     * createtime : 1578447127
     * updatetime : null
     * username : null
     * accounttype : null
     * accountname : null
     */

    private int id;
    private Object state;
    private Object uid;
    private Object deptName;
    private int accountid;

    public String getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(String userAccountId) {
        this.userAccountId = userAccountId;
    }

    /**
     * 未反馈 查询 Account
     */
    private String userAccountId;
    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    private String imgPath;
    private String imgurl;
    private int taskid;
    private int userid;
    private int createtime;
    private Object updatetime;
    private String username;
    private int accounttype;
    private String accountname;

    public String getAccountTypeName() {
        return accountTypeName;
    }

    public void setAccountTypeName(String accountTypeName) {
        this.accountTypeName = accountTypeName;
    }

    private String accountTypeName ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getState() {
        return state;
    }

    public void setState(Object state) {
        this.state = state;
    }

    public Object getUid() {
        return uid;
    }

    public void setUid(Object uid) {
        this.uid = uid;
    }

    public Object getDeptName() {
        return deptName;
    }

    public void setDeptName(Object deptName) {
        this.deptName = deptName;
    }

    public int getAccountid() {
        return accountid;
    }

    public void setAccountid(int accountid) {
        this.accountid = accountid;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getCreatetime() {
        return createtime;
    }

    public void setCreatetime(int createtime) {
        this.createtime = createtime;
    }

    public Object getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Object updatetime) {
        this.updatetime = updatetime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAccounttype() {
        return accounttype;
    }

    public void setAccounttype(int accounttype) {
        this.accounttype = accounttype;
    }

    public String  getAccountname() {
        return accountname;
    }

    public void setAccountname(String  accountname) {
        this.accountname = accountname;
    }
}
