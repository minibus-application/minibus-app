package org.minibus.app.ui.schedule;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.minibus.app.data.network.pojo.city.City;
import org.minibus.app.data.network.pojo.route.Route;
import org.minibus.app.ui.cities.arrival.ArrivalCitiesFragment;
import org.minibus.app.ui.cities.departure.DepartureCitiesFragment;
import org.minibus.app.utils.CommonUtil;
import org.minibus.app.ui.base.BaseFragment;
import org.minibus.app.ui.custom.BadgeDrawable;
import org.minibus.app.ui.login.LoginFragment;
import org.minibus.app.ui.profile.UserProfileFragment;
import org.minibus.app.ui.schedule.trip.BusTripFragment;
import org.minibus.app.helpers.AppAnimHelper;

import com.google.android.material.appbar.AppBarLayout;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.appcompat.widget.Toolbar;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.minibus.app.ui.base.BackButtonListener;
import org.minibus.app.data.network.pojo.schedule.BusTrip;
import org.minibus.app.ui.R;
import org.minibus.app.ui.custom.SpanningLinearLayoutManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.kennyc.view.MultiStateView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BusScheduleFragment extends BaseFragment implements
        BusScheduleContract.View,
        SwipeRefreshLayout.OnRefreshListener,
        BusScheduleAdapter.OnItemClickListener,
        BusScheduleCalendarAdapter.OnItemClickListener,
        DepartureCitiesFragment.OnCitySelectListener,
        ArrivalCitiesFragment.OnCitySelectListener,
        BusTripFragment.OnBusTripBookingListener,
        LoginFragment.LoginFragmentCallback,
        UserProfileFragment.UserProfileFragmentCallback,
        BackButtonListener {

    @BindView(R.id.cl_schedule_content) CoordinatorLayout layoutContent;
    @BindView(R.id.recycler_calendar) RecyclerView recyclerCalendar;
    @BindView(R.id.recycler_bus_schedule) RecyclerView recyclerBusSchedule;
    @BindView(R.id.swipe_refresh_bus_schedule) SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.button_swap_direction) ImageButton buttonSwapDirection;
    @BindView(R.id.input_dep_city) TextInputEditText inputDepartureBusStop;
    @BindView(R.id.input_arr_city) TextInputEditText inputArrivalBusStop;
    @BindView(R.id.tv_toolbar_title) TextView textToolbarTitle;
    @BindView(R.id.tv_toolbar_subtitle) TextView textToolbarSubtitle;
    @BindView(R.id.appbar) AppBarLayout appBar;
    @BindView(R.id.container_bus_schedule) MultiStateView multiStateView;
    @BindView(R.id.toolbar_custom) Toolbar toolbar;
    @BindView(R.id.fab_filter) FloatingActionButton fabFilter;
    @BindView(R.id.fab_jump_top) FloatingActionButton fabJumpTop;

    @Inject BusSchedulePresenter<BusScheduleContract.View> presenter;

    private MenuItem menuItemProfile;
    private LinearLayoutManager layoutManagerBusSchedule;
    private SpanningLinearLayoutManager layoutManagerCalendar;
    private BusScheduleCalendarAdapter adapterCalendar;
    private BusScheduleAdapter adapterBusSchedule;
    private BadgeDrawable drawableProfileBadge;

    private boolean isFilterExpanded = true;

    public static BusScheduleFragment newInstance() {
        return new BusScheduleFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bus_schedule, container, false);
        setHasOptionsMenu(true);

        getActivityComponent().inject(this);
        setUnbinder(ButterKnife.bind(this, view));
        presenter.attachView(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        fabJumpTop.setVisibility(View.INVISIBLE);

        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.setColorSchemeResources(R.color.colorAccent);

        layoutManagerCalendar = new SpanningLinearLayoutManager(getMainActivity(), LinearLayoutManager.HORIZONTAL, false);
        adapterCalendar = new BusScheduleCalendarAdapter(getMainActivity());
        adapterCalendar.setOnItemClickListener(this);
        recyclerCalendar.setAdapter(adapterCalendar);
        recyclerCalendar.setLayoutManager(layoutManagerCalendar);
        recyclerCalendar.setHasFixedSize(true);

        layoutManagerBusSchedule = new LinearLayoutManager(getMainActivity());
        adapterBusSchedule = new BusScheduleAdapter(getMainActivity());
        adapterBusSchedule.setOnItemClickListener(this);

//        DividerItemDecoration itemDecorator = new DividerItemDecoration(getMainActivity(), DividerItemDecoration.VERTICAL);
//        itemDecorator.setDrawable(ContextCompat.getDrawable(getMainActivity(), R.drawable.item_divider));
//        recyclerBusSchedule.addItemDecoration(itemDecorator);
        recyclerBusSchedule.setAdapter(adapterBusSchedule);
        recyclerBusSchedule.setLayoutManager(layoutManagerBusSchedule);
        recyclerBusSchedule.setHasFixedSize(false);
        recyclerBusSchedule.setItemAnimator(null);
        recyclerBusSchedule.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visiblePos = layoutManagerBusSchedule.findFirstVisibleItemPosition();

                if (dy < 0) showJumpTopFab();
                if (dy > 0 || (visiblePos <= 1 && visiblePos >= 0)) hideJumpTopFab();
            }
        });

        (getMainActivity()).setSupportActionBar(toolbar);
        (getMainActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        appBar.setOutlineProvider(null);
        appBar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onAppBarStateChanged(AppBarLayout appBarLayout, State state) {
                if (state.name().equals(State.COLLAPSED.toString())) {
                    presenter.onFilterCollapsed();
                    isFilterExpanded = false;
                } else if (state.name().equals(State.EXPANDED.toString())) {
                    presenter.onFilterExpanded();
                    isFilterExpanded = true;
                }
            }
        });

        textToolbarTitle.setText(getResources().getString(R.string.bus_schedule_title));
        textToolbarSubtitle.setVisibility(View.VISIBLE);
        textToolbarSubtitle.setText(getResources().getString(R.string.bus_schedule_filter_title));

        // disable custom ItemAnimator to prevent blinking on notifyItemChanged
        RecyclerView.ItemAnimator animator = recyclerBusSchedule.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }

        // shift progress indicator from center to top with a small margin
        // because of the collapsing toolbar layout behavior
        try {
            RelativeLayout loadingView = (RelativeLayout) multiStateView.getView(MultiStateView.ViewState.LOADING);
            loadingView.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
            loadingView.setPadding(0, CommonUtil.dpToPx(getMainActivity(), 32), 0, 0);
        } catch (NullPointerException e) {}
    }

    @OnClick(R.id.button_empty_cities)
    public void onDepartureCitiesButtonClick() {
        presenter.onDepartureCityClick();
    }

    @OnClick(R.id.fab_filter)
    public void onFilterFabClick() {
        presenter.onFilterFabClick();
    }

    @OnClick(R.id.fab_jump_top)
    public void onJumpTopFabClick() {
        presenter.onJumpTopFabClick();
    }

    @OnClick(R.id.button_swap_direction)
    public void onSwapDirectionButtonClick() {
        presenter.onDirectionSwapButtonClick(adapterCalendar.getSelectedDate());
    }

    @OnClick(R.id.input_dep_city)
    public void onDepartureCityFieldClick() {
        presenter.onDepartureCityClick();
    }

    @OnClick(R.id.input_arr_city)
    public void onArrivalCityFieldClick() {
        presenter.onArrivalCityClick();
    }

    @Override
    public void onLoggedIn() {
        presenter.onUserLoggedIn();
    }

    @Override
    public void onLoggedOut() {
        presenter.onUserLoggedOut();
    }

    @Override
    public void onUserBookingsUpdate() {
        presenter.onUserBookingsUpdate();
    }

    @Override
    public void onBusTripBooked() {
        presenter.onUserBookedBusTrip(adapterCalendar.getSelectedDate());
    }

    @Override
    public void onArrivalCitySelected(City city) {
        presenter.onArrivalCityChange(city, adapterCalendar.getSelectedDate());
    }

    @Override
    public void onDepartureCitySelected(City city) {
        presenter.onDepartureCityChange(city, adapterCalendar.getSelectedDate());
    }

    @Override
    public void updateProfileBadge() {
        getMainActivity().invalidateOptionsMenu();
    }

    @Override
    public void setDirectionDescription(String text) {
        AppAnimHelper.textFadeInOut(textToolbarSubtitle, text);
    }

    @Override
    public void setDirectionDescription(int resId) {
        AppAnimHelper.textFadeInOut(textToolbarSubtitle, getResources().getString(resId));
    }

    @Override
    public void hideRefresh() {
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void showRefresh() {
        swipeRefresh.setRefreshing(true);
    }

    @Override
    public void showLoadingDataDialog() {
        super.showProgress();
    }

    @Override
    public void hideLoadingDataDialog() {
        super.hideProgress();
    }

    @Override
    public void toggleFilter() {
        if (isFilterExpanded) {
            appBar.setExpanded(false, true);
        } else {
            appBar.setExpanded(true, true);
        }
    }

    @Override
    public void showEmptyView() {
        if (multiStateView.getViewState() != MultiStateView.ViewState.EMPTY) {
            multiStateView.setViewState(MultiStateView.ViewState.EMPTY);
        }
    }

    @Override
    public void showFilter() {
        appBar.setExpanded(true, true);
    }

    @Override
    public void showBusTripLoading() {
        adapterBusSchedule.setLoading(true);
    }

    @Override
    public void hideBusTripLoading() {
        adapterBusSchedule.setLoading(false);
    }

    @Override
    public void showJumpTopFab() {
        fabJumpTop.show();
    }

    @Override
    public void hideJumpTopFab() {
        fabJumpTop.hide();
    }

    @Override
    public void showProgress() {
        if (multiStateView.getViewState() != MultiStateView.ViewState.LOADING) {
            multiStateView.setViewState(MultiStateView.ViewState.LOADING);
        }
    }

    @Override
    public void hideProgress() {
        if (swipeRefresh.isRefreshing()) swipeRefresh.setRefreshing(false);

        if (multiStateView.getViewState() == MultiStateView.ViewState.LOADING) {
            multiStateView.setViewState(MultiStateView.ViewState.CONTENT);
        }
    }

    @Override
    public void showSwapDirectionAnimation() {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 180,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration((long) 200);
        rotateAnimation.setRepeatCount(0);
        buttonSwapDirection.startAnimation(rotateAnimation);
    }

    @Override
    public void setDirection(String departureCity, String arrivalCity) {
        setDepartureCity(departureCity);
        setArrivalCity(arrivalCity);
    }

    @Override
    public void setOperationalDays(List<Integer> daysOfWeek) {

    }

    @Override
    public void setDepartureCity(String city) {
        inputDepartureBusStop.setText(city);
    }

    @Override
    public void setArrivalCity(String city) {
        inputArrivalBusStop.setText(city);
    }

    @Override
    public void setProfileBadge(int bookingsQuantity) {
        LayerDrawable profileIcon = (LayerDrawable) menuItemProfile.getIcon();
        Drawable drawable = profileIcon.findDrawableByLayerId(R.id.ic_profile_badge);

        if (drawableProfileBadge != null && drawable instanceof BadgeDrawable) {
            drawableProfileBadge = (BadgeDrawable) drawable;
        } else {
            drawableProfileBadge = new BadgeDrawable.Builder()
                    .withCounter(true)
                    .withColor(R.color.colorError)
                    .build();
        }

        drawableProfileBadge.setCount(bookingsQuantity);

        profileIcon.mutate();
        profileIcon.setDrawableByLayerId(R.id.ic_profile_badge, drawableProfileBadge);
    }

    @Override
    public void setBusScheduleData(List<BusTrip> busTrips, Route route) {
        Drawable background;

        if (busTrips == null || busTrips.isEmpty()) {
            multiStateView.setViewState(MultiStateView.ViewState.EMPTY);
            background = multiStateView.getBackground();
        } else {
            adapterBusSchedule.setData(busTrips, route);
            multiStateView.setViewState(MultiStateView.ViewState.CONTENT);
            background = recyclerBusSchedule.getBackground();
        }

        // hack due to the custom rounded filter background drawable
        if (background instanceof ColorDrawable) {
            layoutContent.setBackgroundColor(((ColorDrawable) background).getColor());
        }
    }

    @Override
    public void openBusTripSummary(BusTrip busTrip, Route route, String depDate) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(BusTripFragment.BUS_TRIP_KEY, busTrip);
        bundle.putSerializable(BusTripFragment.BUS_ROUTE_KEY, route);
        bundle.putString(BusTripFragment.BUS_DATE_KEY, depDate);

        super.openDialogFragment(BusTripFragment.newInstance(), BusTripFragment.REQ_CODE, bundle);
    }

    @Override
    public void openProfile() {
        super.openDialogFragment(UserProfileFragment.newInstance(), UserProfileFragment.REQ_CODE, null);
    }

    @Override
    public void openLogin() {
        super.openDialogFragment(LoginFragment.newInstance(), LoginFragment.REQ_CODE, null);
    }

    @Override
    public void openDepartureCities() {
        super.openDialogFragment(DepartureCitiesFragment.newInstance(), DepartureCitiesFragment.REQ_CODE, null);
    }

    @Override
    public void openArrivalCities() {
        super.openDialogFragment(ArrivalCitiesFragment.newInstance(), ArrivalCitiesFragment.REQ_CODE, null);
    }

    @Override
    public void jumpTop() {
        fabJumpTop.hide();
        recyclerBusSchedule.scrollToPosition(0);
    }

    @Override
    public void onDateClick(View view, int position) {
        presenter.onDateClick(adapterCalendar.getDate(position));
    }

    @Override
    public void onBusTripSelect(View view, long id, int pos, String routeId) {
        presenter.onBusTripSelectButtonClick(adapterCalendar.getSelectedDate(), id, pos, routeId);
    }

    @Override
    public void onRefresh() {
        presenter.onRefresh(adapterCalendar.getSelectedDate());
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart(adapterCalendar.getSelectedDate());
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_bus_schedule, menu);
        menuItemProfile = menu.findItem(R.id.menu_item_profile);

        presenter.onCreateProfileBadge();

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_profile) presenter.onProfileIconClick();
        return true;
    }

    @Override
    public boolean onBackPressed() {
        presenter.onBackPressed();
        return true;
    }

    @Override
    public void finish() {
        getMainActivity().finish();
    }
}
