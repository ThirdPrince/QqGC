package com.yq.news.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择单位 bean
 * 树形结构
 */
public class DepartmentBean {


    /**
     * id : 1
     * name : 测试部门
     * pid : 0
     */

    private int id;
    private String name;

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    private int pId;
    protected List<DepartmentBean> children = new ArrayList<>();


    public List<DepartmentBean> getChildren() {
        return children;
    }

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



    public void add(DepartmentBean bean) {
        children.add(bean);
    }

}
