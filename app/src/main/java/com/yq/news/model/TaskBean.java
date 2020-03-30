package com.yq.news.model;


import java.io.Serializable;
import java.util.List;

/**
 * 任务Bean
 */
public class TaskBean implements Serializable {


    /**
     * id : 25
     * name : 蔡敏测试
     * description : 士大夫撒旦
     * creator : 1
     * recordCount : null
     * userCount : null
     * finished : null
     * feededcount : 0
     * feedtotal : 200
     * starttime : 1578240000
     * endtime : 1581695999
     * pilotcount : 40
     * createtime : 1578273091
     * username : admin
     */

    private int id;
    private String name;
    private String description;
    private int creator;

    public List<TaskInfoUser> getTaskInfoUsers() {
        return taskInfoUsers;
    }

    public void setTaskInfoUsers(List<TaskInfoUser> taskInfoUsers) {
        this.taskInfoUsers = taskInfoUsers;
    }

    private List<TaskInfoUser> taskInfoUsers;

    public int getRecordcount() {
        return recordcount;
    }

    public void setRecordcount(int recordcount) {
        this.recordcount = recordcount;
    }

    private int recordcount;
    private Object userCount;


    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    private int finished;
    private int feededcount;
    private int feedtotal;
    private int starttime;
    private int endtime;
    /**
     * 指标量
     */
    private int pilotcount;
    private int createtime;
    private String username;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type ;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }


    public Object getUserCount() {
        return userCount;
    }

    public void setUserCount(Object userCount) {
        this.userCount = userCount;
    }


    public int getFeededcount() {
        return feededcount;
    }

    public void setFeededcount(int feededcount) {
        this.feededcount = feededcount;
    }

    public int getFeedtotal() {
        return feedtotal;
    }

    public void setFeedtotal(int feedtotal) {
        this.feedtotal = feedtotal;
    }

    public int getStarttime() {
        return starttime;
    }

    public void setStarttime(int starttime) {
        this.starttime = starttime;
    }

    public int getEndtime() {
        return endtime;
    }

    public void setEndtime(int endtime) {
        this.endtime = endtime;
    }

    public int getPilotcount() {
        return pilotcount;
    }

    public void setPilotcount(int pilotcount) {
        this.pilotcount = pilotcount;
    }

    public int getCreatetime() {
        return createtime;
    }

    public void setCreatetime(int createtime) {
        this.createtime = createtime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
