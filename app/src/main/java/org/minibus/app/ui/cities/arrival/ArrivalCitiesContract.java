package org.minibus.app.ui.cities.arrival;

import org.minibus.app.data.network.pojo.city.City;
import org.minibus.app.ui.cities.CitiesContract;
import org.minibus.app.ui.cities.departure.DepartureCitiesContract;

import java.util.List;

public interface ArrivalCitiesContract {

    interface View extends CitiesContract.View {
        void setCitiesData(List<City> cities, long prevSelectedCityId);
    }

    interface Presenter<V extends ArrivalCitiesContract.View> extends CitiesContract.Presenter<V> {
        void onStart();
    }
}
