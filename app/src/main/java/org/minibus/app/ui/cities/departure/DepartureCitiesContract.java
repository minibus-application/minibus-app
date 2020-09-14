package org.minibus.app.ui.cities.departure;

import org.minibus.app.data.network.pojo.city.City;
import org.minibus.app.ui.cities.CitiesContract;

import java.util.List;

public interface DepartureCitiesContract {

    interface View extends CitiesContract.View {
        void setCitiesData(List<City> cities, long prevSelectedCityId);
    }

    interface Presenter<V extends DepartureCitiesContract.View> extends CitiesContract.Presenter<V> {
        void onStart();
    }
}
