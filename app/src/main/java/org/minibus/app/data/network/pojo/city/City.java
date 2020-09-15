package org.minibus.app.data.network.pojo.city;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;

public class City implements Serializable {

    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("region")
    private String region;

    @SerializedName("station")
    private String station;

    @SerializedName("lat")
    private double latitude;

    @SerializedName("lng")
    private double longitude;

    public City(String id, String name, String region, String station, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.station = station;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public long getLongId() {
        return new BigInteger(id, 16).longValue();
    }

    public String getId() {
        return id;
    }

    public String getRegion() {
        return region;
    }

    public String getStation() {
        return station;
    }

    public String getFullName() {
        return TextUtils.join(", ", new String[]{name, region});
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return id.equals(city.id) && Objects.equals(name, city.name) && Objects.equals(region, city.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, region);
    }
}
