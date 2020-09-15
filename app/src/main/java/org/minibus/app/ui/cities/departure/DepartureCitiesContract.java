package org.minibus.app.ui.cities.departure;

import org.minibus.app.data.network.pojo.city.City;
import org.minibus.app.ui.cities.BaseCitiesContract;

import java.util.List;

public interface DepartureCitiesContract {

    interface View extends BaseCitiesContract.View {
        void setCitiesData(List<City> cities, long prevSelectedCityId);
    }

    interface Presenter<V extends DepartureCitiesContract.View> extends BaseCitiesContract.Presenter<V> {
        void onStart();
    }
}
