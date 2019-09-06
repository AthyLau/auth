package com.bizseer.auth.util.http;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

/**
 * Created by chengshiwen on 2017/9/23.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
@Setter
public class JsonResponse {

    private int code;
    private String msg;
    private Object data;
    public static final JsonResponse SUCCESS = new JsonResponse();
    public static final JsonResponse BAD_REQUEST = new JsonResponse(HttpStatus.BAD_REQUEST);
    public static final JsonResponse NOT_FOUND = new JsonResponse(HttpStatus.NOT_FOUND);
    public static final JsonResponse UNAUTHORIZED = new JsonResponse(HttpStatus.UNAUTHORIZED);
    public static final JsonResponse NO_CONTENT = new JsonResponse(HttpStatus.NO_CONTENT);
    private static final String PERSIST_ERROR_MESSAGE = "persistError";

    public JsonResponse() {
        this.code = HttpStatus.OK.value();
        this.msg = HttpStatus.OK.getReasonPhrase();
    }

    public JsonResponse(Object data) {
        this.code = HttpStatus.OK.value();
        this.msg = HttpStatus.OK.getReasonPhrase();
        this.data = data;
    }

    public JsonResponse(HttpStatus httpStatus) {
        this.code = httpStatus.value();
        this.msg = httpStatus.getReasonPhrase();
    }

    public JsonResponse(HttpStatus httpStatus, Object data) {
        this.code = httpStatus.value();
        this.msg = httpStatus.getReasonPhrase();
        this.data = data;
    }

    public JsonResponse(HttpStatus httpStatus, String msg) {
        this.code = httpStatus.value();
        this.msg = msg;
    }

    public JsonResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public JsonResponse(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static JsonResponse success() {
        return SUCCESS;
    }

    public static JsonResponse success(Object object) {
        return new JsonResponse(HttpStatus.OK, object);
    }

    public static JsonResponse accepted(Object object) {
        return new JsonResponse(HttpStatus.ACCEPTED, object);
    }

    public static JsonResponse error(HttpStatus httpStatus) {
        return new JsonResponse(httpStatus);
    }

    public static JsonResponse persistError() {
        return new JsonResponse(200, PERSIST_ERROR_MESSAGE);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }
}
