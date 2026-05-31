package com;

import java.util.HashMap;

import lombok.Data;

@Data
public class QueryPageParam {
    private String storage;
    private String goodsType;
// 记得加上getter、setter
    private int pageNum=0;
    private int pageSize=10;
    private String name;
    private Integer sex;
    private int No;
    private HashMap param = new HashMap();
    private Object roleId; // 加这一行
    private Integer userId; // 加这一行

    // 生成 getter 和 setter
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public String getName() {
        return name;
    }
     public Integer getSex() {
        return sex;
    }
    public void setSex(Integer sex) {
        this.sex = sex;
    }
    public void setName(String Names) {
        this.name = Names;
    }
    public int getNo() {
        return No;
    }
    public Object getRoleId() {
        return roleId; 
    }
    public void setRoleId(Object roleId) { 
        this.roleId = roleId; 
    }
    public void setNo(int No) {
        this.No = No;
    }
}