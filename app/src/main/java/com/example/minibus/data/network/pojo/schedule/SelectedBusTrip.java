package com.example.minibus.data.network.pojo.schedule;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectedBusTrip {

    @SerializedName("id")
    private int id;

    @SerializedName("departureTime")
    private String departureTime;

    @SerializedName("arrivalTime")
    private String arrivalTime;

    public SelectedBusTrip(int id,
                           String departureTime,
                           String arrivalTime) {
        this.id = id;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartureTime() {
        return getTrimmedTime(departureTime);
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return getTrimmedTime(arrivalTime);
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SelectedBusTrip SelectedBusTrip = (SelectedBusTrip) o;
        return id == SelectedBusTrip.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    protected String getTrimmedTime(String time) {
        Pattern pattern = Pattern.compile("(:0{2}$)");
        Matcher matcher = pattern.matcher(time);
        if (matcher.find()) return time.substring(0, time.length() - matcher.group(0).length());
        else return time;
    }
}
