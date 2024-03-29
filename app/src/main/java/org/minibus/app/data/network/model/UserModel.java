package org.minibus.app.data.network.model;

import org.minibus.app.data.network.AppApiClient;
import org.minibus.app.data.network.pojo.user.UserResponse;

import java.util.HashMap;

import io.reactivex.Single;

public class UserModel extends Model {

    public UserModel(AppApiClient appApiClient) {
        super(appApiClient);
    }

    public Single<UserResponse> doGetUserData(String authToken, boolean withBookingsHistory) {
        return getClient().getApiService().getUserData("Bearer ".concat(authToken), withBookingsHistory);
    }

    public Single<UserResponse> doAuthUserData(String name, String phone, String password) {
        return getClient().getApiService().authUserData(getUserRequestBody(name, phone, password));
    }

    public Single<UserResponse> doCreateUserData(String name, String phone, String password) {
        return getClient().getApiService().createUserData(getUserRequestBody(name, phone, password));
    }

    public Single<UserResponse> doDeleteUserBookingData(String authToken, String bookingId) {
        return getClient().getApiService().deleteBookingData("Bearer ".concat(authToken), bookingId);
    }

    private HashMap<String, Object> getUserRequestBody(String name, String phone, String password) {
        HashMap<String, Object> reqBody = new HashMap<>();
        reqBody.put("name", name);
        reqBody.put("phone", phone);
        reqBody.put("password", password);
        return reqBody;
    }
}
