package com.example.minibus.ui.stops;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.minibus.AppConstants;
import com.example.minibus.data.network.pojo.city.CityResponse;
import com.example.minibus.data.network.pojo.city.BusStop;
import com.example.minibus.ui.R;
import com.example.minibus.ui.base.BaseDialogFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.kennyc.view.MultiStateView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BusStopsFragment extends BaseDialogFragment implements
        BusStopsAdapter.OnItemClickListener,
        BusStopsContract.View {

    public static final int REQ_CODE = AppConstants.BUS_STOPS_FRAGMENT_REQ_CODE;

    @BindView(R.id.container_cities) MultiStateView multiStateView;
    @BindView(R.id.list_cities) ExpandableListView expandableListCities;
    @BindView(R.id.appbar_bus_stops) AppBarLayout appbar;
    @BindView(R.id.toolbar_custom) Toolbar toolbar;
    @BindView(R.id.text_toolbar_title) TextView toolbarTitle;

    @Inject
    BusStopsPresenter<BusStopsContract.View> presenter;

    public interface BusStopsFragmentCallback {
        void onChangedDepartureBusStop(BusStop selectedDepartureBusStop);
        void onChangedArrivalBusStop(BusStop arrivalCityFinalBusStop, BusStop departureCityStartBusStop);
    }

    private BusStopsFragmentCallback callback;
    private BusStopsAdapter adapter;

    public static BusStopsFragment newInstance() {
        return new BusStopsFragment();
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.style.FragmentAnimation;
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_DialogFragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bus_stops, container, false);

        callback = (BusStopsFragmentCallback) getTargetFragment();
        setUnbinder(ButterKnife.bind(this, view));
        getActivityComponent().inject(this);
        presenter.attachView(this);

        toolbar.setNavigationIcon(R.drawable.ic_close_dark_24dp);
        toolbar.setNavigationOnClickListener(v -> presenter.onCloseButtonClick());
        toolbarTitle.setText(getMainActivity().getResources().getString(R.string.bus_stops_title));

        return view;
    }

    @OnClick(R.id.button_empty_bus_stops)
    public void onRefreshButtonClick() {
        presenter.onRefreshButtonClick();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    public void onBusStopClick(View view, BusStop selectedBusStop) {
        presenter.onDepartureBusStopSelected(selectedBusStop);
    }

    @Override
    public void changeDepartureBusStop(BusStop selectedBusStop) {
        callback.onChangedDepartureBusStop(selectedBusStop);
    }

    @Override
    public void changeArrivalBusStop(BusStop finalArrivalBusStop, BusStop startDepartureBusStop) {
        callback.onChangedArrivalBusStop(finalArrivalBusStop, startDepartureBusStop);
    }

    @Override
    public void setCitiesData(List<CityResponse> cities, int prevSelectedBusStopId) {
        adapter = new BusStopsAdapter(getMainActivity(), cities, prevSelectedBusStopId);
        expandableListCities.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        multiStateView.setViewState(MultiStateView.ViewState.CONTENT);
    }

    @Override
    public void showEmptyView() {
        if (multiStateView.getViewState() != MultiStateView.ViewState.EMPTY) {
            multiStateView.setViewState(MultiStateView.ViewState.EMPTY);
        }
    }

    @Override
    public void showProgress() {
        if (multiStateView.getViewState() != MultiStateView.ViewState.LOADING) {
            multiStateView.setViewState(MultiStateView.ViewState.LOADING);
        }
    }

    @Override
    public void hideProgress() {
        if (multiStateView.getViewState() == MultiStateView.ViewState.LOADING) {
            multiStateView.setViewState(MultiStateView.ViewState.CONTENT);
        }
    }

    @Override
    public void close() {
        dismiss();
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }
}
