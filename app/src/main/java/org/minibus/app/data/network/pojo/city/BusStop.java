package org.minibus.app.data.network.pojo.city;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class BusStop implements Serializable {

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private int id;

    @SerializedName("departureCityName")
    private String departureCityName;

    @SerializedName("arrivalCityName")
    private String arrivalCityName;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    public BusStop(String name,
                   int id,
                   String departureCityName,
                   String arrivalCityName,
                   double latitude,
                   double longitude) {
        this.name = name;
        this.id = id;
        this.departureCityName = departureCityName;
        this.arrivalCityName = arrivalCityName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String stopName) {
        this.name = stopName;
    }

    public int getId() {
        return id;
    }

    public void setId(int stopId) {
        this.id = stopId;
    }

    public String getDepartureCityName() {
        return departureCityName;
    }

    public void setDepartureCityName(String departureCityName) {
        this.departureCityName = departureCityName;
    }

    public String getArrivalCityName() {
        return arrivalCityName;
    }

    public void setArrivalCityName(String arrivalCityName) {
        this.arrivalCityName = arrivalCityName;
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
        BusStop busStop = (BusStop) o;
        return id == busStop.id &&
                Objects.equals(name, busStop.name) &&
                Objects.equals(departureCityName, busStop.departureCityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, departureCityName);
    }

    public String getJoinedCityBusStop() {
        return TextUtils.join(", ", new String[]{name, departureCityName});
    }
}
