package org.minibus.app.data.network.model;

import org.minibus.app.data.network.AppApiClient;

import javax.inject.Inject;

public class BaseModel {

    private AppApiClient appApiClient;

    @Inject
    public BaseModel(AppApiClient appApiClient) {
        this.appApiClient = appApiClient;
    }

    protected AppApiClient getClient() {
        return appApiClient;
    }
}
