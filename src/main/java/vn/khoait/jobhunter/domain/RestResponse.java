package vn.khoait.jobhunter.domain;

public class RestResponse<T> {
    private int statusCode;
    private T data;
    private Object message;
    private String error;
    public int getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
    public Object getMessage() {
        return message;
    }
    public void setMessage(Object message) {
        this.message = message;
    }
    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }
    
}
