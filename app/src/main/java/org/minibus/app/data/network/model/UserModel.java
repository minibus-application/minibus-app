package org.minibus.app.data.network.model;

import org.minibus.app.data.network.AppApiClient;
import org.minibus.app.data.network.pojo.user.UserRequest;
import org.minibus.app.data.network.pojo.user.UserResponse;

import io.reactivex.Single;

public class UserModel extends BaseModel {

    public UserModel(AppApiClient appApiClient) {
        super(appApiClient);
    }

    public Single<UserResponse> doGetUserData(String authToken) {
        return getClient().getApiService().getUserData("Basic ".concat(authToken));
    }

    public Single<UserResponse> doPostUserData(UserRequest newUser) {
        return getClient().getApiService().postUserData(newUser);
    }
}
