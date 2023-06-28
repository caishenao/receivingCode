package cn.cai.receivingCode.commont;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * 定义基础的返回值
 */
@Data
public class Result<T>{
    private Integer code;
    private String msg;
    private T data;

    public Result() {
    }

    public static<T> Result<T> success(HttpStatus code, String msg, T data) {
        Result<T> result = new Result<>();
        result.code = code.value();
        result.msg = msg;
        result.data = data;
        return result;
    }

    public static<T> Result<T> success() {
        Result<T> result = new Result<>();
        result.code = HttpStatus.OK.value();
        result.msg = "相应成功!";
        return result;
    }

    public static<T> Result<T> success(String msg, T data) {
        Result<T> result = new Result<>();
        result.code = HttpStatus.OK.value();
        result.msg = msg;
        result.data = data;
        return result;
    }

    public static<T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.code = HttpStatus.OK.value();
        result.msg = "请求成功!";
        result.data = data;
        return result;
    }

    public static<T> Result<T> error(HttpStatus code, String msg, T data) {
        Result<T> result = new Result<>();
        result.code = code.value();
        result.msg = msg;
        result.data = data;
        return result;
    }

    public static<T> Result<T> error(String msg, T data) {
        Result<T> result = new Result<>();
        result.code = HttpStatus.NOT_FOUND.value();
        result.msg = msg;
        result.data = data;
        return result;
    }

    public static<T> Result<T> error(T data) {
        Result<T> result = new Result<>();
        result.code = HttpStatus.NOT_FOUND.value();
        result.msg = "请求失败!";
        result.data = data;
        return result;
    }

}
