package com.lanai.weather.Utils;

// 用于返回带有状态码,消息和数据的json数据

public class ResData {
    public static final String OK_MSG = "操作成功！";
    public static final String ERR_MSG = "后台报错！";

    private int status; // 状态码
    private String msg; // 返回的消息
    private Object data; // 返回的数据

    public ResData() {
    }

    public ResData(Object data) {
        this.data = data;
    }

    public ResData(String msg) {
        this.msg = msg;
    }

    public ResData(int status) {
        this.status = status;
    }

//    public ResData(int status, String msg) {
//        this.status = status;
//        this.msg = msg;
//    }

    public ResData(int status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    // 成功ok 200
    public static ResData ok() {
        return new ResData(200,"请求成功",null);
    }

    // 成功ok 200 返回数据
    public static ResData ok(Object data) {
        return new ResData(200,"请求成功",data);
    }

    // 404
    public static ResData notFound() {
        return new ResData(404,"错误的请求路径",null);
    }

    // 400
    public static ResData err() {
        return new ResData(400,"请求参数错误",null);
    }
    public static ResData err(String msg) {
        return new ResData(400,msg,null);
    }

    // 500
    public static ResData serverErr() {
        return new ResData(500,"服务器出错",null);
    }

    // 自定义
    public static ResData customRes(int status,String msg, Object data) {
        return new ResData(status,msg,data);
    }

    //无需返回值(整形结果)
    public static ResData resultInt(Integer i){
        if (i>0) return ResData.ok();
        else return ResData.err();
    }
    //需要返回值(整形结果)
    public static ResData resultInt(Integer i,String okData,String errMsg){
        if (i>0) return ResData.ok(okData);
        else return ResData.err(errMsg);
    }
    //无需返回结果(对象结果)
    public static ResData resultObject(Object object){
        if (object!=null) return ResData.ok(object);
        else return ResData.err();
    }
    //需要返回结果(对象结果)
    public static ResData resultObject(Object object,String okData,String errMsg){
        if (object != null) return ResData.ok(okData);
        else return ResData.err(errMsg);
    }
}
