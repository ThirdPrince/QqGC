package com.yq.news.model;

import java.io.Serializable;

/**
 * Task 里的UserList
 */
public class TaskInfoUser implements Serializable {


    /**
     * id : 48
     * account : test2
     * telphone : 12313123312
     * email :
     * state : 1
     * remark :
     * areacode : 320000
     * regid : NjnbrrlBQKXQVx4KNokfbJCZpUmZQb83NIxMRYe96N8umdcwk8MPfXkluktY1Ibo
     * isadmin : null
     * deptid : 2
     * deptname : null
     * roleid : 2
     * usertype : 1
     * rolename : null
     * realname : Test2
     * applytime : 1577252629
     * equipmentcount : 1
     * equipmentlist : null
     * industryid : 300000
     * industryname : null
     * areaname : null
     * mobiletype : xiaomi
     * recordcount : 0
     */

    private int id;
    private String account;
    private String telphone;
    private String email;
    private int state;
    private String remark;
    private int areacode;
    private String regid;
    private Object isadmin;
    private int deptid;
    private Object deptname;
    private int roleid;
    private int usertype;
    private Object rolename;
    private String realname;
    private int applytime;
    private int equipmentcount;
    private Object equipmentlist;
    private int industryid;
    private Object industryname;
    private Object areaname;
    private String mobiletype;
    private int recordcount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getAreacode() {
        return areacode;
    }

    public void setAreacode(int areacode) {
        this.areacode = areacode;
    }

    public String getRegid() {
        return regid;
    }

    public void setRegid(String regid) {
        this.regid = regid;
    }

    public Object getIsadmin() {
        return isadmin;
    }

    public void setIsadmin(Object isadmin) {
        this.isadmin = isadmin;
    }

    public int getDeptid() {
        return deptid;
    }

    public void setDeptid(int deptid) {
        this.deptid = deptid;
    }

    public Object getDeptname() {
        return deptname;
    }

    public void setDeptname(Object deptname) {
        this.deptname = deptname;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public int getUsertype() {
        return usertype;
    }

    public void setUsertype(int usertype) {
        this.usertype = usertype;
    }

    public Object getRolename() {
        return rolename;
    }

    public void setRolename(Object rolename) {
        this.rolename = rolename;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public int getApplytime() {
        return applytime;
    }

    public void setApplytime(int applytime) {
        this.applytime = applytime;
    }

    public int getEquipmentcount() {
        return equipmentcount;
    }

    public void setEquipmentcount(int equipmentcount) {
        this.equipmentcount = equipmentcount;
    }

    public Object getEquipmentlist() {
        return equipmentlist;
    }

    public void setEquipmentlist(Object equipmentlist) {
        this.equipmentlist = equipmentlist;
    }

    public int getIndustryid() {
        return industryid;
    }

    public void setIndustryid(int industryid) {
        this.industryid = industryid;
    }

    public Object getIndustryname() {
        return industryname;
    }

    public void setIndustryname(Object industryname) {
        this.industryname = industryname;
    }

    public Object getAreaname() {
        return areaname;
    }

    public void setAreaname(Object areaname) {
        this.areaname = areaname;
    }

    public String getMobiletype() {
        return mobiletype;
    }

    public void setMobiletype(String mobiletype) {
        this.mobiletype = mobiletype;
    }

    public int getRecordcount() {
        return recordcount;
    }

    public void setRecordcount(int recordcount) {
        this.recordcount = recordcount;
    }
}
