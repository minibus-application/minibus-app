package org.minibus.app.ui.profile;

import android.animation.LayoutTransition;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.minibus.app.AppConstants;
import org.minibus.app.data.network.pojo.booking.Booking;
import org.minibus.app.ui.R;
import org.minibus.app.ui.main.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.kennyc.view.MultiStateView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

public class UserProfileFragment extends BottomSheetDialogFragment implements
        UserProfileContract.View,
        UserBookingsAdapter.OnItemClickListener {

    public static final int REQ_CODE = AppConstants.USER_PROFILE_FRAGMENT_REQ_CODE;

    @BindView(R.id.bottom_sheet_root) RelativeLayout layoutRoot;
    @BindView(R.id.image_user_avatar) ImageView imageUserAvatar;
    @BindView(R.id.text_user_name) TextView textUserName;
    @BindView(R.id.text_user_phone) TextView textUserPhone;
    @BindView(R.id.button_user_logout) Button buttonLogout;
    @BindView(R.id.recycler_user_bookings) RecyclerView recyclerBookings;
    @BindView(R.id.indicator_user_bookings) ScrollingPagerIndicator indicatorBookings;
    @BindView(R.id.multi_state_user_bookings) MultiStateView multiStateView;
    @BindView(R.id.container_user_bookings) RelativeLayout containerUserBookings;

    @Inject UserProfilePresenter<UserProfileContract.View> presenter;

    public interface UserProfileFragmentCallback {
        void onUserBookingsUpdate();
        void onLoggedOut();
    }

    private Unbinder unbinder;
    private MainActivity activity;
    private LinearLayoutManager layoutManager;
    private UserBookingsAdapter adapter;
    private UserProfileFragmentCallback callback;

    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            this.activity = (MainActivity) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_BottomSheetDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);

        callback = (UserProfileFragment.UserProfileFragmentCallback) getTargetFragment();
        unbinder = ButterKnife.bind(this, view);
        getMainActivity().getActivityComponent().inject(this);
        presenter.attachView(this);

        layoutManager = new LinearLayoutManager(getMainActivity(), RecyclerView.HORIZONTAL, false);
        adapter = new UserBookingsAdapter(getMainActivity());
        adapter.setOnItemClickListener(this);
        recyclerBookings.setAdapter(adapter);
        recyclerBookings.setLayoutManager(layoutManager);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerBookings);

        indicatorBookings.attachToRecyclerView(recyclerBookings);

        // enable "animateLayoutChanges" programmatically and setting a flag on it
        // that prevents bottom sheet from unexpected jumping on layout changes
        LayoutTransition transition = new LayoutTransition();
        transition.setAnimateParentHierarchy(false);
        multiStateView.setLayoutTransition(transition);

        try {
            // set Empty state view height to all container nested views
            View emptyView = multiStateView.getView(MultiStateView.ViewState.EMPTY);
            emptyView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int emptyViewHeight = emptyView.getMeasuredHeight();

            ViewGroup.LayoutParams multiStateViewParams = multiStateView.getLayoutParams();
            multiStateViewParams.height = emptyViewHeight;
            multiStateView.setLayoutParams(multiStateViewParams);
        } catch (NullPointerException e) {}

        return view;
    }

    @OnClick(R.id.button_empty_bus_schedule)
    public void onBusScheduleButtonClick() {
        presenter.onBusScheduleButtonClick();
    }

    @OnClick(R.id.button_user_logout)
    public void onLogoutButtonClick() {
        presenter.onLogoutButtonClick();
    }

    @Override
    public MainActivity getMainActivity() {
        return activity;
    }

    @Override
    public void onBookingCancelButtonClick(View view, String id) {
        presenter.onBookingCancelButtonClick(id);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    public void updateUserBookingsBadge() {
        callback.onUserBookingsUpdate();
    }

    @Override
    public void setUserBookingsData(List<Booking> bookings) {
        adapter.setData(bookings);
        multiStateView.setViewState(MultiStateView.ViewState.CONTENT);
    }

    @Override
    public void setUserData(String userName, String userPhone) {
        textUserName.setText(userName);
        textUserPhone.setText(userPhone);
    }

    @Override
    public void logout() {
        callback.onLoggedOut();
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
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroyView();
    }
}
