package org.minibus.app.ui.schedule;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.minibus.app.data.network.pojo.route.Route;
import org.minibus.app.ui.R;
import org.minibus.app.data.network.pojo.schedule.RouteTrip;
import org.minibus.app.ui.custom.ProgressMaterialButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class RouteScheduleAdapter extends RecyclerView.Adapter<RouteScheduleAdapter.BusScheduleViewHolder> {

    public interface OnItemClickListener {
        void onBusTripSelect(View view, long id, int pos, String routeId);
    }

    private Context context;
    private List<RouteTrip> routeTrips = new ArrayList<>();
    private Route route = null;
    private OnItemClickListener clickListener;
    private boolean isItemClickable = true;
    private boolean isItemLoading = false;
    private int lastClickedItemPos = -1;

    public RouteScheduleAdapter(Context context) {
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setData(List<RouteTrip> newRouteTrips, Route route) {
        DiffUtil.DiffResult diffResult =
                DiffUtil.calculateDiff(new RouteScheduleDiffUtilCallback(this.routeTrips, newRouteTrips), true);

        this.route = route;
        this.routeTrips.clear();
        this.routeTrips.addAll(newRouteTrips);

        diffResult.dispatchUpdatesTo(this);
    }

    public void setLoading(boolean isLoading) {
        isItemLoading = isLoading;
        isItemClickable = !isLoading;

        if (lastClickedItemPos >= 0) notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BusScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int layoutIdForBusTrip = R.layout.view_bus_trip;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForBusTrip, viewGroup, false);

        return new BusScheduleViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return routeTrips.get(position).getLongId();
    }

    @Override
    public int getItemCount() {
        return routeTrips == null ? 0 : routeTrips.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final BusScheduleViewHolder viewHolder, final int position) {
        RouteTrip routeTrip = routeTrips.get(position);
        viewHolder.bind(routeTrip, this.route);

        Timber.d("Bind bus trip with id = %d, position = %d", getItemId(position), position);

        // set loading indicator only for clicked item
        viewHolder.setLoading(lastClickedItemPos == position && isItemLoading);
    }

    class BusScheduleViewHolder extends RecyclerView.ViewHolder {

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
        @BindView(R.id.ll_trip) CardView layoutBusTrip;

        private long itemId;
        private String routeId;

        public BusScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(RouteTrip routeTrip, Route route) {
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
            textCost.setText(String.format("%s %s", routeTrip.getCost(), routeTrip.getCurrency()));

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
                clickListener.onBusTripSelect(itemView, itemId, itemPos, routeId);

                Timber.d("Select bus trip with id = %d, position = %d", itemId, itemPos);
            }
        }

        protected void setLoading(boolean isLoading) {
            btnSelectTrip.setLoading(isLoading);
            btnSelectTrip.setEnabled(!isLoading);
        }
    }
}
