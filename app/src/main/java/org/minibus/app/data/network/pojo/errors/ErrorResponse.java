package org.minibus.app.data.network.pojo.errors;

import com.google.gson.annotations.SerializedName;

public class ErrorResponse {

    @SerializedName("timestamp")
    private String timestamp;

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    public ErrorResponse(String timestamp,
                         int status,
                         String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
