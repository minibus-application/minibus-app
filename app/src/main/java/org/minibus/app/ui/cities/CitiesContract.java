package org.minibus.app.ui.cities;

import org.minibus.app.data.network.pojo.city.CityResponse;
import org.minibus.app.data.network.pojo.city.City;
import org.minibus.app.ui.base.Contract;

import java.util.List;

public interface CitiesContract {

    interface View extends Contract.View {

        void setCitiesData(List<City> cities);
        void changeCity(City city);
        void close();
    }

    interface Presenter<V extends CitiesContract.View> extends Contract.Presenter<V> {

        void onStart();
        void onCloseButtonClick();
        void onCitySelect(City city);
    }
}
