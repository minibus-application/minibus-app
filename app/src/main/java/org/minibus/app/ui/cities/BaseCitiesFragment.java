package org.minibus.app.ui.cities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.kennyc.view.MultiStateView;

import org.minibus.app.data.network.pojo.city.City;
import org.minibus.app.ui.R;
import org.minibus.app.ui.base.BaseDialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public abstract class BaseCitiesFragment extends BaseDialogFragment implements
        CitiesAdapter.OnItemClickListener,
        BaseCitiesContract.View {

    @BindView(R.id.msv_cities) MultiStateView multiStateView;
    @BindView(R.id.rv_cities) RecyclerView recyclerCities;
    @BindView(R.id.appbar_cities) AppBarLayout appbar;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_toolbar_title) TextView toolbarTitle;

    @Inject
    BaseCitiesPresenter<BaseCitiesContract.View> presenter;

    protected LinearLayoutManager layoutManager;
    protected CitiesAdapter adapter;
    protected ArrayList<City> cities;
    private String fragmentTitle;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.FragmentAnimation;
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getArguments() != null) {
//            cities = getArguments().getParcelableArrayList(CITY_KEY);
//        } else {
//            dismiss();
//        }

        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_DialogFragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bus_stops, container, false);

        // callback = (CitiesFragment.BusStopsFragmentCallback) getTargetFragment();
        setUnbinder(ButterKnife.bind(this, view));
        getActivityComponent().inject(this);
        presenter.attachView(this);

        toolbar.setNavigationIcon(R.drawable.ic_close_dark_24dp);
        toolbar.setNavigationOnClickListener(v -> presenter.onCloseButtonClick());
        toolbarTitle.setText(getTitle());

        layoutManager = new LinearLayoutManager(getMainActivity());
        adapter = new CitiesAdapter(getMainActivity());
        adapter.setOnItemClickListener(this);

//        DividerItemDecoration itemDecorator = new DividerItemDecoration(getMainActivity(), DividerItemDecoration.VERTICAL);
//        itemDecorator.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getMainActivity(), R.drawable.item_divider)));

        // recyclerCities.addItemDecoration(itemDecorator);
        recyclerCities.setAdapter(adapter);
        recyclerCities.setLayoutManager(layoutManager);
        recyclerCities.setHasFixedSize(false);

        return view;
    }

    @OnClick(R.id.button_empty_cities)
    public void onBusScheduleButtonClick() {
        presenter.onCloseButtonClick();
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void onCityClick(View view, City city, int pos) {
        presenter.onCitySelect(city);
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
    protected void onBack() {
        presenter.onCloseButtonClick();
    }

    @Override
    public void close() {
        dismiss();
    }

    protected void setData(List<City> cities, long prevSelectedCityId) {
        adapter.setData(cities, prevSelectedCityId);
        multiStateView.setViewState(MultiStateView.ViewState.CONTENT);
    }

    protected abstract String getTitle();
}
