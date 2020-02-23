package com.example.minibus.data.network.pojo.city;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.stream.Collectors;

public class CityResponse implements Comparable<CityResponse> {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("stops")
    private List<BusStop> busStops;

    public CityResponse(int id, String name, List<BusStop> busStops) {
        this.id = id;
        this.name = name;
        this.busStops = busStops;
    }

    public List<BusStop> getBusStops() {
        return busStops;
    }

    public void setBusStops(List<BusStop> busStops) {
        this.busStops = busStops;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(CityResponse city) {
        return this.id - city.getId();
    }

    public boolean isBusStopsListEmpty() {
        return getBusStopsCount() == 0;
    }

    public int getBusStopsCount() {
        return busStops == null ? 0 : busStops.size();
    }

    public List<Integer> getBusStopsIds() {
        return getBusStops().stream().map(BusStop::getId).collect(Collectors.toList());
    }

    public BusStop getFinalBusStop() {
        return busStops != null ? busStops.get(busStops.size() - 1) : null;
    }

    public BusStop getStartBusStop() {
        return busStops != null ? busStops.get(0) : null;
    }
}
