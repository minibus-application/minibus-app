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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class RouteScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface ItemClickListener {
        void onRouteTripItemClick(View view, long id, int pos, String routeId);
    }

    public interface SortByItemClickListener {
        void onSortByItemClick(SortingOption selectedSortingOption);
    }

    public enum SortingOption {
        DEPARTURE_TIME("Departure time", 0),
        ARRIVAL_TIME("Arrival time", 1),
        CARRIER_RATING("Carrier rating", 2),
        PRICE("Pricing", 3);

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
            return Arrays.stream(SortingOption.values())
                    .filter(o -> o.getPosition() == position).findFirst().get();
        }
    }

    private Context context;
    private Route route = null;
    private ItemClickListener itemClickListener;
    private SortByItemClickListener sortByItemClickListener;
    private List<RouteTrip> routeTrips = new ArrayList<>();
    private SortingOption selectedSortingOption;
    private static boolean isItemClickable = true;
    private static boolean isItemLoading = false;
    private static int lastClickedItemPos = -1;
    private static final int FILTER = 0;
    private static final int ITEM = 1;

    public RouteScheduleAdapter(Context context) {
        this.context = context;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setSortByItemClickListener(SortByItemClickListener sortByItemClickListener) {
        this.sortByItemClickListener = sortByItemClickListener;
    }

    public void setData(List<RouteTrip> newRouteTrips, Route route) {
        DiffUtil.DiffResult diffResult =
                DiffUtil.calculateDiff(new RouteScheduleDiffUtilCallback(this.routeTrips, newRouteTrips), true);

        this.route = route;
        this.routeTrips.clear();
        this.routeTrips.addAll(newRouteTrips);

        diffResult.dispatchUpdatesTo(this);
    }

    public void setSortingOption(SortingOption selectedSortingOption) {
        this.selectedSortingOption = selectedSortingOption;
        sortBy(selectedSortingOption);

        notifyItemChanged(0);
    }

    private void sortBy(SortingOption sortingOption) {
        switch (sortingOption) {
            case DEPARTURE_TIME:

                break;
            case ARRIVAL_TIME:

                break;
            case CARRIER_RATING:

                break;
            case PRICE:
                Collections.sort(routeTrips, (o1, o2) -> {
                    return o1.getPriceToCompare().compareTo(o2.getPriceToCompare());
                });
                break;
            default:
                break;
        }

        notifyDataSetChanged();
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

        if (viewType == FILTER) {
            return new SortByViewHolder(inflater.inflate(R.layout.view_filter, viewGroup, false), sortByItemClickListener);
        }
        return new RouteScheduleViewHolder(inflater.inflate(R.layout.view_bus_trip, viewGroup, false), itemClickListener);
    }

    @Override
    public long getItemId(int position) {
        return routeTrips.get(position - 1).getLongId();
    }

    @Override
    public int getItemCount() {
        return routeTrips == null ? 0 : routeTrips.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? FILTER : ITEM;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder.getItemViewType() == ITEM) {
            Timber.d("Bind bus trip with id = %d, position = %d", getItemId(position), position);

            RouteTrip routeTrip = routeTrips.get(position - 1);
            ((RouteScheduleViewHolder) viewHolder).bind(context, routeTrip, this.route);

            // set loading indicator only for clicked item
            ((RouteScheduleViewHolder) viewHolder).setLoading(lastClickedItemPos == position && isItemLoading);
        } else {
            ((SortByViewHolder) viewHolder).bind(context, selectedSortingOption);
        }
    }

    static class SortByViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.btn_sort) MaterialButton buttonSortBy;

        private SortByItemClickListener sortByItemClickListener;
        private SortingOption selectedSortingOption;

        public SortByViewHolder(View itemView, final SortByItemClickListener sortByItemClickListener) {
            super(itemView);
            this.sortByItemClickListener = sortByItemClickListener;
            ButterKnife.bind(this, itemView);
        }

        public void bind(Context context, SortingOption selectedSortingOption) {
            this.selectedSortingOption = selectedSortingOption;

            String option = selectedSortingOption.getOption().toLowerCase();
            buttonSortBy.setText(context.getResources().getString(R.string.label_sorting_by, option));
        }

        @OnClick(R.id.btn_sort)
        public void onSortByButtonClick(View itemView) {
            sortByItemClickListener.onSortByItemClick(selectedSortingOption);
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
        @BindView(R.id.btn_select) ProgressMaterialButton btnSelectTrip;
        @BindView(R.id.ll_trip) FrameLayout layoutBusTrip;

        private long itemId;
        private String routeId;
        private ItemClickListener itemClickListener;

        public RouteScheduleViewHolder(@NonNull View itemView, final ItemClickListener itemClickListener) {
            super(itemView);
            this.itemClickListener = itemClickListener;
            ButterKnife.bind(this, itemView);
        }

        public void bind(Context context, RouteTrip routeTrip, Route route) {
            itemId = routeTrip.getLongId();
            routeId = route.getId();

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
                layoutBusTrip.setEnabled(false);
            } else {
                textSeatsAvailable.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
                layoutBusTrip.setEnabled(true);
            }
        }

        @OnClick(R.id.btn_select)
        public void onSelectButtonClick(View itemView) {
            // have to get position by invoking getAdapterPosition()
            // because this is where sometimes the "position" value is not the proper one
            // and sometimes returns an exception regarding inconsistent items state.
            int itemPos = getAdapterPosition();

            if (isItemClickable && itemPos >= 0) {
                lastClickedItemPos = itemPos;
                itemClickListener.onRouteTripItemClick(itemView, itemId, itemPos, routeId);

                Timber.d("Select bus trip with id = %d, position = %d", itemId, itemPos);
            }
        }

        protected void setLoading(boolean isLoading) {
            btnSelectTrip.setLoading(isLoading);
            btnSelectTrip.setEnabled(!isLoading);
        }
    }
}
