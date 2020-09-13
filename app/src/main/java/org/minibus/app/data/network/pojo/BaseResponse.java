package org.minibus.app.data.network.pojo;

import com.google.gson.annotations.SerializedName;

public class BaseResponse<T> {

    @SerializedName("success")
    private boolean isSucceeded;

    @SerializedName("result")
    private T result;

    public BaseResponse(T result) {
        this.result = result;
    }

    public T getResult() {
        return result;
    }

    public boolean isSucceeded() {
        return isSucceeded;
    }
}
