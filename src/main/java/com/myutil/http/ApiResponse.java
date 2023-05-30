package com.myutil.http;

/**
 * @author pfzhao
 * @title: ApiResponse
 * @projectName myUtil
 * @description: TODO
 * @date 2023/5/30 11:07
 */
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private int code;
    private String message;
    private String details;

    public static ApiResponse success() {
        return success((Object)null);
    }

    public static ApiResponse success(Object data) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.success = true;
        apiResponse.data = data;
        apiResponse.code = ApiResponseCode.OK.getCode();
        apiResponse.message = ApiResponseCode.OK.getMessage();
        return apiResponse;
    }

    public static ApiResponse fail(String realMessage) {
        return fail(ApiResponseCode.BUSINESS_EXCEPTION.getCode(), realMessage == null ? ApiResponseCode.BUSINESS_EXCEPTION.getMessage() : realMessage);
    }

    public static ApiResponse fail(int code, String message) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.success = false;
        apiResponse.code = code;
        apiResponse.message = message;
        return apiResponse;
    }

    public ApiResponse() {
    }

    public boolean isSuccess() {
        return this.success;
    }

    public T getData() {
        return this.data;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getDetails() {
        return this.details;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ApiResponse)) {
            return false;
        } else {
            ApiResponse<?> other = (ApiResponse)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.isSuccess() != other.isSuccess()) {
                return false;
            } else {
                Object this$data = this.getData();
                Object other$data = other.getData();
                if (this$data == null) {
                    if (other$data != null) {
                        return false;
                    }
                } else if (!this$data.equals(other$data)) {
                    return false;
                }

                if (this.getCode() != other.getCode()) {
                    return false;
                } else {
                    Object this$message = this.getMessage();
                    Object other$message = other.getMessage();
                    if (this$message == null) {
                        if (other$message != null) {
                            return false;
                        }
                    } else if (!this$message.equals(other$message)) {
                        return false;
                    }

                    Object this$details = this.getDetails();
                    Object other$details = other.getDetails();
                    if (this$details == null) {
                        if (other$details != null) {
                            return false;
                        }
                    } else if (!this$details.equals(other$details)) {
                        return false;
                    }

                    return true;
                }
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof ApiResponse;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1 * 59 + (this.isSuccess() ? 79 : 97);
        Object $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        result = result * 59 + this.getCode();
        Object $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        Object $details = this.getDetails();
        result = result * 59 + ($details == null ? 43 : $details.hashCode());
        return result;
    }

    public String toString() {
        return "ApiResponse(success=" + this.isSuccess() + ", data=" + this.getData() + ", code=" + this.getCode() + ", message=" + this.getMessage() + ", details=" + this.getDetails() + ")";
    }
}
