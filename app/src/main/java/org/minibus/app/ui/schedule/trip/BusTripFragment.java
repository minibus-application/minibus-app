package org.minibus.app.ui.schedule.trip;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.minibus.app.AppConstants;
import org.minibus.app.data.network.pojo.city.BusStop;
import org.minibus.app.data.network.pojo.schedule.BusTrip;
import org.minibus.app.utils.CommonUtil;
import org.minibus.app.ui.R;
import org.minibus.app.ui.base.BaseDialogFragment;
import com.google.android.material.button.MaterialButton;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BusTripFragment extends BaseDialogFragment implements BusTripContract.View {

    public static final int REQ_CODE = AppConstants.BUS_TRIP_FRAGMENT_REQ_CODE;

    @BindView(R.id.radio_group_sum_passengers) RadioGroup radioGroupPassengers;
    @BindView(R.id.text_sum_arr_time) TextView textArrivalTime;
    @BindView(R.id.text_sum_dep_time) TextView textDepartureTime;
    @BindView(R.id.text_sum_dep_date) TextView textDepartureDate;
    @BindView(R.id.text_sum_dep_bus_stop) TextView textDepartureBusStop;
    @BindView(R.id.button_sum_cancel) MaterialButton buttonCancel;
    @BindView(R.id.button_sum_book) MaterialButton buttonBook;

    @Inject BusTripPresenter<BusTripContract.View> presenter;

    public interface BusTripFragmentCallback {
        void onBusTripBooked();
        void onRedirectToLogin();
    }

    public static final String BUS_STOP_KEY = "key_bus_stop";
    public static final String BUS_TRIP_KEY = "key_bus_trip";
    public static final String BUS_DATE_KEY = "key_bus_date";

    private BusTripFragmentCallback callback;
    private BusTrip busTrip;
    private BusStop departureBusStop;
    private String departureDate;
    private int seatsCount;

    public static BusTripFragment newInstance() {
        return new BusTripFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogScaleAnimation;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            busTrip = (BusTrip) getArguments().getSerializable(BUS_TRIP_KEY);
            departureBusStop = (BusStop) getArguments().getSerializable(BUS_STOP_KEY);
            departureDate = getArguments().getString(BUS_DATE_KEY);
            seatsCount = busTrip.getSeatsCount();
        } else {
            dismiss();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getMainActivity());
        View view = getMainActivity().getLayoutInflater().inflate(R.layout.fragment_bus_trip, null);

        builder.setView(view).setTitle(R.string.bus_trip_summary_title);

        callback = (BusTripFragment.BusTripFragmentCallback) getTargetFragment();
        setUnbinder(ButterKnife.bind(this, view));
        getActivityComponent().inject(this);
        presenter.attachView(this);

        Resources res = getMainActivity().getResources();

        textDepartureTime.setText(res.getString(R.string.label_departure_time, busTrip.getDepartureTime()));
        textArrivalTime.setText(res.getString(R.string.label_arrival_time, busTrip.getArrivalTime()));
        textDepartureBusStop.setText(res.getString(R.string.label_bus_stop, departureBusStop.getName()));
        textDepartureDate.setText(res.getString(R.string.label_date, departureDate));

        return builder.create();
    }

    @OnClick(R.id.button_sum_cancel)
    public void onCancelButtonClick() {
        presenter.onCancelClick();
    }

    @OnClick(R.id.button_sum_book)
    public void onBookButtonClick() {
        presenter.onBookClick(departureDate, busTrip, departureBusStop, getPassengersCount());
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onSetupPassengersOptions(seatsCount);
    }

    @Override
    public void setPassengersCount(int passengersCount) {
        RadioButton[] radioButtons = new RadioButton[passengersCount];
        int margin = CommonUtil.dpToPx(getMainActivity(),4);
        int height = CommonUtil.dpToPx(getMainActivity(),32);
        int width = height;

        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(width, height);
        params.setMargins(margin, margin, margin, margin);

        if (radioGroupPassengers.getChildCount() == 0) {
            for (int i = 0; i < passengersCount; i++) {
                radioButtons[i] = new RadioButton(getMainActivity());
                radioButtons[i].setText(String.valueOf(i + 1));
                radioButtons[i].setChecked(i == 0);
                radioButtons[i].setId(i);
                radioButtons[i].setLayoutParams(params);

                radioGroupPassengers.addView(radioButtons[i]);
            }
        }
    }

    @Override
    public void openLogin() {
        callback.onRedirectToLogin();
        close();
    }

    @Override
    public void closeOnBooked() {
        callback.onBusTripBooked();
        close();
    }

    @Override
    public void close() {
        dismiss();
    }

    @Override
    public void hide() {
        getDialog().hide();
    }

    @Override
    public void resume() {
        getDialog().show();
    }

    @Override
    public void showError(String msg) {
        close();
        super.showError(msg);
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }

    private int getPassengersCount() {
        int checkedId = radioGroupPassengers.getCheckedRadioButtonId();
        View radioButton = radioGroupPassengers.findViewById(checkedId);

        int idx = radioGroupPassengers.indexOfChild(radioButton);
        RadioButton rb = (RadioButton) radioGroupPassengers.getChildAt(idx);

        return Integer.valueOf(rb.getText().toString());
    }
}
