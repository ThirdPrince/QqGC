package com.yq.news.model;

public class MsgInfo {


    /**
     * id : 622
     * type : 0
     * content : 新增【郭德纲大概】任务
     * isread : 0
     * receiveid : 48
     * createtime : 1578449376
     * relateid : 85
     * remark : null
     */

    private int id;
    private int type;
    private String content;
    private int isread;
    private int receiveid;
    private int createtime;
    private int relateid;
    private Object remark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsread() {
        return isread;
    }

    public void setIsread(int isread) {
        this.isread = isread;
    }

    public int getReceiveid() {
        return receiveid;
    }

    public void setReceiveid(int receiveid) {
        this.receiveid = receiveid;
    }

    public int getCreatetime() {
        return createtime;
    }

    public void setCreatetime(int createtime) {
        this.createtime = createtime;
    }

    public int getRelateid() {
        return relateid;
    }

    public void setRelateid(int relateid) {
        this.relateid = relateid;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
    }
}
