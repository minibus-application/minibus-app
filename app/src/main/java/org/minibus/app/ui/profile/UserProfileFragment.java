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
    @BindView(R.id.btn_action) MaterialButton buttonLogout;

    @Inject UserProfilePresenter<UserProfileContract.View> presenter;

    public static final int REQ_CODE = AppConstants.USER_PROFILE_FRAGMENT_REQ_CODE;
    private LinearLayoutManager layoutManager;
    private UserBookingsAdapter adapter;

    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogSlideAnimation;
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_DialogFragment);
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

        toolbar.setNavigationIcon(R.drawable.ic_close_dark_24dp);
        toolbar.setNavigationOnClickListener(v -> presenter.onCloseButtonClick());

        textToolbarSubtitle.setVisibility(View.VISIBLE);
        buttonLogout.setVisibility(View.VISIBLE);
        buttonLogout.setText(R.string.logout);

        swipeRefreshBookings.setOnRefreshListener(this);
        swipeRefreshBookings.setColorSchemeResources(R.color.colorAccent);

        layoutManager = new LinearLayoutManager(getMainActivity(), RecyclerView.VERTICAL, false);
        adapter = new UserBookingsAdapter(getMainActivity());
        adapter.setOnItemClickListener(this);
        recyclerBookings.setAdapter(adapter);
        recyclerBookings.setLayoutManager(layoutManager);

        return view;
    }

    @OnClick(R.id.btn_action)
    public void onLogoutButtonClick() {
        presenter.onLogoutButtonClick();
    }

    @OnClick(R.id.button_empty_route_schedule)
    public void onRouteScheduleButtonClick() {
        presenter.onRouteScheduleButtonClick();
    }

    @Override
    public void onRefresh() {
        if (buttonActiveBookings.isChecked()) {
            presenter.onRefreshActiveBookings();
        } else if (buttonBookingsHistory.isChecked()) {
            presenter.onRefreshBookingsHistory();
        }
    }

    @Override
    public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        if (checkedId == buttonActiveBookings.getId() && isChecked) {
            presenter.onActiveBookingsTabSelected();
        }
        if (checkedId == buttonBookingsHistory.getId() && isChecked) {
            presenter.onBookingsHistoryTabSelected();
        }
    }

    @Override
    protected void onBack() {
        presenter.onCloseButtonClick();
    }

    @Override
    public void onBookingActionButtonClick(View view, String id) {
        presenter.onBookingCancelButtonClick(id);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    public void hideRefresh() {
        swipeRefreshBookings.setRefreshing(false);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void setActiveTabCounter(int bookingsCount) {
//        if (buttonActiveBookings.isChecked()) {
//            buttonActiveBookings.setText(String.format("%s (%d)", getResources().getString(R.string.active), bookingsCount));
//            buttonBookingsHistory.setText(getResources().getString(R.string.history));
//        } else if (buttonBookingsHistory.isChecked()) {
//            buttonBookingsHistory.setText(String.format("%s (%d)", getResources().getString(R.string.history), bookingsCount));
//            buttonActiveBookings.setText(getResources().getString(R.string.active));
//        }
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
    public void logout() {
        close();
    }

    @Override
    public void close() {
        dismiss();
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
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }
}
