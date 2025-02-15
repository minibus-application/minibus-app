package org.minibus.app.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import org.minibus.app.AppConstants;
import org.minibus.app.data.network.pojo.city.City;
import org.minibus.app.data.network.pojo.route.Route;
import org.minibus.app.data.network.pojo.user.User;
import org.minibus.app.di.ApplicationContext;

import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

@Singleton
public class AppStorageManager {

    private static final String KEY_USER_AUTH_TOKEN = "key_user_auth_token";
    private static final String KEY_USER_DATA = "key_user_data";
    private static final String KEY_ROUTE = "key_route";
    private static final String KEY_DEPARTURE_CITY = "key_departure_city";
    private static final String KEY_ARRIVAL_CITY = "key_arrival_city";

    private SharedPreferences storage;

    @Inject
    public AppStorageManager(@ApplicationContext Context context) {
        storage = context.getSharedPreferences(AppConstants.STORAGE_NAME, Context.MODE_PRIVATE);
    }

    public boolean isAuthorised() {
        return contains(KEY_USER_AUTH_TOKEN) && !get(KEY_USER_AUTH_TOKEN, "").isEmpty();
    }

    public boolean isRouteStored() {
        return contains(KEY_ROUTE);
    }

    public boolean isArrivalCityStored() {
        return contains(KEY_ARRIVAL_CITY);
    }

    public boolean isDepartureCityStored() {
        return contains(KEY_DEPARTURE_CITY);
    }

    public void setRoute(Route route) {
        setDepartureCity(route.getDepartureCity());
        setArrivalCity(route.getArrivalCity());
        put(KEY_ROUTE, route);
    }

    public void setDepartureCity(City city) {
        put(KEY_DEPARTURE_CITY, city);
    }

    public void setArrivalCity(City city) {
        put(KEY_ARRIVAL_CITY, city);
    }

    public void setUserData(User user) {
        put(KEY_USER_DATA, user);
    }

    public void setAuthToken(String token) {
        put(KEY_USER_AUTH_TOKEN, token);
    }

    public void setUserSession(String token, User user) {
        setAuthToken(token);
        setUserData(user);
    }

    public String getAuthToken() {
        return get(KEY_USER_AUTH_TOKEN, null);
    }

    public User getUserData() {
        return new Gson().fromJson(get(KEY_USER_DATA, null), User.class);
    }

    public Route getRoute() {
        return new Gson().fromJson(get(KEY_ROUTE, null), Route.class);
    }

    public City getArrivalCity() {
        return new Gson().fromJson(get(KEY_ARRIVAL_CITY, null), City.class);
    }

    public City getDepartureCity() {
        return new Gson().fromJson(get(KEY_DEPARTURE_CITY, null), City.class);
    }

    public void clearAll() {
        storage.edit().clear().apply();
    }

    public void clearUserSession() {
        delete(KEY_USER_DATA);
        delete(KEY_USER_AUTH_TOKEN);
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
