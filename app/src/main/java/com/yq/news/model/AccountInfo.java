package com.yq.news.model;


import java.io.Serializable;

/**
 * 账号
 */
public class AccountInfo implements Serializable {


    /**
     * id : 18
     * nickname : 黄小草擦擦擦
     * creator : 1
     * accounttype : 268435456
     * accountid : 单独单独
     * realname : 实打实
     * createtime : 1577929608
     * updatetime : 1577930150
     */

    private int id;
    private String nickname;
    private int creator;
    private int accounttype;
    private String accountid;
    private String realname;
    private int createtime;
    private int updatetime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public int getAccounttype() {
        return accounttype;
    }

    public void setAccounttype(int accounttype) {
        this.accounttype = accounttype;
    }

    public String getAccountid() {
        return accountid;
    }

    public void setAccountid(String accountid) {
        this.accountid = accountid;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public int getCreatetime() {
        return createtime;
    }

    public void setCreatetime(int createtime) {
        this.createtime = createtime;
    }

    public int getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(int updatetime) {
        this.updatetime = updatetime;
    }
}
