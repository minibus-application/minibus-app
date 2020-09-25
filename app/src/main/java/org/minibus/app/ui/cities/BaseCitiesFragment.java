package org.minibus.app.ui.cities;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
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
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public abstract class BaseCitiesFragment extends BaseDialogFragment implements
        CitiesAdapter.OnCityItemClickListener,
        BaseCitiesContract.View {

    @BindView(R.id.msv_cities) MultiStateView multiStateView;
    @BindView(R.id.rv_cities) RecyclerView recyclerCities;
    @BindView(R.id.appbar_cities) AppBarLayout appbar;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_toolbar_title) TextView toolbarTitle;
    @BindView(R.id.tv_toolbar_subtitle) TextView toolbarSubtitle;

    @Inject
    BaseCitiesPresenter<BaseCitiesContract.View> presenter;

    protected LinearLayoutManager layoutManager;
    protected CitiesAdapter adapter;
    protected ArrayList<City> cities;

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
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_DialogFragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cities, container, false);

        setUnbinder(ButterKnife.bind(this, view));
        getActivityComponent().inject(this);
        presenter.attachView(this);

        toolbar.setNavigationIcon(R.drawable.ic_close_dark_24dp);
        toolbar.setNavigationOnClickListener(v -> presenter.onCloseButtonClick());
        toolbarTitle.setText(getTitle());
        toolbarSubtitle.setVisibility(View.GONE);

        layoutManager = new LinearLayoutManager(getMainActivity());
        adapter = new CitiesAdapter(getMainActivity());
        adapter.setOnItemClickListener(this);

        recyclerCities.setAdapter(adapter);
        recyclerCities.setLayoutManager(layoutManager);
        recyclerCities.setHasFixedSize(false);

        return view;
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }

    /**
     * OnClick methods
     */

    @OnClick(R.id.btn_empty_cities)
    public void onRouteScheduleButtonClick() {
        presenter.onCloseButtonClick();
    }

    /**
     * Listeners
     */

    @Override
    public void onCityClick(View view, City city, int pos) {
        presenter.onCitySelect(city);
    }

    @Override
    public void onCityLocationClick(View view, City city, int pos) {
        try {
            Uri intentUri = Uri.parse(String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%s,%s",
                    city.getLatitude(), city.getLongitude()));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, intentUri);
            startActivity(mapIntent);
        } catch (Exception e) {
            showError(R.string.error_finding_direction);
        }
    }

    @Override
    protected void onBack() {
        presenter.onCloseButtonClick();
    }

    /**
     * View contract methods
     */

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

    protected void setData(List<City> cities, String prevSelectedCityId) {
        adapter.setData(cities, prevSelectedCityId);
        multiStateView.setViewState(MultiStateView.ViewState.CONTENT);
    }

    protected abstract String getTitle();
}
