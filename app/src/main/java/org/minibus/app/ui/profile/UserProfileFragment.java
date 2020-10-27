package org.minibus.app.ui.profile;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.minibus.app.AppConstants;
import org.minibus.app.data.network.pojo.booking.Booking;
import org.minibus.app.ui.R;
import org.minibus.app.ui.base.BaseDialogFragment;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.kennyc.view.MultiStateView;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserProfileFragment extends BaseDialogFragment implements
        UserProfileContract.View,
        SwipeRefreshLayout.OnRefreshListener,
        MaterialButtonToggleGroup.OnButtonCheckedListener,
        UserBookingsAdapter.OnItemClickListener {

    @BindView(R.id.recycler_user_bookings) RecyclerView recyclerBookings;
    @BindView(R.id.multi_state_user_bookings) MultiStateView multiStateView;
    @BindView(R.id.btn_group) MaterialButtonToggleGroup tabsBookingsTypes;
    @BindView(R.id.btn_active_bookings) MaterialButton buttonActiveBookings;
    @BindView(R.id.btn_bookings_history) MaterialButton buttonBookingsHistory;
    @BindView(R.id.appbar_login) AppBarLayout appbar;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.srl_bookings) SwipeRefreshLayout swipeRefreshBookings;
    @BindView(R.id.tv_toolbar_title) TextView textToolbarTitle;
    @BindView(R.id.tv_toolbar_subtitle) TextView textToolbarSubtitle;

    @Inject UserProfilePresenter<UserProfileContract.View> presenter;

    public static final int REQ_CODE = AppConstants.USER_PROFILE_FRAGMENT_REQ_CODE;
    private BookingsTab checkedTab = BookingsTab.ACTIVE;
    private String defaultActiveBookingsTabName;
    private String defaultBookingsHistoryTabName;
    private LinearLayoutManager layoutManager;
    private UserBookingsAdapter adapter;

    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_DialogFragment);
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.DialogSlideAnimation;
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);

        setUnbinder(ButterKnife.bind(this, view));
        getActivityComponent().inject(this);
        presenter.attachView(this);

        tabsBookingsTypes.setSelectionRequired(true);
        tabsBookingsTypes.setSingleSelection(true);
        tabsBookingsTypes.addOnButtonCheckedListener(this);
        tabsBookingsTypes.check(buttonActiveBookings.getId());
        defaultActiveBookingsTabName = getResources().getString(R.string.active);
        defaultBookingsHistoryTabName = getResources().getString(R.string.history);
        buttonActiveBookings.setText(defaultActiveBookingsTabName);
        buttonBookingsHistory.setText(defaultBookingsHistoryTabName);

        toolbar.setNavigationIcon(R.drawable.ic_close_dark_24dp);
        toolbar.setNavigationOnClickListener(v -> presenter.onCloseButtonClick());

        textToolbarSubtitle.setVisibility(View.VISIBLE);

        swipeRefreshBookings.setOnRefreshListener(this);
        swipeRefreshBookings.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshBookings.setProgressBackgroundColorSchemeResource(R.color.colorPrimary);

        layoutManager = new LinearLayoutManager(getMainActivity(), RecyclerView.VERTICAL, false);
        adapter = new UserBookingsAdapter(getMainActivity());
        adapter.setOnItemClickListener(this);
        recyclerBookings.setAdapter(adapter);
        recyclerBookings.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart(checkedTab);
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }

    /**
     * OnClick methods
     */

    @OnClick(R.id.button_empty_route_schedule)
    public void onRouteScheduleButtonClick() {
        presenter.onRouteScheduleButtonClick();
    }

    /**
     * Listeners
     */

    @Override
    public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        if (checkedId == buttonActiveBookings.getId() && isChecked) {
            checkedTab = BookingsTab.ACTIVE;
            presenter.onBookingsTabChecked(BookingsTab.ACTIVE);
        } else if (checkedId == buttonBookingsHistory.getId() && isChecked) {
            checkedTab = BookingsTab.HISTORY;
            presenter.onBookingsTabChecked(BookingsTab.HISTORY);
        }
    }

    @Override
    public void onBookingActionButtonClick(View view, String id) {
        presenter.onBookingCancelButtonClick(id);
    }

    @Override
    protected void onBack() {
        presenter.onCloseButtonClick();
    }

    /**
     * View contract methods
     */

    @Override
    public void onRefresh() {
        presenter.onBookingsTabRefresh(checkedTab);
    }

    @Override
    public void hideRefresh() {
        swipeRefreshBookings.setRefreshing(false);
    }

    @Override
    public void resetTabsCounter() {
        buttonActiveBookings.setText(defaultActiveBookingsTabName);
        buttonBookingsHistory.setText(defaultBookingsHistoryTabName);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void setCheckedTabCounter(int bookingsCount) {
        if (checkedTab == BookingsTab.ACTIVE) {
            buttonActiveBookings.setText(String.format("%s (%d)", getResources().getString(R.string.active), bookingsCount));
        } else if (checkedTab == BookingsTab.HISTORY) {
            buttonBookingsHistory.setText(String.format("%s (%d)", getResources().getString(R.string.history), bookingsCount));
        }
    }

    @Override
    public void setUserBookingsData(List<Booking> bookings) {
        adapter.setData(bookings);

        multiStateView.setViewState(bookings == null || bookings.isEmpty()
                ? MultiStateView.ViewState.EMPTY
                : MultiStateView.ViewState.CONTENT);
    }

    @Override
    public void setUserData(String userName, String userPhone) {
        textToolbarTitle.setText(userName);
        textToolbarSubtitle.setText(userPhone);
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

    enum BookingsTab {
        ACTIVE, HISTORY
    }
}
