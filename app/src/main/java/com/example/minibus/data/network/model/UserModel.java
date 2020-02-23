package com.example.minibus.data.network.model;

import com.example.minibus.data.network.AppApiClient;
import com.example.minibus.data.network.pojo.user.UserRequest;
import com.example.minibus.data.network.pojo.user.UserResponse;

import io.reactivex.Single;

public class UserModel {

    public Single<UserResponse> doGetUserData(String authToken) {
        return AppApiClient.getApiService().getUserData("Basic ".concat(authToken));
    }

    public Single<UserResponse> doPostUserData(UserRequest newUser) {
        return AppApiClient.getApiService().postUserData(newUser);
    }
}
