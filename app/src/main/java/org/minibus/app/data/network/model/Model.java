package org.minibus.app.data.network.model;

import org.minibus.app.data.network.AppApiClient;

import javax.inject.Inject;

public class Model {

    private AppApiClient appApiClient;

    @Inject
    public Model(AppApiClient appApiClient) {
        this.appApiClient = appApiClient;
    }

    protected AppApiClient getClient() {
        return appApiClient;
    }
}
