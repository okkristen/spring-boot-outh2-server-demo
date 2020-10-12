package com.hzys.ssoserver.exception;


import com.hzys.ssoserver.restResult.ResultCode;

/**
 * 自定义异常
 */
public class CustomException extends RuntimeException {

    private Integer code;

    private ResultCode resultCode;

    public CustomException(){
    }

    public CustomException(ResultCode resultCode){
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.resultCode = resultCode;
    }
    

    public CustomException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }
}
