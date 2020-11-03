package org.minibus.app.ui.schedule;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.minibus.app.data.network.pojo.route.Route;
import org.minibus.app.ui.R;
import org.minibus.app.data.network.pojo.schedule.RouteTrip;
import org.minibus.app.ui.custom.ProgressMaterialButton;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class RouteScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private Route route = null;
    private OnItemClickListener onItemClickListener;
    private OnSortByItemClickListener onSortByItemClickListener;
    private List<RouteTrip> routeTrips = new ArrayList<>();
    private SortingOption selectedSortingOption;
    private static boolean isItemClickable = true;
    private static boolean isItemLoading = false;
    private static int lastClickedItemPos = -1;
    private static final int SORT_BY_ITEM = 0;
    private static final int ITEM = 1;

    public RouteScheduleAdapter(Context context) {
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnSortByItemClickListener(OnSortByItemClickListener onSortByItemClickListener) {
        this.onSortByItemClickListener = onSortByItemClickListener;
    }

    public void setData(List<RouteTrip> newRouteTrips, Route route) {
        DiffUtil.DiffResult diffResult =
                DiffUtil.calculateDiff(new RouteScheduleDiffUtilCallback(this.routeTrips, newRouteTrips), true);

        this.route = route;
        this.routeTrips.clear();
        this.routeTrips.addAll(newRouteTrips);

        diffResult.dispatchUpdatesTo(this);

        sortBy(this.selectedSortingOption);
    }

    public void setSortingOption(SortingOption selectedSortingOption) {
        this.selectedSortingOption = selectedSortingOption;
        sortBy(selectedSortingOption);
    }

    public SortingOption getSortingOption() {
        return selectedSortingOption;
    }

    public void setLoading(boolean isLoading) {
        isItemLoading = isLoading;
        isItemClickable = !isLoading;

        if (lastClickedItemPos >= 0) notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == SORT_BY_ITEM) {
            return new SortByViewHolder(inflater.inflate(R.layout.view_sort_by, viewGroup, false), onSortByItemClickListener);
        }
        return new RouteScheduleViewHolder(inflater.inflate(R.layout.view_route_trip, viewGroup, false), onItemClickListener);
    }

    @Override
    public long getItemId(int position) {
        return new BigInteger(routeTrips.get(position - 1).getId(), 16).longValue();
    }

    @Override
    public int getItemCount() {
        return routeTrips == null ? 0 : routeTrips.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? SORT_BY_ITEM : ITEM;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder.getItemViewType() == ITEM) {
            Timber.d("Bind route trip with id = %d, position = %d", getItemId(position), position);

            RouteTrip routeTrip = routeTrips.get(position - 1);
            ((RouteScheduleViewHolder) viewHolder).bind(context, routeTrip, this.route);

            // set loading indicator only for clicked item
            ((RouteScheduleViewHolder) viewHolder).setLoading(lastClickedItemPos == position && isItemLoading);
        } else {
            ((SortByViewHolder) viewHolder).bind(context, selectedSortingOption);
        }
    }

    private void sortBy(SortingOption sortingOption) {
        switch (sortingOption) {
            default:
            case DEPARTURE_TIME:
                Collections.sort(routeTrips, Comparator.comparingInt(RouteTrip::getComparableDepartureTimeHours)
                        .thenComparingInt(RouteTrip::getComparableDepartureTimeMinutes));
                break;
            case ARRIVAL_TIME:
                Collections.sort(routeTrips, Comparator.comparingInt(RouteTrip::getComparableArrivalTimeHours)
                        .thenComparingInt(RouteTrip::getComparableArrivalTimeMinutes).reversed());
                break;
            case CARRIER_RATING:
                Collections.sort(routeTrips, Comparator.comparingDouble(RouteTrip::getComparableCarrierRating).reversed());
                break;
            case AVAILABLE_SEATS:
                Collections.sort(routeTrips, Comparator.comparingInt(RouteTrip::getSeatsBooked));
                break;
            case PRICE:
                Collections.sort(routeTrips, Comparator.comparingDouble(RouteTrip::getComparablePrice)
                        .thenComparingDouble(RouteTrip::getComparableCarrierRating));
                break;
        }

        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onRouteTripItemClick(View view, String itemId);
    }

    public interface OnSortByItemClickListener {
        void onSortByItemClick(SortingOption selectedSortingOption);
    }

    public enum SortingOption {
        DEPARTURE_TIME("Departure time", 0),
        ARRIVAL_TIME("Arrival time", 1),
        CARRIER_RATING("Carrier rating", 2),
        AVAILABLE_SEATS("Available seats", 3),
        PRICE("Price", 4);

        private String option;
        private int position;

        SortingOption(String option, int position) {
            this.option = option;
            this.position = position;
        }

        public String getOption() {
            return option;
        }

        public int getPosition() {
            return position;
        }

        public ArrayList<String> getOptions() {
            return Arrays.stream(SortingOption.values())
                    .map(SortingOption::getOption).collect(Collectors.toCollection(ArrayList::new));
        }

        public static SortingOption getByPosition(int position) {
            Optional<SortingOption> optional = Arrays.stream(SortingOption.values())
                    .filter(o -> o.getPosition() == position).findFirst();
            return optional.orElse(SortingOption.DEPARTURE_TIME);
        }
    }

    static class SortByViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.btn_sort_by) MaterialButton buttonSortBy;

        private OnSortByItemClickListener onSortByItemClickListener;
        private SortingOption selectedSortingOption;

        public SortByViewHolder(View itemView, final OnSortByItemClickListener onSortByItemClickListener) {
            super(itemView);
            this.onSortByItemClickListener = onSortByItemClickListener;
            ButterKnife.bind(this, itemView);
        }

        public void bind(Context context, SortingOption selectedSortingOption) {
            this.selectedSortingOption = selectedSortingOption;

            String option = selectedSortingOption.getOption().toLowerCase();
            buttonSortBy.setText(context.getResources().getString(R.string.label_sort_by_option, option));
        }

        @OnClick(R.id.btn_sort_by)
        public void onSortByButtonClick(View itemView) {
            onSortByItemClickListener.onSortByItemClick(selectedSortingOption);
        }
    }

    static class RouteScheduleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_carrier) TextView textCarrierName;
        @BindView(R.id.tv_carrier_rating) TextView textCarrierRating;
        @BindView(R.id.tv_dep_time) TextView textDepartureTime;
        @BindView(R.id.tv_duration) TextView textDuration;
        @BindView(R.id.tv_arr_time) TextView textArrivalTime;
        @BindView(R.id.tv_dep_station) TextView textDepartureStation;
        @BindView(R.id.tv_arr_station) TextView textArrivalStation;
        @BindView(R.id.tv_available_seats) TextView textSeatsAvailable;
        @BindView(R.id.tv_cost) TextView textCost;
        @BindView(R.id.btn_select) ProgressMaterialButton btnSelect;
        @BindView(R.id.fl_trip) FrameLayout layoutRouteTrip;

        private String itemId;
        private OnItemClickListener onItemClickListener;

        public RouteScheduleViewHolder(@NonNull View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            ButterKnife.bind(this, itemView);
        }

        public void bind(Context context, RouteTrip routeTrip, Route route) {
            itemId = routeTrip.getId();

            textCarrierName.setText(routeTrip.getVehicle().getCarrier().getName());
            textCarrierRating.setText(routeTrip.getVehicle().getCarrier().getRating());
            textDepartureTime.setText(routeTrip.getDepartureTime());
            textDepartureStation.setText(route.getDepartureCity().getStation());
            textArrivalTime.setText(routeTrip.getArrivalTime());
            textArrivalStation.setText(route.getArrivalCity().getStation());
            textDuration.setText(routeTrip.getDuration());
            textSeatsAvailable.setText(context.getResources().getString(R.string.label_available_seats_count, routeTrip.getAvailableSeats()));
            textCost.setText(String.format("%s %s", routeTrip.getPrice(), routeTrip.getCurrency()));

            if (routeTrip.getAvailableSeats() == 0) {
                textSeatsAvailable.setTextColor(ContextCompat.getColor(context, R.color.colorError));
                btnSelect.setEnabled(false);
                btnSelect.setClickable(false);
            } else {
                textSeatsAvailable.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
                btnSelect.setEnabled(true);
                btnSelect.setClickable(true);
            }
        }

        @OnClick(R.id.btn_select)
        public void onSelectButtonClick(View itemView) {
            int itemPos = getAdapterPosition();

            if (isItemClickable && itemPos >= 0) {
                lastClickedItemPos = itemPos;
                onItemClickListener.onRouteTripItemClick(itemView, itemId);

                Timber.d("Select bus trip with id = %s, position = %d", itemId, itemPos);
            }
        }

        protected void setLoading(boolean isLoading) {
            btnSelect.setLoading(isLoading);
            btnSelect.setEnabled(!isLoading);
        }
    }
}
