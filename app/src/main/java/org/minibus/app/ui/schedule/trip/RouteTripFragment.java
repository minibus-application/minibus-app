package org.minibus.app.ui.schedule.trip;

import android.app.Dialog;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.minibus.app.AppConstants;
import org.minibus.app.data.network.pojo.route.Route;
import org.minibus.app.data.network.pojo.schedule.RouteTrip;
import org.minibus.app.ui.base.BaseSheetDialogFragment;
import org.minibus.app.ui.custom.CounterLayout;
import org.minibus.app.ui.R;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;


public class RouteTripFragment extends BaseSheetDialogFragment implements RouteTripContract.View {

    public static final int REQ_CODE = AppConstants.ROUTE_TRIP_FRAGMENT_REQ_CODE;

    @BindView(R.id.btn_confirm_reservation) MaterialButton btnConfirmReservation;
    @BindView(R.id.tv_summary_title) TextView textTitle;
    @BindView(R.id.tv_summary_cost) TextView textCost;
    @BindView(R.id.tv_trip_travel_time) TextView textTravelTime;
    @BindView(R.id.tv_trip_route) TextView textRoute;
    @BindView(R.id.tv_trip_dep_station) TextView textDepartureStation;
    @BindView(R.id.tv_trip_arr_station) TextView textArrivalStation;
    @BindView(R.id.tv_trip_vehicle) TextView textVehicle;
    @BindView(R.id.tv_trip_vehicle_color) TextView textVehicleColor;
    @BindView(R.id.tv_trip_vehicle_plate_num) TextView textVehiclePlateNumber;
    @BindView(R.id.tv_trip_passenger_info) TextView textPassengerInfo;
    @BindView(R.id.cl_trip_seats) CounterLayout counterSeats;

    @Inject
    RouteTripPresenter<RouteTripContract.View> presenter;

    public interface OnRouteTripBookingListener {
        void onRouteTripBooked();
    }

    public static final String ROUTE_KEY = "key_route";
    public static final String ROUTE_TRIP_KEY = "key_route_trip";
    public static final String DEPARTURE_DATE_KEY = "key_departure_date";

    private OnRouteTripBookingListener listener;
    private RouteTrip routeTrip;
    private Route route;
    private String departureDate;
    private int availableSeatsCount;

    public static RouteTripFragment newInstance() {
        return new RouteTripFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            routeTrip = (RouteTrip) getArguments().getSerializable(ROUTE_TRIP_KEY);
            route = (Route) getArguments().getSerializable(ROUTE_KEY);
            departureDate = getArguments().getString(DEPARTURE_DATE_KEY);
            availableSeatsCount = routeTrip.getAvailableSeats();
        } else {
            dismiss();
        }

        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_BottomSheetDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getMainActivity().getLayoutInflater().inflate(R.layout.fragment_route_trip_summary, null);

        listener = (OnRouteTripBookingListener) getTargetFragment();
        setUnbinder(ButterKnife.bind(this, view));
        getActivityComponent().inject(this);
        presenter.attachView(this);

        textTitle.setText(departureDate);
        textCost.setText(String.format("%s %s", routeTrip.getPrice(), routeTrip.getCurrency()));
        textTravelTime.setText(String.format("%s - %s (%s)", routeTrip.getDepartureTime(), routeTrip.getArrivalTime(), routeTrip.getDuration()));
        textRoute.setText(route.getDescription());
        textDepartureStation.setText(route.getDepartureCity().getStation());
        textArrivalStation.setText(route.getArrivalCity().getStation());
        textVehicleColor.setText(Html.fromHtml("\u2B24"));
        textVehicleColor.setTextColor(Color.parseColor(routeTrip.getVehicle().getColor()));
        textVehicle.setText(String.format("%s %s", routeTrip.getVehicle().getMake(), routeTrip.getVehicle().getModel()));
        textVehiclePlateNumber.setText(routeTrip.getVehicle().getPlateNumber());

        counterSeats.setOnChangedValueListener(value -> {
            try {
                float cost = Float.parseFloat(routeTrip.getPrice());
                String newCost = String.format("%s %s",
                        (double) Math.round(counterSeats.getValue() * cost * 10) / 10, routeTrip.getCurrency());
                textCost.setText(newCost);
            } catch (Exception ignore) {
                Timber.d("Can't parse to float: %s", textCost.getText());
            }
        });

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog bottomSheetDialog = super.onCreateDialog(savedInstanceState);

        bottomSheetDialog.setOnShowListener(dialogInterface -> {
            FrameLayout bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);

            BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
            behavior.setSkipCollapsed(true);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });
        return bottomSheetDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart(availableSeatsCount);
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }

    @OnClick(R.id.btn_confirm_reservation)
    public void onConfirmReservationButtonClick() {
        presenter.onConfirmReservationButtonClick(departureDate, routeTrip.getId(), counterSeats.getValue());
    }

    @Override
    public void disableConfirmReservationButton() {
        btnConfirmReservation.setEnabled(false);
    }

    @Override
    public void enableConfirmReservationButton() {
        btnConfirmReservation.setEnabled(true);
    }

    @Override
    public void setPassengerName(String name) {
        textPassengerInfo.setText(name);
    }

    @Override
    public void setSeatsCounterRange(int minSeatsValue, int maxSeatsValue) {
        counterSeats.setValue(minSeatsValue);
        counterSeats.setMinValue(minSeatsValue);
        counterSeats.setMaxValue(maxSeatsValue);
    }

    @Override
    public void close() {
        dismiss();
    }

    @Override
    public void closeOnBooked() {
        listener.onRouteTripBooked();
        close();
    }
}
