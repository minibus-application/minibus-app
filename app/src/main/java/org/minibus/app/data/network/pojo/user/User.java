package org.minibus.app.data.network.pojo.user;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;

public class User {

    @SerializedName("_id")
    private String id;

    @SerializedName("driver")
    private boolean isDriver;

    @SerializedName("name")
    private String name;

    @SerializedName("phone")
    private String phone;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    public User(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public long getLongId() {
        return new BigInteger(id, 16).longValue();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isDriver() {
        return isDriver;
    }
}
