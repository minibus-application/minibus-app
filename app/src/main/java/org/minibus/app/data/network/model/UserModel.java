package org.minibus.app.data.network.model;

import org.minibus.app.data.network.AppApiClient;
import org.minibus.app.data.network.pojo.user.UserRequest;
import org.minibus.app.data.network.pojo.user.UserResponse;

import io.reactivex.Single;

public class UserModel {

    public Single<UserResponse> doGetUserData(String authToken) {
        return AppApiClient.getApiService().getUserData("Basic ".concat(authToken));
    }

    public Single<UserResponse> doPostUserData(UserRequest newUser) {
        return AppApiClient.getApiService().postUserData(newUser);
    }
}
