package nwpu.deviceonenet.onenet.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

// 统一结果返回类
@Data
@AllArgsConstructor
public class R {

    private Integer code;

    private String message;

    private Object data;

    public static R ok(Object data, String message) {
        return new R(200, message, data);
    }

    public static R ok(Object data) {
        return R.ok(data, "success");
    }

    public static R error( Integer code, String message) {
        return new R(code, message, "");
    }
}
