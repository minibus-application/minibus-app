package org.minibus.app.data.network.pojo.user;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.Objects;

public class User {

    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("phone")
    private String phone;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("enRouteBookingsCount")
    private int enRouteBookingsCount;

    @SerializedName("activeBookingsCount")
    private int activeBookingsCount;

    @SerializedName("totalBookingsCount")
    private int totalBookingsCount;

    public User(String id,
                String name,
                String phone,
                String createdAt,
                int enRouteBookingsCount,
                int activeBookingsCount,
                int totalBookingsCount) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.createdAt = createdAt;
        this.enRouteBookingsCount = enRouteBookingsCount;
        this.activeBookingsCount = activeBookingsCount;
        this.totalBookingsCount = totalBookingsCount;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public int getEnRouteBookingsCount() {
        return enRouteBookingsCount;
    }

    public int getActiveBookingsCount() {
        return activeBookingsCount;
    }

    public int getTotalBookingsCount() {
        return totalBookingsCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) &&
                Objects.equals(phone, user.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, phone);
    }
}
