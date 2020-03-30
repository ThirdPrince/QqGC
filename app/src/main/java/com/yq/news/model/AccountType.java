package com.yq.news.model;

/**
 * 微博 微信
 */
public class AccountType {


    /**
     * id : 1048576
     * name : 微博
     * enName : MICROBLOG
     * remark : null
     * domainType : 0
     */

    private int id;
    private String name;
    private String enName;
    private Object remark;
    private int domainType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
    }

    public int getDomainType() {
        return domainType;
    }

    public void setDomainType(int domainType) {
        this.domainType = domainType;
    }
}
