package org.minibus.app.ui.schedule;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.jetbrains.annotations.NotNull;
import org.minibus.app.AppConstants;
import org.minibus.app.data.network.pojo.city.City;
import org.minibus.app.data.network.pojo.route.Route;
import org.minibus.app.helpers.AppAlertsHelper;
import org.minibus.app.ui.BuildConfig;
import org.minibus.app.ui.cities.arrival.ArrivalCitiesFragment;
import org.minibus.app.ui.cities.departure.DepartureCitiesFragment;
import org.minibus.app.ui.schedule.trip.RouteTripFragment;
import org.minibus.app.ui.sorting.SortingOptionsDialogFragment;
import org.minibus.app.utils.CommonUtil;
import org.minibus.app.ui.base.BaseFragment;
import org.minibus.app.ui.login.LoginFragment;
import org.minibus.app.ui.profile.UserProfileFragment;
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
import org.minibus.app.data.network.pojo.schedule.RouteTrip;
import org.minibus.app.ui.R;
import org.minibus.app.ui.custom.SpanningLinearLayoutManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.kennyc.view.MultiStateView;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RouteScheduleFragment extends BaseFragment implements
        RouteScheduleContract.View,
        SwipeRefreshLayout.OnRefreshListener,
        RouteScheduleAdapter.OnItemClickListener,
        RouteScheduleAdapter.OnSortByItemClickListener,
        RouteScheduleCalendarAdapter.OnCalendarItemClickListener,
        SortingOptionsDialogFragment.OnSortingOptionClickListener,
        DepartureCitiesFragment.OnCityClickListener,
        ArrivalCitiesFragment.OnCityClickListener,
        RouteTripFragment.OnRouteTripBookingListener,
        LoginFragment.OnLoginListener,
        BackButtonListener {

    @BindView(R.id.cl_route_schedule_content) CoordinatorLayout layoutContent;
    @BindView(R.id.rv_calendar) RecyclerView recyclerCalendar;
    @BindView(R.id.rv_route_schedule) RecyclerView recyclerRouteSchedule;
    @BindView(R.id.srl_route_schedule) SwipeRefreshLayout swipeRefreshRouteSchedule;
    @BindView(R.id.ib_swap_direction) ImageButton buttonSwapDirection;
    @BindView(R.id.et_dep_city) TextInputEditText inputDepartureCity;
    @BindView(R.id.et_arr_city) TextInputEditText inputArrivalCity;
    @BindView(R.id.tv_toolbar_title) TextView textToolbarTitle;
    @BindView(R.id.tv_toolbar_subtitle) TextView textToolbarSubtitle;
    @BindView(R.id.msv_route_schedule) MultiStateView multiStateView;
    @BindView(R.id.fab_route_direction) FloatingActionButton fabRouteDirection;
    @BindView(R.id.fab_jump_top) FloatingActionButton fabJumpTop;
    @BindView(R.id.appbar) AppBarLayout appBar;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Inject
    RouteSchedulePresenter<RouteScheduleContract.View> presenter;

    private static final RouteScheduleAdapter.SortingOption DEFAULT_SORTING_OPTION = RouteScheduleAdapter.SortingOption.DEPARTURE_TIME;
    private boolean isRouteDirectionExpanded = false;
    private Menu menu;
    private LinearLayoutManager layoutManagerRouteSchedule;
    private SpanningLinearLayoutManager layoutManagerCalendar;
    private RouteScheduleCalendarAdapter adapterCalendar;
    private RouteScheduleAdapter adapterRouteSchedule;

    public static RouteScheduleFragment newInstance() {
        return new RouteScheduleFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_route_schedule, container, false);
        setHasOptionsMenu(true);

        getActivityComponent().inject(this);
        setUnbinder(ButterKnife.bind(this, view));
        presenter.attachView(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        fabJumpTop.setVisibility(View.INVISIBLE);

        swipeRefreshRouteSchedule.setOnRefreshListener(this);
        swipeRefreshRouteSchedule.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshRouteSchedule.setProgressBackgroundColorSchemeResource(R.color.colorPrimary);

        layoutManagerCalendar = new SpanningLinearLayoutManager(getMainActivity(), LinearLayoutManager.HORIZONTAL, false);
        adapterCalendar = new RouteScheduleCalendarAdapter(getMainActivity());
        adapterCalendar.setOnItemClickListener(this);
        recyclerCalendar.setAdapter(adapterCalendar);
        recyclerCalendar.setLayoutManager(layoutManagerCalendar);
        recyclerCalendar.setHasFixedSize(true);

        layoutManagerRouteSchedule = new LinearLayoutManager(getMainActivity());
        adapterRouteSchedule = new RouteScheduleAdapter(getMainActivity());
        adapterRouteSchedule.setSortingOption(DEFAULT_SORTING_OPTION);
        adapterRouteSchedule.setOnItemClickListener(this);
        adapterRouteSchedule.setOnSortByItemClickListener(this);

        recyclerRouteSchedule.setAdapter(adapterRouteSchedule);
        recyclerRouteSchedule.setLayoutManager(layoutManagerRouteSchedule);
        recyclerRouteSchedule.setHasFixedSize(false);
        recyclerRouteSchedule.setItemAnimator(null);
        recyclerRouteSchedule.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int scrollPos = 0;

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrollPos += dy;

                int firstVisibleItemPos = layoutManagerRouteSchedule.findFirstVisibleItemPosition();

                if (dy < 0) showJumpTopFab();
                if (scrollPos == 0 || (firstVisibleItemPos <= 1 && firstVisibleItemPos >= 0)) hideJumpTopFab();
            }
        });

        (getMainActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull((getMainActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(false);

        appBar.setOutlineProvider(null);
        appBar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onAppBarStateChanged(AppBarLayout appBarLayout, State state) {
                if (state.name().equals(State.COLLAPSED.toString())) {
                    presenter.onRouteDirectionCollapsed();
                    isRouteDirectionExpanded = false;
                } else if (state.name().equals(State.EXPANDED.toString())) {
                    presenter.onRouteDirectionExpanded();
                    isRouteDirectionExpanded = true;
                }
            }
        });

        textToolbarTitle.setText(getResources().getString(R.string.route_schedule_title));
        textToolbarSubtitle.setVisibility(View.VISIBLE);
        textToolbarSubtitle.setText(getResources().getString(R.string.route_direction_title));

        // disable custom ItemAnimator to prevent blinking on notifyItemChanged
        RecyclerView.ItemAnimator animator = recyclerRouteSchedule.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }

        // shift progress indicator from center to top with a small margin
        // because of the collapsing toolbar layout behavior
        try {
            RelativeLayout loadingLayout = (RelativeLayout) multiStateView.getView(MultiStateView.ViewState.LOADING);
            loadingLayout.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
            loadingLayout.setPadding(0, CommonUtil.dpToPx(getMainActivity(), 32), 0, 0);
        } catch (NullPointerException ignore) {

        }
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
    public void onCreateOptionsMenu(@NotNull Menu menu, MenuInflater inflater) {
        // set default menu
        this.menu = menu;
        inflater.inflate(R.menu.menu_route_schedule, menu);
        super.onCreateOptionsMenu(menu, inflater);

        presenter.onCreatedOptionsMenu();
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_profile:
                presenter.onProfileMenuItemClick();
                return true;
            case R.id.menu_item_login:
                presenter.onLoginMenuItemClick();
                return true;
            case R.id.menu_item_about:
                presenter.onAboutMenuItemClick();
                return true;
            case R.id.menu_item_logout:
                presenter.onLogoutMenuItemClick();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onBackPressed() {
        presenter.onBackPressed();
        return true;
    }

    /**
     * OnClick methods
     */

    @OnClick(R.id.btn_empty_action)
    public void onDepartureCitiesButtonClick() {
        presenter.onDepartureCityFieldClick();
    }

    @OnClick(R.id.et_dep_city)
    public void onDepartureCityFieldClick() {
        presenter.onDepartureCityFieldClick();
    }

    @OnClick(R.id.et_arr_city)
    public void onArrivalCityFieldClick() {
        presenter.onArrivalCityFieldClick();
    }

    @OnClick(R.id.fab_route_direction)
    public void onRouteDirectionFabClick() {
        presenter.onRouteDirectionFabClick();
    }

    @OnClick(R.id.fab_jump_top)
    public void onJumpTopFabClick() {
        presenter.onJumpTopFabClick();
    }

    @OnClick(R.id.ib_swap_direction)
    public void onSwapRouteDirectionButtonClick() {
        presenter.onSwapRouteDirectionButtonClick(adapterCalendar.getSelectedDate());

        RotateAnimation rotateAnimation = new RotateAnimation(0, 180,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration((long) 200);
        rotateAnimation.setRepeatCount(0);
        buttonSwapDirection.startAnimation(rotateAnimation);
    }

    /**
     * Listeners
     */

    @Override
    public void onUserLoggedIn() {
        presenter.onUserAuthorized();
    }

    @Override
    public void onRouteTripBooked() {
        presenter.onRouteTripBooked(adapterCalendar.getSelectedDate());
    }

    @Override
    public void onArrivalCityItemClicked(City city) {
        presenter.onArrivalCityChanged(city, adapterCalendar.getSelectedDate());
    }

    @Override
    public void onDepartureCityItemClicked(City city) {
        presenter.onDepartureCityChanged(city, adapterCalendar.getSelectedDate());
    }

    @Override
    public void onDateItemClick(View view, int position) {
        presenter.onCalendarDateClick(adapterCalendar.getDate(position));
    }

    @Override
    public void onRouteTripItemClick(View view, String itemId) {
        presenter.onRouteTripSelectButtonClick(adapterCalendar.getSelectedDate(), itemId);
    }

    @Override
    public void onSortByItemClick(RouteScheduleAdapter.SortingOption selectedSortingOption) {
        presenter.onSortByClick(selectedSortingOption);
    }

    @Override
    public void onSortingOptionItemClick(int position) {
        RouteScheduleAdapter.SortingOption newSortingOption = RouteScheduleAdapter.SortingOption.getByPosition(position);
        if (adapterRouteSchedule.getSortingOption() != newSortingOption) {
            adapterRouteSchedule.setSortingOption(RouteScheduleAdapter.SortingOption.getByPosition(position));
        }
    }

    /**
     * View contract methods
     */

    @Override
    public void hideRefresh() {
        swipeRefreshRouteSchedule.setRefreshing(false);
    }

    @Override
    public void showRefresh() {
        swipeRefreshRouteSchedule.setRefreshing(true);
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
    public void toggleRouteDirection() {
        if (isRouteDirectionExpanded) hideRouteDirection();
        else showRouteDirection();
    }

    @Override
    public void hideRouteDirection() {
        appBar.setExpanded(false, true);
    }

    @Override
    public void showRouteDirection() {
        appBar.setExpanded(true, true);
    }

    @Override
    public void showEmptyView() {
        if (multiStateView.getViewState() != MultiStateView.ViewState.EMPTY) {
            multiStateView.setViewState(MultiStateView.ViewState.EMPTY);
        }
    }

    @Override
    public void showRouteTripLoading() {
        adapterRouteSchedule.setLoading(true);
    }

    @Override
    public void hideRouteTripLoading() {
        adapterRouteSchedule.setLoading(false);
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
    public void jumpTop() {
        fabJumpTop.hide();
        recyclerRouteSchedule.scrollToPosition(0);
    }

    @Override
    public void finish() {
        getMainActivity().finish();
    }

    @Override
    public void disableRouteDirection() {
        inputDepartureCity.setEnabled(false);
        inputArrivalCity.setEnabled(false);
        buttonSwapDirection.setEnabled(false);
    }

    @Override
    public void enableRouteDirection() {
        inputDepartureCity.setEnabled(true);
        inputArrivalCity.setEnabled(true);
        buttonSwapDirection.setEnabled(true);
    }

    @Override
    public void setOptionsMenu(boolean isUserAuthorized) {
        if (isUserAuthorized) {
            this.menu.findItem(R.id.menu_item_profile).setVisible(true);
            this.menu.findItem(R.id.menu_item_login).setVisible(false);
            this.menu.findItem(R.id.menu_item_logout).setVisible(true);
        } else {
            this.menu.findItem(R.id.menu_item_profile).setVisible(false);
            this.menu.findItem(R.id.menu_item_login).setVisible(true);
            this.menu.findItem(R.id.menu_item_logout).setVisible(false);
        }
        this.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setRouteDirectionDescription(String text) {
        fadeInOut(textToolbarSubtitle, text);
    }

    @Override
    public void setRouteDirectionDescription(int resId) {
        fadeInOut(textToolbarSubtitle, getResources().getString(resId));
    }

    @Override
    public void setRouteDirection(String depCity, String arrCity) {
        setDepartureCity(depCity);
        setArrivalCity(arrCity);
    }

    @Override
    public void setOperationalDays(List<Integer> operationalDays) {
        adapterCalendar.setActiveDays(operationalDays);
    }

    @Override
    public void setDepartureCity(String city) {
        inputDepartureCity.setText(city);
    }

    @Override
    public void setArrivalCity(String city) {
        inputArrivalCity.setText(city);
    }

    @Override
    public void setRouteScheduleData(List<RouteTrip> routeTrips, Route route) {
        Drawable background;

        if (routeTrips == null || routeTrips.isEmpty()) {
            multiStateView.setViewState(MultiStateView.ViewState.EMPTY);
            background = multiStateView.getBackground();
        } else {
            adapterRouteSchedule.setData(routeTrips, route);
            multiStateView.setViewState(MultiStateView.ViewState.CONTENT);
            background = recyclerRouteSchedule.getBackground();
        }

        // hack due to the custom rounded filter background drawable
        if (background instanceof ColorDrawable) {
            layoutContent.setBackgroundColor(((ColorDrawable) background).getColor());
        }
    }

    @Override
    public void openSortingOptions(RouteScheduleAdapter.SortingOption sortingOption) {
        ArrayList<String> options = DEFAULT_SORTING_OPTION.getOptionsResIds().stream()
                .map(this::getString)
                .collect(Collectors.toCollection(ArrayList::new));

        Bundle bundle = new Bundle();
        bundle.putStringArrayList(SortingOptionsDialogFragment.SORT_OPTIONS_KEY, options);
        bundle.putInt(SortingOptionsDialogFragment.SORT_OPTION_POS_KEY, sortingOption.getPosition());

        super.openDialogFragment(SortingOptionsDialogFragment.newInstance(), SortingOptionsDialogFragment.REQ_CODE, bundle);
    }

    @Override
    public void openRouteTripSummary(RouteTrip routeTrip, Route route, LocalDate depDate) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(RouteTripFragment.ROUTE_TRIP_KEY, routeTrip);
        bundle.putSerializable(RouteTripFragment.ROUTE_KEY, route);
        bundle.putSerializable(RouteTripFragment.DEPARTURE_DATE_KEY, depDate);

        super.openDialogFragment(RouteTripFragment.newInstance(), RouteTripFragment.REQ_CODE, bundle);
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
    public void openAbout() {
        AppAlertsHelper.showAlertDialog(getMainActivity(),
                getResources().getString(R.string.menu_about),
                String.format("Version: %s\nAuthor: %s", BuildConfig.VERSION_NAME, AppConstants.APP_AUTHOR));
    }

    /**
     * Base view contract methods
     */

    @Override
    public void showProgress() {
        if (multiStateView.getViewState() != MultiStateView.ViewState.LOADING) {
            multiStateView.setViewState(MultiStateView.ViewState.LOADING);
        }
    }

    @Override
    public void hideProgress() {
        if (swipeRefreshRouteSchedule.isRefreshing()) swipeRefreshRouteSchedule.setRefreshing(false);

        if (multiStateView.getViewState() == MultiStateView.ViewState.LOADING) {
            multiStateView.setViewState(MultiStateView.ViewState.CONTENT);
        }
    }

    private static void fadeInOut(final TextView textView, String text) {
        textView.animate().setDuration(200).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                textView.setText(text);
                textView.animate().setListener(null).setDuration(200).alpha(1.0f);
            }
        }).alpha(0.0f);
    }
}
