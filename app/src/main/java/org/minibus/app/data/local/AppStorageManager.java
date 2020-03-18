package org.minibus.app.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import org.minibus.app.AppConstants;
import org.minibus.app.data.network.pojo.city.BusStop;
import org.minibus.app.data.network.pojo.user.User;
import org.minibus.app.di.ApplicationContext;
import com.google.gson.Gson;
import org.minibus.app.data.network.pojo.user.UserResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

@Singleton
public class AppStorageManager {

    private static final String KEY_USER_BOOKINGS_COUNT = "key_user_bookings_count";
    private static final String KEY_USER_BOOKINGS_LIMIT = "key_user_bookings_limit";
    private static final String KEY_USER_BOOKINGS_LEFT = "key_user_bookings_left";
    private static final String KEY_USER_AUTH_TOKEN = "key_user_auth_token";
    private static final String KEY_USER_DATA = "key_user_data";
    private static final String KEY_DEPARTURE_BUS_STOP = "key_departure_bus_stop";
    private static final String KEY_DEPARTURE_START_BUS_STOP = "key_departure_start_bus_stop";
    private static final String KEY_ARRIVAL_BUS_STOP = "key_arrival_bus_stop";

    private SharedPreferences storage;

    @Inject
    public AppStorageManager(@ApplicationContext Context context) {
        storage = context.getSharedPreferences(AppConstants.STORAGE_NAME, Context.MODE_PRIVATE);
    }

    public boolean isUserLoggedIn() {
        return contains(KEY_USER_AUTH_TOKEN) && !get(KEY_USER_AUTH_TOKEN, "").isEmpty();
    }

    public boolean isEmpty() {
        return !isUserLoggedIn() && !isDirectionStored();
    }

    public boolean isDirectionStored() {
        return isArrivalBusStopStored() && isDepartureBusStopStored() && contains(KEY_DEPARTURE_START_BUS_STOP);
    }

    public boolean isArrivalBusStopStored() {
        return contains(KEY_ARRIVAL_BUS_STOP);
    }

    public boolean isDepartureBusStopStored() {
        return contains(KEY_DEPARTURE_BUS_STOP);
    }

    public boolean isStoredArrivalCityMatches(String newArrivalCity) {
        return isDirectionStored() && getArrivalBusStop().getDepartureCityName().equals(newArrivalCity);
    }

    public void setDepartureBusStop(BusStop busStop) {
        put(KEY_DEPARTURE_BUS_STOP, busStop);
    }

    public void setArrivalBusStop(BusStop busStop) {
        put(KEY_ARRIVAL_BUS_STOP, busStop);
    }

    public void setDepartureCityStartBusStop(BusStop busStop) {
        put(KEY_DEPARTURE_START_BUS_STOP, busStop);
    }

    public void setDirection(BusStop departureBusStop, BusStop arrivalBusStop) {
        setDepartureBusStop(departureBusStop);
        setArrivalBusStop(arrivalBusStop);
    }

    public void setUserData(UserResponse userResponse) {
        // have to cast to User because we don't need dynamic bookings list in storage
        put(KEY_USER_DATA, (User) userResponse);

        // setUserBookingsLimit(userResponse.getBookingsLimit());
        // TODO forced to replace bookings limit property value from user response with a constant
        //  because of the server bug when just registered user gets limit = 0 until update data
        setUserBookingsLimit(AppConstants.DEFAULT_PASSENGERS_COUNT_PER_BOOKING);
        setUserBookingsCount(userResponse.getBookingsCount());
    }

    public void setUserAuthToken(String token) {
        put(KEY_USER_AUTH_TOKEN, token);
    }

    public void setUserBookingsCount(int count) {
        put(KEY_USER_BOOKINGS_COUNT, count);

        setUserBookingsLeft(getUserBookingsLimit() - count);
    }

    private void setUserBookingsLeft(int left) {
        put(KEY_USER_BOOKINGS_LEFT, left);
    }

    private void setUserBookingsLimit(int limit) {
        put(KEY_USER_BOOKINGS_LIMIT, limit);
    }

    public void setUserSession(String token, UserResponse userResponse) {
        setUserAuthToken(token);
        setUserData(userResponse);
    }

    public String getUserAuthToken() {
        return get(KEY_USER_AUTH_TOKEN, null);
    }

    public int getUserBookingsCount() {
        return get(KEY_USER_BOOKINGS_COUNT, 0);
    }

    public int getUserBookingsLimit() {
        return get(KEY_USER_BOOKINGS_LIMIT, AppConstants.DEFAULT_PASSENGERS_COUNT_PER_BOOKING);
    }

    public int getUserBookingsLeft() {
        return get(KEY_USER_BOOKINGS_LEFT, AppConstants.DEFAULT_PASSENGERS_COUNT_PER_BOOKING);
    }

    public User getUserData() {
        return new Gson().fromJson(get(KEY_USER_DATA, null), User.class);
    }

    public BusStop getArrivalBusStop() {
        return new Gson().fromJson(get(KEY_ARRIVAL_BUS_STOP, null), BusStop.class);
    }

    public BusStop getDepartureBusStop() {
        return new Gson().fromJson(get(KEY_DEPARTURE_BUS_STOP, null), BusStop.class);
    }

    public BusStop getDepartureCityStartBusStop() {
        return new Gson().fromJson(get(KEY_DEPARTURE_START_BUS_STOP, null), BusStop.class);
    }

    public void clearAll() {
        storage.edit().clear().apply();
    }

    public void clearUserSession() {
        delete(KEY_USER_DATA);
        delete(KEY_USER_BOOKINGS_COUNT);
        delete(KEY_USER_BOOKINGS_LIMIT);
        delete(KEY_USER_BOOKINGS_LEFT);
        delete(KEY_USER_AUTH_TOKEN);
    }

    public void clearDirection() {
        delete(KEY_ARRIVAL_BUS_STOP);
        delete(KEY_DEPARTURE_BUS_STOP);
        delete(KEY_DEPARTURE_START_BUS_STOP);
    }
    private boolean contains(String key) {
        return storage.contains(key);
    }

    private void delete(String key) {
        Timber.tag("Storage").d("Delete %s key from store", key);
        storage.edit().remove(key).apply();
    }

    private void put(String key, Object value) {
        String jsonObj = new Gson().toJson(value);
        put(key, jsonObj);
    }

    private void put(String key, String value) {
        Timber.tag("Storage").d("Store %s value with %s key", value, key);
        storage.edit().putString(key, value).apply();
    }

    private void put(String key, int value) {
        Timber.tag("Storage").d("Store %d value with %s key", value, key);
        storage.edit().putInt(key, value).apply();
    }

    private void put(String key, boolean value) {
        Timber.tag("Storage").d("Store %b value with %s key", value, key);
        storage.edit().putBoolean(key, value).apply();
    }

    private String get(String key, String defaultValue) {
        return storage.getString(key, defaultValue);
    }

    private int get(String key, int defaultValue) {
        return storage.getInt(key, defaultValue);
    }

    private boolean get(String key, boolean defaultValue) {
        return storage.getBoolean(key, defaultValue);
    }
}
