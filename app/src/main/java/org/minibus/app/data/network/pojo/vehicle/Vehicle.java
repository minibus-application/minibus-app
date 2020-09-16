package org.minibus.app.data.network.pojo.vehicle;

import com.google.gson.annotations.SerializedName;

import org.minibus.app.data.network.pojo.carrier.Carrier;

import java.io.Serializable;
import java.math.BigInteger;

public class Vehicle implements Serializable {

    @SerializedName("_id")
    private String id;

    @SerializedName("color")
    private String color;

    @SerializedName("make")
    private String make;

    @SerializedName("model")
    private String model;

    @SerializedName("capacity")
    private int capacity;

    @SerializedName("plateNum")
    private String plateNumber;

    @SerializedName("carrier")
    private Carrier carrier;

    public Vehicle(String id,
                   String color,
                   String make,
                   String model,
                   int capacity,
                   String plateNumber,
                   Carrier carrier) {
        this.id = id;
        this.color = color;
        this.make = make;
        this.model = model;
        this.capacity = capacity;
        this.plateNumber = plateNumber;
        this.carrier = carrier;
    }

    public long getId() {
        return new BigInteger(id, 16).longValue();
    }

    public String getColor() {
        return color;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public Carrier getCarrier() {
        return carrier;
    }
}
