package com;

import lombok.Data;

@Data
public class Result {
    private int code;//编码
    private String msg;//成功/失败
    private Long total;//总记录数
    private Object data;//数据

    public static Result fail(String msg) {
        return result(0, "失败", 0L, null);
    }
    public static Result success(String msg, Object data) {
        return result(1, "成功", 0L, data);
    }
    public static Result success(String msg, Long total, Object data) {
        return result(1, "成功", total, data);
    }

    public static Result result(int code, String msg, Long total, Object data) {
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setTotal(total);
        r.setData(data);
        return r;
    };
}
